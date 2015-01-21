package mmeent.java.main.connection.connection.server;

import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.board.Board;
import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.player.LeaderboardEntry;
import mmeent.java.main.connection.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthias on 14/01/2015.
 * @author mmeent
 */
public class ServerPacket implements Packet {
    private Connection connection;
    private Player sender;
    private String prefix;

    public ServerPacket(Connection c, String prefix){
        this.connection = c;
        this.sender = c.getPlayer();
        this.prefix = prefix;
    }

    public synchronized void write(Connection c){
        c.startPacket();
        c.writePartial(this.prefix);
    }

    public void respond(Packet packet){
        this.connection.send(packet);
    }

    public void onReceive(){

    }

    public static ServerPacket read(Connection c, String[] args){
        return new ServerPacket(c, "SERVERPACKET");
    }

    public void returnError(String e){
        this.connection.send(new ServerPacket.ErrorPacket(this.connection, this.prefix, e));
    }

    public Connection getConnection(){
        return this.connection;
    }

    public Player getSender(){
        return this.sender;
    }

    public static class PongPacket extends ServerPacket{
        public PongPacket(Connection c){
            super(c, Protocol.Server.PONG);
        }

        public static PongPacket read(Connection c, String[] args){
            return new PongPacket(c);
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.stopPacket();
        }
    }

    public static class ErrorPacket extends ServerPacket{
        private String id;
        private String msg;

        public ErrorPacket(Connection c, String prefix, String msg){
            super(c, Protocol.Server.ERROR);
            this.id = prefix;
            this.msg = msg;
        }

        public static ErrorPacket read(Connection c, String[] args){
            String msg = "";
            for(int i = 2; i < args.length; i++){
                msg += args[i];
            }
            return new ErrorPacket(c, args[1], msg);
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(this.id);
            c.writePartial(this.msg);
        }
    }

    public static class AcceptConnectPacket extends ServerPacket{
        public AcceptConnectPacket(Connection c){
            super(c, Protocol.Server.ACCEPT_CONNECT);
        }

        public static AcceptConnectPacket read(Connection c, String[] args){
            return new AcceptConnectPacket(c);
        }

        public synchronized void write(Connection c){
            super.write(c);
        }
    }

    public static class LobbyPacket extends ServerPacket{
        private Player[] players;

        public static LobbyPacket read(Connection c, String[] args){
            Player[] players = new Player[args.length - 1];
            for(int i = 1; i < args.length; i++){
                players[i - 1] = Player.get(args[i]);
            }
            return new LobbyPacket(c, players);
        }

        public LobbyPacket(Connection c, Player[] players){
            super(c, Protocol.Server.LOBBY);
            this.players = players;
        }

        public synchronized void write(Connection c){
            super.write(c);
            for(Player localPlayer : this.players){
                c.writePartial(localPlayer.getName());
            }
            c.stopPacket();
            c.sendBuffer();
        }
    }

    public static class BoardPacket extends ServerPacket{
        private Board board;
        public static BoardPacket read(Connection c, String[] args){
            int width = Short.parseShort(args[1]);
            int height = Short.parseShort(args[2]);
            Board board = new Board((short) width, (short) height);
            for(int i = 0; i <width * height; i++){
                if(!args[i + 3].equals("0")) board.move((short) (i % width), Byte.parseByte(args[i + 3]));
            }
            return new BoardPacket(c, board);
        }

        public BoardPacket(Connection c, Board board){
            super(c, Protocol.Server.BOARD);
            this.board = board;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(Short.toString(this.board.getWidth()));
            c.writePartial(Short.toString(this.board.getHeight()));
            for(byte b: this.board.getFields()){
                c.writePartial(Byte.toString(b));
            }
            c.stopPacket();
            c.sendBuffer();
        }
    }

    public static class LeaderBoardPacket extends ServerPacket{
        private List<LeaderboardEntry> entries;
        public static LeaderBoardPacket read(Connection c, String[] args){
            List<LeaderboardEntry> entries = new ArrayList<LeaderboardEntry>();
            String name;
            int wins;
            int loss;
            int total;
            int ranking;
            for(int i = 1; i < args.length; i++){
                name = args[i++];
                wins = Integer.parseInt(args[i++]);
                loss = Integer.parseInt(args[i++]);
                total = Integer.parseInt(args[i++]);
                ranking = Integer.parseInt(args[i]);
                entries.add(new LeaderboardEntry(name, wins, loss, total, ranking));
            }
            return new LeaderBoardPacket(c, entries);
        }

