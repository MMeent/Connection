package mmeent.java.main.connection.connection.client;

import mmeent.java.main.connection.ConnectServer;
import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.connection.server.ServerPacket;
import mmeent.java.main.connection.exception.ConnectFourException;
import mmeent.java.main.connection.game.Invite;
import mmeent.java.main.connection.game.Move;
import mmeent.java.main.connection.player.Player;

import java.util.Arrays;

/**
 * Created by Matthias on 14/01/2015.
 */
public class ClientPacket implements Packet {
    private Connection connection;
    private String prefix;
    private Player sender = null;

    public ClientPacket(Connection connection, String prefix){
        this.connection = connection;
        this.sender = connection.getPlayer();
        this.prefix = prefix;
    }

    public static ClientPacket read(Connection c, String[] args){
        return new ClientPacket(c, "CLIENTPACKET");
    }

    public void returnError(String e){
        this.connection.send(new ClientPacket.ErrorPacket(this.connection, this.prefix, e));
    }

    public synchronized void write(Connection c){
        c.writePartial(this.prefix);
    }

    public void returnError(String prefix, String e){
        this.connection.send(new ClientPacket.ErrorPacket(this.connection, this.prefix, e));
    }

    public void respond(Packet packet){
        this.connection.send(packet);
    }

    public void onReceive(){

    }

    public Connection getConnection(){
        return this.connection;
    }

    public Player getSender(){
        return this.sender;
    }

    public static class ChatPacket extends ClientPacket{
        private String message = "";

        public static ChatPacket read(Connection c, String[] args){
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
            ConnectServer.server.chat.addMessage(this.message);
        }
    }

    public static class ConnectPacket extends ClientPacket{
        private String username;
        private String[] options;

        public static ConnectPacket read(Connection c, String[] args){
            String username = args[1];
            String[] options = (args.length > 1) ? Arrays.copyOfRange(args, 2, args.length) : new String[0];
            return new ConnectPacket(c, username, options);
        }

        public ConnectPacket(Connection c, String username, String[] options){
            super(c, Protocol.Client.CONNECT);
            this.username = username;
            this.options = options;
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
            this.getConnection().setPlayer(Player.get(this.username));
            ConnectServer.server.addPlayerConnection(Player.get(this.username), this.getConnection());
        }
    }

    public static class InvitePacket extends ClientPacket{
        private String playername;
        private short boardwidth;
        private short boardheight;

        public static InvitePacket read(Connection c, String[] args){
            String playername = args[1];
            if(args.length == 2) return new InvitePacket(c, playername);
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
        }

        public void onReceive(){
            Player invited = Player.get(this.playername);
            Player inviter = this.getSender();
            new Invite(inviter, invited, this.boardwidth, this.boardheight);
            invited.getConnection().send(new ServerPacket.InvitePacket(invited.getConnection(), inviter, this.boardwidth, this.boardheight));
        }
    }

    public static class AcceptInvitePacket extends ClientPacket{
        private String username;

        public static AcceptInvitePacket read(Connection c, String[] args){
            String username = args[1];
            return new AcceptInvitePacket(c, username);
        }

        public AcceptInvitePacket(Connection c, String username){
            super(c, Protocol.Client.ACCEPT_INVITE);
            this.username = username;
        }

        public void onReceive(){
            Player invited = this.getSender();
            Player inviter = Player.get(this.username);
            ((Invite) Invite.invites.get(inviter).get(invited)).startGame().start();
        }
    }

    public static class DeclineInvitePacket extends ClientPacket{
        private String username;

        public static DeclineInvitePacket read(Connection c, String[] args){
            String username = args[1];
            return new DeclineInvitePacket(c, username);
        }

        public DeclineInvitePacket(Connection c, String username){
            super(c, Protocol.Client.DECLINE_INVITE);
            this.username = username;
        }

        public void onReceive(){
            Player inviter = Player.get(username);
            Player invited = this.getSender();
            Invite.invites.get(inviter).remove(invited);
        }
    }

    public static class MovePacket extends ClientPacket{
        private short column;

        public static MovePacket read(Connection c, String[] args){
            short column = Short.valueOf(args[1]);
            return new MovePacket(c, column);
        }

        public MovePacket(Connection c, short column){
            super(c, Protocol.Client.MOVE);
            this.column = column;
        }

        public void onReceive(){
            Player sender = this.getSender();
            if(sender.getGame() == null) return;
            try{
                sender.getGame().move(new Move(sender, column, sender.getGame().getTurn()));
            } catch (ConnectFourException e){
                this.returnError(e.getMessage());
            }
        }
    }

    public static class QuitPacket extends ClientPacket{
        private String reason = "";

        public static QuitPacket read(Connection c, String[] args){
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

        public void onReceive(){
            ConnectServer.server.removePlayer(this.getSender());
        }
    }

    public static class RequestBoardPacket extends ClientPacket{
        public static RequestBoardPacket read(Connection c, String[] args){
            return new RequestBoardPacket(c);
        }

        public RequestBoardPacket(Connection c){
            super(c, Protocol.Client.REQUEST_BOARD);
        }

        public void onReceive(){
            if(this.getSender().getGame() != null) this.respond(new ServerPacket.BoardPacket(this.getConnection(), this.getSender().getGame().getBoard()));
            else this.returnError("You have to be in a game to get a board");
        }
    }

    public static class RequestLeaderboardPacket extends ClientPacket{
        public static RequestLeaderboardPacket read(Connection c, String[] args){
            return new RequestLeaderboardPacket(c);
        }

        public RequestLeaderboardPacket(Connection c){
            super(c, Protocol.Client.REQUEST_LEADERBOARD);
        }
    }

    public static class RequestLobbyPacket extends ClientPacket{
        public static RequestLobbyPacket read(Connection c, String[] args){
            return new RequestLobbyPacket(c);
        }

        public RequestLobbyPacket(Connection c){
            super(c, Protocol.Client.REQUEST_LOBBY);
        }

        public void onReceive(){
            this.respond(new ServerPacket.LobbyPacket(this.getConnection(), (Player[]) ConnectServer.server.getPlayers().toArray()));
        }
    }

    public static class ErrorPacket extends ClientPacket{
        private String code;
        private String message;

        public static ErrorPacket read(Connection c, String[] args){
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

        public void onReceive(){
            System.out.println("Error received from client: " + this.code + " : " + message);
        }
    }

    public static class PingPacket extends ClientPacket{
        public static PingPacket read(Connection c, String[] args){
            return new PingPacket(c);
        }

        public PingPacket(Connection c){
            super(c, Protocol.Client.PING);
        }

        public void onReceive(){
            this.respond(new ServerPacket.PongPacket(this.getConnection()));
        }
    }
}
