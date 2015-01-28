package mmeent.java.main.connection.connection.server;

import mmeent.java.main.connection.ConnectClient;
import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.board.Board;
import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.connection.client.ClientPacket;
import mmeent.java.main.connection.exception.ConnectFourException;
import mmeent.java.main.connection.exception.InvalidPacketException;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Move;
import mmeent.java.main.connection.player.LeaderboardEntry;
import mmeent.java.main.connection.player.Player;
import mmeent.java.main.connection.render.Renderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthias on 14/01/2015.
 * @author mmeent
 */
public class ServerPacket implements Packet {
    private Connection connection;
    private Player client;
    private String prefix;

    /**
     * Default and superconstructor for all server-sent packets.
     * @param c the connection of the packet
     * @param prefix the header of the packet
     */
    public ServerPacket(Connection c, String prefix){
        this.connection = c;
        if(c != null) this.client = c.getClient();
        this.prefix = prefix;
    }

    /**
     * The method to be called when a packet has to be sent to the client.
     * @param c the connection the packet has to be sent over
     */
    public synchronized void write(Connection c){
        c.startPacket();
        c.writePartial(this.prefix);
    }

    /**
     * An easy way to respond to a packet
     * @param packet the packet you are responding with
     */
    public void respond(Packet packet){
        this.connection.send(packet);
    }

    /**
     * The method called when a packet has been received and processed
     */
    public void onReceive(){

    }

    /**
     * The method called through reflection in <code>mmeent.java.main.connection.connection.Packets</code>;
     * @param c the connection the packet has been sent over.
     * @param args the <code>String[]</code> containing all the space-seperated values of the packet that has to be read.
     * @return a the packet nescessary
     * @throws InvalidPacketException
     */
    public static ServerPacket read(Connection c, String[] args) throws InvalidPacketException{
        return new ServerPacket(c, "SERVERPACKET");
    }

    /**
     * An easy way to send an error to the other side of the connection, if something went wrong.
     * @param e the error message.
     */
    public void returnError(String e){
        this.connection.send(new ServerPacket.ErrorPacket(this.connection, this.prefix, e));
    }

    /**
     * Get the connection the packet was sent over. Null if no connection available for that packet.
     * @return the connection of the packet
     */
    public Connection getConnection(){
        return this.connection;
    }

    /**
     * Get the client <code>Player</code> corresponding to the connection
     * @return the Player corresponding with the connection
     */
    public Player getClient(){
        return this.client;
    }

    /**
     * The server PONG packet
     * PROTOCOL: Protocol.Server.PONG
     */
    public static class PongPacket extends ServerPacket{
        public PongPacket(Connection c){
            super(c, Protocol.Server.PONG);
        }

        public static PongPacket read(Connection c, String[] args) throws InvalidPacketException {
            return new PongPacket(c);
        }

        @Override
        public synchronized void write(Connection c){
            super.write(c);
            c.stopPacket();
            c.sendBuffer();
        }
    }

    /**
     * The server ERROR packet
     * PROTOCOL: Protocol.Server.ERROR
     */
    public static class ErrorPacket extends ServerPacket{
        private String id;
        private String msg;

        public ErrorPacket(Connection c, String prefix, String msg){
            super(c, Protocol.Server.ERROR);
            this.id = prefix;
            this.msg = msg;
        }

        public static ErrorPacket read(Connection c, String[] args) throws InvalidPacketException{
            String msg = "";
            for(int i = 2; i < args.length; i++){
                msg += args[i] + " ";
            }
            if(msg.length() > 256) throw new InvalidPacketException("The message length is too long");
            return new ErrorPacket(c, args[1], msg);
        }

        @Override
        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(this.id);
            c.writePartial(this.msg);
            c.stopPacket();
            c.sendBuffer();
        }

