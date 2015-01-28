package mmeent.java.main.connection.connection.client;

import mmeent.java.main.connection.ConnectServer;
import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.connection.server.ServerPacket;
import mmeent.java.main.connection.exception.ConnectFourException;
import mmeent.java.main.connection.exception.InvalidPacketException;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Invite;
import mmeent.java.main.connection.game.Move;
import mmeent.java.main.connection.player.Player;

import java.util.Arrays;

/**
 * Created by Matthias on 14/01/2015.
 * @author mmeent
 */
public class ClientPacket implements Packet {
    private Connection connection;
    private String prefix;
    private Player client = null;

    /**
     * Default constructor for the client-sent packets.
     *
     * @param connection the connection the packet was sent over
     * @param prefix the header of the packet, as defined by the protocol.
     */
    public ClientPacket(Connection connection, String prefix){
        this.connection = connection;
        if(connection != null) this.client = connection.getClient();
        this.prefix = prefix;
    }

    /**
     * The method called by <code>mmeent.java.main.connection.connection.Packets</code> via reflection to create a new packet.
     * @param c the connection over which the packet was sent
     * @param args the resulting <code>String[]</code> of arguments when the packet is split on spaces.
     * @return null if no packet can be constructed using the given arguments, else the new packet.
     * @throws InvalidPacketException throws exception if the incoming package is not valid.
     */
    public static ClientPacket read(Connection c, String[] args) throws InvalidPacketException{
        return new ClientPacket(c, "CLIENTPACKET");
    }

    /**
     * An easy way to return an error to the client.
     * @param e the error message to be sent
     */
    public void returnError(String e){
        this.connection.send(new ClientPacket.ErrorPacket(this.connection, this.prefix, e));
    }

    /**
     * The supermethod to make writing packets easier.
     * @param c the connection to which the packet has to be sent.
     */
    public synchronized void write(Connection c){
        c.startPacket();
        c.writePartial(this.prefix);
    }

    /**
     * An easy way to respond to an packet.
     * @param packet the packet you are responding with
     */
    public void respond(Packet packet){
        this.connection.send(packet);
    }

    /**
     * The method called when the packet has been received. This is overridden per-packet.
     */
    public void onReceive(){

    }

    /**
     * An easy way to get the connection over which the packet has been sent.
     * @return the corresponding connection.
     */
    public Connection getConnection(){
        return this.connection;
    }

    /**
     * An easy way to get the corresponding client for the sent packet
     * @return the client corresponding to the packet.
     */
    public Player getClient(){
        return this.client;
    }

    /**
     * The client CHAT packet
     * PROTOCOL: Protocol.Client.CHAT
     */
    public static class ChatPacket extends ClientPacket{
        private String message = "";

        public static ChatPacket read(Connection c, String[] args) throws InvalidPacketException{
            String message = "";
            for (int i = 1; i < args.length; i++) {
                message += " " + args[i];
            }
            return new ChatPacket(c, message);
        }

        public ChatPacket(Connection c, String message){
            super(c, Protocol.Client.CHAT);
            this.message = message;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(message);
            c.stopPacket();
            c.sendBuffer();
        }

        public void onReceive(){
            ConnectServer.server.chat.addMessage(this.getClient(), this.message);
        }
    }

    /**
     * The client CONNECT packet
     * PROTOCOL: Protocol.Client.CONNECT
     */
    public static class ConnectPacket extends ClientPacket{
        private String username;
        private String[] options;

        public static ConnectPacket read(Connection c, String[] args) throws InvalidPacketException{
            String username = args[1];
            String[] options = (args.length > 1) ? Arrays.copyOfRange(args, 2, args.length) : new String[0];
            return new ConnectPacket(c, username, options);
        }

        public ConnectPacket(Connection c, String username, String[] options){
            super(c, Protocol.Client.CONNECT);
            this.username = username;
            this.options = options;
            c.setClient(Player.get(username));
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(username);
            for(String opt: this.options){
                c.writePartial(opt);
            }
            c.stopPacket();
            c.sendBuffer();
        }

