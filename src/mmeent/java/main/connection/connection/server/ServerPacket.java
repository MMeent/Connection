package mmeent.java.main.connection.connection.server;

import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.board.Board;
import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.player.LeaderboardEntry;
import mmeent.java.main.connection.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthias on 14/01/2015.
 * @author mmeent
 */
public class ServerPacket implements Packet {
    private Connection connection;
    private String prefix;

    public ServerPacket(Connection c, String prefix){
        this.connection = c;
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

    public void returnError(int id, String e){
        this.connection.send(new ServerPacket.ErrorPacket(this.connection, id, e));
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
        private int id;
        private String msg;

        public ErrorPacket(Connection c, int id, String msg){
            super(c, Protocol.Server.ERROR);
            this.id = id;
            this.msg = msg;
        }

        public static ErrorPacket read(Connection c, String[] args){
            String msg = "";
            for(int i = 2; i < args.length; i++){
                msg += args[i];
            }
            return new ErrorPacket(c, Integer.parseInt(args[1]), msg);
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(Integer.toString(this.id));
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
    }

    public static class LobbyPacket extends ServerPacket{
        private LocalPlayer[] players;

        public static LobbyPacket read(Connection c, String[] args){
            LocalPlayer[] players = new LocalPlayer[args.length - 1];
            for(int i = 1; i < args.length; i++){
                players[i - 1] = new LocalPlayer(args[i]);
            }
            return new LobbyPacket(c, players);
        }

        public LobbyPacket(Connection c, LocalPlayer[] players){
            super(c, Protocol.Server.LOBBY);
            this.players = players;
        }

        public synchronized void write(Connection c){
            super.write(c);
            for(LocalPlayer localPlayer : this.players){
                c.writePartial(localPlayer.getName());
            }
            c.stopPacket();
            c.sendBuffer();
        }
    }

    public static class BoardPacket extends ServerPacket{
        private Board board;
        public static BoardPacket read(Connection c, String[] args){
            short width = Short.parseShort(args[1]);
            short height = Short.parseShort(args[2]);
            Board board = new Board(width, height);
            for(int i = 0; i < width * height; i++){
                if(!args[i + 3].equals("0")) board.move(i % width, Byte.parseByte(args[i + 3]));
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
        private LocalPlayer player;
        public static InvitePacket read(Connection c, String[] args){
            LocalPlayer player = new LocalPlayer(args[1]);
            return new InvitePacket(c, player);
        }

        public InvitePacket(Connection c, LocalPlayer player){
            super(c, Protocol.Server.INVITE);
            this.player = player;
        }
    }

    public static class GameStartPacket extends ServerPacket{
        private LocalPlayer player1;
        private LocalPlayer player2;

        public GameStartPacket(Connection c, LocalPlayer p1, LocalPlayer p2){
            super(c, Protocol.Server.GAME_START);
            this.player1 = p1;
            this.player2 = p2;
        }
    }

    public static class GameEndPacket extends ServerPacket{
        private String reason;
        private String extra;

        public GameEndPacket(Connection c, String reason, String extra){
            super(c, Protocol.Server.GAME_END);
            this.reason = reason;
            this.extra = extra;
        }
    }
}