        public LeaderBoardPacket(Connection c, List<LeaderboardEntry> entries){
            super(c, Protocol.Server.LEADERBOARD);
            this.entries = entries;
        }

        public synchronized void write(Connection c){
            super.write(c);
            for(LeaderboardEntry entry: this.entries){
                c.writePartial(entry.getPlayer().getName());
                c.writePartial(Integer.toString(entry.getWins()));
                c.writePartial(Integer.toString(entry.getLoss()));
                c.writePartial(Integer.toString(entry.getTotal()));
                c.writePartial(Integer.toString(entry.getRanking()));
            }
            c.stopPacket();
            c.sendBuffer();
        }
    }

    public static class InvitePacket extends ServerPacket{
        private Player player;
        private short boardwidth;
        private short boardheight;

        public static InvitePacket read(Connection c, String[] args){
            Player player = Player.get(args[1]);
            return new InvitePacket(c, player, Short.parseShort(args[2]), Short.parseShort(args[3]));
        }

        public InvitePacket(Connection c, Player player, short boardwidth, short boardheight){
            super(c, Protocol.Server.INVITE);
            this.player = player;
            this.boardwidth = boardwidth;
            this.boardheight = boardheight;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(this.player.getName());
            c.writePartial(Short.toString(this.boardwidth));
            c.writePartial(Short.toString(this.boardheight));
            c.stopPacket();
            c.sendBuffer();
        }
    }

    public static class GameStartPacket extends ServerPacket{
        private Player player1;
        private Player player2;
        private String options = "";

        public static GameStartPacket read(Connection c, String[] args){
            Player p1 = Player.get(args[1]);
            Player p2 = Player.get(args[2]);
            StringBuilder options = new StringBuilder();
            for(int i = 3; i < args.length; i++){
                options.append(' ').append(args[i]);
            }
            return new GameStartPacket(c, p1, p2, options.toString());
        }

        public GameStartPacket(Connection c, Player p1, Player p2){
            super(c, Protocol.Server.GAME_START);
            this.player1 = p1;
            this.player2 = p2;
        }

        public GameStartPacket(Connection c, Player p1, Player p2, String options){
            this(c, p1, p2);
            this.options = options;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(this.player1.getName());
            c.writePartial(this.player2.getName());
            c.writePartial(this.options);
            c.stopPacket();
            c.sendBuffer();
        }
    }

    public static class GameEndPacket extends ServerPacket{
        private String reason;
        private String extra;

        public static GameEndPacket read(Connection c, String[] args){
            return new GameEndPacket(c, args[1], args[2]);
        }

        public GameEndPacket(Connection c, String reason, String extra){
            super(c, Protocol.Server.GAME_END);
            this.reason = reason;
            this.extra = extra;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(this.reason);
            c.writePartial(this.extra);
            c.stopPacket();
            c.sendBuffer();
        }
    }

    public static class RequestMovePacket extends ServerPacket{
        public static RequestMovePacket read(Connection c, String[] args){
            return new RequestMovePacket(c);
        }

        public RequestMovePacket(Connection c){
            super(c, Protocol.Server.REQUEST_MOVE);
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.stopPacket();
            c.sendBuffer();
        }
    }

    public static class MoveOkPacket extends ServerPacket{
        private byte playerid;
        private short column;
        Player player;

        public static MoveOkPacket read(Connection c, String[] args){
            byte player_number = Byte.valueOf(args[1]);
            short column = Short.valueOf(args[2]);
            Player player = args.length >= 3 ? Player.get(args[3]) : null;
            return new MoveOkPacket(c, player_number, column, player);
        }

        public MoveOkPacket(Connection c, byte playerid, short column, Player player){
            super(c, Protocol.Server.MOVE_OK);
            this.playerid = playerid;
            this.column = column;
            this.player = player;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(Byte.toString(this.playerid));
            c.writePartial(Short.toString(this.column));
            c.writePartial(this.player.getName());
        }
    }

    public static class ChatPacket extends ServerPacket{
        private String msg;

        public static ChatPacket read(Connection c, String[] args){
            StringBuilder msg = new StringBuilder();
            for(int i = 1; i < args.length; i++){
                msg.append(args[i]).append(' ');
            }
            return new ChatPacket(c, msg.toString());
        }

        public ChatPacket(Connection c, String msg){
            super(c, Protocol.Server.CHAT);
            this.msg = msg;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(this.msg);
            c.stopPacket();
            c.sendBuffer();
        }
    }
}