        public void onReceive(){
            this.getConnection().setClient(Player.get(this.username));
            ConnectServer.server.addPlayerConnection(Player.get(this.username), this.getConnection());
            this.respond(new ServerPacket.AcceptConnectPacket(this.getConnection()));
            Player[] players = new Player[ConnectServer.server.getPlayers().size()];
            players = ConnectServer.server.getPlayers().toArray(players);
            ConnectServer.server.notifyAll(new ServerPacket.LobbyPacket(this.getConnection(), players));
        }
    }

    /**
     * The client INVITE packet
     * PROTOCOL: Protocol.Client.INVITE
     */
    public static class InvitePacket extends ClientPacket{
        private String playername;
        private short boardwidth;
        private short boardheight;

        public static InvitePacket read(Connection c, String[] args) throws InvalidPacketException{
            String playername = args[1];
            if(args.length < 4) return new InvitePacket(c, playername);
            return new InvitePacket(c, playername, Short.parseShort(args[2]), Short.parseShort(args[3]));
        }

        public InvitePacket(Connection c, String playername){
            this(c, playername, (short) 7, (short) 6);
        }

        public InvitePacket(Connection c, String playername, short width, short height){
            super(c, Protocol.Client.INVITE);
            this.playername = playername;
            this.boardwidth = width;
            this.boardheight = height;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(this.playername);
            c.writePartial(Integer.toString(this.boardwidth));
            c.writePartial(Integer.toString(this.boardheight));
            c.stopPacket();
            c.sendBuffer();
        }

        public void onReceive(){
            Player invited = Player.get(this.playername);
            Player inviter = this.getClient();
            new Invite(inviter, invited, this.boardwidth, this.boardheight);
            invited.getConnection().send(new ServerPacket.InvitePacket(invited.getConnection(), inviter, this.boardwidth, this.boardheight));
        }
    }

    /**
     * The client ACCEPT_INVITE packet
     * PROTOCOL: Protocol.Client.ACCEPT_INVITE
     */
    public static class AcceptInvitePacket extends ClientPacket{
        private String username;

        public static AcceptInvitePacket read(Connection c, String[] args) throws InvalidPacketException{
            String username = args[1];
            return new AcceptInvitePacket(c, username);
        }

        public AcceptInvitePacket(Connection c, String username){
            super(c, Protocol.Client.ACCEPT_INVITE);
            this.username = username;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(this.username);
            c.stopPacket();
            c.sendBuffer();
        }

        public synchronized void onReceive(){
            try{
                Player invited = this.getClient();
                if(invited.getGame() != null) throw new ConnectFourException("You should not accept a game when in a game");
                Player inviter = Player.get(this.username);
                Game g = Invite.invites.get(inviter).get(invited).startGame();
                g.getActivePlayer().getConnection().send(new ServerPacket.RequestMovePacket(g.getActivePlayer().getConnection()));
            } catch(ConnectFourException e){
                this.returnError(e.getMessage());
            } catch (Exception e){
                e.printStackTrace(System.out);
            }
        }
    }

    /**
     * The client DECLINE_INVITE packet
     * PROTOCOL: Protocol.Client.DECLINE_INVITE
     */
    public static class DeclineInvitePacket extends ClientPacket{
        private String username;

        public static DeclineInvitePacket read(Connection c, String[] args) throws InvalidPacketException{
            String username = args[1];
            return new DeclineInvitePacket(c, username);
        }

        public DeclineInvitePacket(Connection c, String username){
            super(c, Protocol.Client.DECLINE_INVITE);
            this.username = username;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.stopPacket();
            c.sendBuffer();
        }

        public void onReceive(){
            Player inviter = Player.get(username);
            Player invited = this.getClient();
            Invite.invites.get(inviter).remove(invited);
        }
    }

    /**
     * The client MOVE packet
     * PROTOCOL: Protocol.Client.MOVE
     */
    public static class MovePacket extends ClientPacket{
        private short column;

        public static MovePacket read(Connection c, String[] args) throws InvalidPacketException{
            short column = Short.valueOf(args[1]);
            return new MovePacket(c, column);
        }

        public MovePacket(Connection c, short column){
            super(c, Protocol.Client.MOVE);
            this.column = column;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(Short.toString(this.column));
            c.stopPacket();
            c.sendBuffer();
        }

        public void onReceive(){
            Player sender = this.getClient();
            if(sender.getGame() == null) return;
            try{
                sender.getGame().move(new Move(sender, column, sender.getGame().getTurn()));
            } catch (ConnectFourException e){
                e.printStackTrace();
                this.returnError(e.getMessage());
            }
        }
    }