        @Override
        public void onReceive(){
            ConnectClient.get().getRenderer().addErrorMessage(this.id, this.msg);
        }
    }

    /**
     * The server ACCEPT_CONNECT packet
     * PROTOCOL: Protocol.Server.ACCEPT_CONNECT
     */
    public static class AcceptConnectPacket extends ServerPacket{
        public AcceptConnectPacket(Connection c){
            super(c, Protocol.Server.ACCEPT_CONNECT);
        }

        public static AcceptConnectPacket read(Connection c, String[] args) throws InvalidPacketException{
            return new AcceptConnectPacket(c);
        }

        @Override
        public synchronized void write(Connection c){
            super.write(c);
            c.stopPacket();
            c.sendBuffer();
        }

        @Override
        public void onReceive(){
            ConnectClient.get().getRenderer().addMessage("Connected");
        }
    }

    /**
     * The server LOBBY packet
     * PROTOCOL: Protocol.Server.LOBBY
     */
    public static class LobbyPacket extends ServerPacket{
        private Player[] players;

        public static LobbyPacket read(Connection c, String[] args) throws InvalidPacketException{
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

        @Override
        public synchronized void write(Connection c){
            super.write(c);
            for(Player localPlayer : this.players){
                c.writePartial(localPlayer.getName());
            }
            c.stopPacket();
            c.sendBuffer();
        }

        @Override
        public void onReceive(){
            ConnectClient c = ConnectClient.get();
            Renderer r = c.getRenderer();
            r.addMessage("There are now " + this.players.length + " players online: ");
            StringBuilder b = new StringBuilder();
            for(Player player: this.players){
                if(b.length() > 0) b.append(", ");
                b.append(player.getName());
            }
            r.addMessage(b.toString());
            /*
            List<char[]> playernames = new ArrayList<char[]>();
            for(Player player: this.players){
                if(b.length() > 0) b.append(", ");
                if(b.length() > 155){
                    char[] pnames = new char[b.length()];
                    b.getChars(0, b.length() - 1, pnames, 0);
                    playernames.add(pnames);
                    b.delete(0, b.length() - 1);
                }
                b.append(player.getName());
            }

            for(char[] chars: playernames){
                r.addMessage(String.valueOf(chars));
            }*/
        }
    }

    /**
     * The server BOARD packet
     * PROTOCOL: Protocol.Server.BOARD
     */
    public static class BoardPacket extends ServerPacket{
        private Board board;
        public static BoardPacket read(Connection c, String[] args) throws InvalidPacketException{
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

        @Override
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

        @Override
        public void onReceive(){
            ConnectClient.get().getRenderer().setBoard(this.board);
            if(ConnectClient.get().getConnection().getClient().getGame() != null) ConnectClient.get().getConnection().getClient().getGame().setBoard(this.board);
        }
    }

    /**
     * The server LEADERBOARD packet
     * PROTOCOL: Protocol.Server.LEADERBOARD
     */
    public static class LeaderBoardPacket extends ServerPacket{
        private List<LeaderboardEntry> entries;
        public static LeaderBoardPacket read(Connection c, String[] args) throws InvalidPacketException{
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

        @Override
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

        @Override
        public void onReceive(){
            Renderer r = ConnectClient.get().getRenderer();
            r.addMessage("   Player name   | Wins | Loss | Play | Rank ");
            for(LeaderboardEntry e: this.entries){
                StringBuilder s = new StringBuilder();
                s.append(e.getPlayer().getName()).setLength(16);
                s.append("| ").append(e.getWins()).setLength(23);
                s.append("| ").append(e.getLoss()).setLength(30);
                s.append("| ").append(e.getTotal()).setLength(37);
                r.addMessage(s.append("| ").append(e.getRanking()).toString());
            }
        }
    }

    /**
     * The server INVITE packet
     * PROTOCOL: Protocol.Server.INVITE
     */
    public static class InvitePacket extends ServerPacket{
        private Player player;
        private short boardwidth;
        private short boardheight;

        public static InvitePacket read(Connection c, String[] args) throws InvalidPacketException{
            Player player = Player.get(args[1]);
            return new InvitePacket(c, player, Short.parseShort(args[2]), Short.parseShort(args[3]));
        }

        public InvitePacket(Connection c, Player player, short boardwidth, short boardheight){
            super(c, Protocol.Server.INVITE);
            this.player = player;
            this.boardwidth = boardwidth;
            this.boardheight = boardheight;
        }

        @Override
        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(this.player.getName());
            c.writePartial(Short.toString(this.boardwidth));
            c.writePartial(Short.toString(this.boardheight));
            c.stopPacket();
            c.sendBuffer();
        }

        @Override
        public void onReceive(){
            Renderer r = ConnectClient.get().getRenderer();
            r.addMessage("You have been invited to a game by " + this.player.getName());
        }
    }

    /**
     * The server GAME_START packet
     * PROTOCOL: Protocol.Server.GAME_START
     */
    public static class GameStartPacket extends ServerPacket{
        private Player player1;
        private Player player2;
        private String options = "";

        public static GameStartPacket read(Connection c, String[] args) throws InvalidPacketException{
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

        @Override
        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(this.player1.getName());
            c.writePartial(this.player2.getName());
            c.writePartial(this.options);
            c.stopPacket();
            c.sendBuffer();
        }

        @Override
        public void onReceive(){
            Player[] players = {this.player1, this.player2};
            Game game = new Game(players);
            Renderer r = ConnectClient.get().getRenderer();
            r.addMessage("A Game has started between " + this.player1.getName() + " and " + this.player2.getName());
            r.setBoard(game.getBoard());
            this.getClient().setGame(game);
        }
    }

    /**
     * The server GAME_END packet
     * PROTOCOL: Protocol.Server.GAME_END
     */
    public static class GameEndPacket extends ServerPacket{
        private String reason;
        private String extra;

        public static GameEndPacket read(Connection c, String[] args) throws InvalidPacketException{
            return new GameEndPacket(c, args[1], args[2]);
        }

        public GameEndPacket(Connection c, String reason, String extra){
            super(c, Protocol.Server.GAME_END);
            this.reason = reason;
            this.extra = extra;
        }

        @Override
        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(this.reason);
            c.writePartial(this.extra);
            c.stopPacket();
            c.sendBuffer();
        }

        @Override
        public void onReceive(){
            if(reason.equals("DISCONNECT")) ConnectClient.get().getRenderer().addMessage("You won: other client disconnected");
            if(reason.equals("WIN")) ConnectClient.get().getRenderer().addMessage(extra + " won the game");
            if(reason.equals("DRAW")) ConnectClient.get().getRenderer().addMessage("You both didn't win, it's a draw.");
            this.getClient().setGame(null);
        }
    }

    /**
     * The server REQUEST_MOVE packet
     * PROTOCOL: Protocol.Server.REQUEST_MOVE
     */
    public static class RequestMovePacket extends ServerPacket{
        public static RequestMovePacket read(Connection c, String[] args) throws InvalidPacketException{
            return new RequestMovePacket(c);
        }

        public RequestMovePacket(Connection c){
            super(c, Protocol.Server.REQUEST_MOVE);
        }

        @Override
        public synchronized void write(Connection c){
            super.write(c);
            c.stopPacket();
            c.sendBuffer();
        }

        @Override
        public void onReceive() {
            ConnectClient.get().getRenderer().addMessage("Make a move: MOVE <column number>");
        }
    }

    /**
     * The server MOVE_OK packet
     * PROTOCOL: Protocol.Server.MOVE_OK
     */
    public static class MoveOkPacket extends ServerPacket{
        private byte playerid;
        private short column;
        Player player;

        public static MoveOkPacket read(Connection c, String[] args) throws InvalidPacketException{
            byte playerid = Byte.valueOf(args[1]);
            short column = Short.valueOf(args[2]);
            Player player = args.length >= 3 ? Player.get(args[3]) : null;
            return new MoveOkPacket(c, playerid, column, player);
        }

        public MoveOkPacket(Connection c, byte playerid, short column, Player player){
            super(c, Protocol.Server.MOVE_OK);
            this.playerid = playerid;
            this.column = column;
            this.player = player;
        }

        @Override
        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(Byte.toString(this.playerid));
            c.writePartial(Short.toString(this.column));
            c.writePartial(this.getClient().getName());
            c.stopPacket();
            c.sendBuffer();
        }

        @Override
        public void onReceive(){
            try{
                this.getClient().getGame().move(new Move(this.playerid, this.column, this.getClient().getGame().getTurn(), this.getClient().getGame().getBoard()));
                ConnectClient.get().getRenderer().render();
            } catch(ConnectFourException e){
                e.printStackTrace(System.out);
            }
        }
    }

    /**
     * The server CHAT packet
     * PROTOCOL: Protocol.Server.CHAT
     */
    public static class ChatPacket extends ServerPacket {
        private String msg;
        private Player player;

        public static ChatPacket read(Connection c, String[] args) throws InvalidPacketException{
            StringBuilder msg = new StringBuilder();
            Player p = Player.get(args[1]);
            for (int i = 2; i < args.length; i++) {
                msg.append(args[i]).append(' ');
            }
            return new ChatPacket(c, p, msg.toString());
        }

        public ChatPacket(Connection c, Player p, String msg) {
            super(c, Protocol.Server.CHAT);
            this.player = p;
            this.msg = msg;
        }

        @Override
        public synchronized void write(Connection c) {
            super.write(c);
            c.writePartial(this.player.getName());
            c.writePartial(this.msg);
            c.stopPacket();
            c.sendBuffer();
        }

        @Override
        public void onReceive(){
            ConnectClient.get().getRenderer().addChatMessage("<" + this.player.getName() + "> " + this.msg);
        }
    }
}