    /**
     * The client QUIT packet
     * PROTOCOL: Protocol.Client.QUIT
     */
    public static class QuitPacket extends ClientPacket{
        private String reason = "";

        public static QuitPacket read(Connection c, String[] args) throws InvalidPacketException{
            String reason = "";
            if(args.length > 1) for (int i = 1; i < args.length; i++) {
                reason += args[i];
            }
            return new QuitPacket(c, reason);
        }

        public QuitPacket(Connection c, String reason){
            super(c, Protocol.Client.QUIT);
            this.reason = reason;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(reason);
            c.stopPacket();
            c.sendBuffer();
        }

        public void onReceive(){
            ConnectServer.server.removePlayer(this.getClient());
            this.getConnection().quit();
        }
    }

    /**
     * The client REQUEST_BOARD packet
     * PROTOCOL: Protocol.Client.REQUEST_BOARD
     */
    public static class RequestBoardPacket extends ClientPacket{
        public static RequestBoardPacket read(Connection c, String[] args) throws InvalidPacketException{
            return new RequestBoardPacket(c);
        }

        public RequestBoardPacket(Connection c){
            super(c, Protocol.Client.REQUEST_BOARD);
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.stopPacket();
            c.sendBuffer();
        }

        public void onReceive(){
            if(this.getClient().getGame() != null) this.respond(new ServerPacket.BoardPacket(this.getConnection(), this.getClient().getGame().getBoard()));
            else this.returnError("You have to be in a game to get a board");
        }
    }

    /**
     * The client REQUEST_LEADERBOARD packet
     * PROTOCOL: Protocol.Client.REQUEST_LEADERBOARD
     */
    public static class RequestLeaderboardPacket extends ClientPacket{
        public static RequestLeaderboardPacket read(Connection c, String[] args) throws InvalidPacketException {
            return new RequestLeaderboardPacket(c);
        }

        public RequestLeaderboardPacket(Connection c){
            super(c, Protocol.Client.REQUEST_LEADERBOARD);
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.stopPacket();
            c.sendBuffer();
        }

        public void onReceive(){

        }
    }

    /**
     * The client REQUEST_LOBBY packet
     * PROTOCOL: Protocol.Client.REQUEST_LOBBY
     */
    public static class RequestLobbyPacket extends ClientPacket{
        public static RequestLobbyPacket read(Connection c, String[] args) throws InvalidPacketException{
            return new RequestLobbyPacket(c);
        }

        public RequestLobbyPacket(Connection c){
            super(c, Protocol.Client.REQUEST_LOBBY);
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.stopPacket();
            c.sendBuffer();
        }

        public void onReceive(){
            this.respond(new ServerPacket.LobbyPacket(this.getConnection(), (Player[]) ConnectServer.server.getPlayers().toArray()));
        }
    }

    /**
     * The client ERROR packet
     * PROTOCOL: Protocol.Client.ERROR
     */
    public static class ErrorPacket extends ClientPacket{
        private String code;
        private String message;

        public static ErrorPacket read(Connection c, String[] args) throws InvalidPacketException{
            String code = args[1];
            String message = "";
            for(int i = 2; i < args.length; i++){
                message += args[i];
            }
            return new ErrorPacket(c, code, message);
        }

        public ErrorPacket(Connection c, String prefix, String message){
            super(c, Protocol.Client.ERROR);
            this.code = prefix;
            this.message = message;
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.writePartial(this.code);
            c.writePartial(this.message);
            c.stopPacket();
            c.sendBuffer();
        }

        public void onReceive(){
            System.out.println("Error received from client: " + this.code + " : " + message);
        }
    }

    /**
     * The client PING packet
     * PROTOCOL: Protocol.Client.PING
     */
    public static class PingPacket extends ClientPacket{
        public static PingPacket read(Connection c, String[] args) throws InvalidPacketException{
            return new PingPacket(c);
        }

        public PingPacket(Connection c){
            super(c, Protocol.Client.PING);
        }

        public synchronized void write(Connection c){
            super.write(c);
            c.stopPacket();
            c.sendBuffer();
        }

        public void onReceive(){
            this.respond(new ServerPacket.PongPacket(this.getConnection()));
        }
    }
}
