package mmeent.java.main.connection.connection;

import mmeent.java.main.connection.ConnectClient;
import mmeent.java.main.connection.ConnectServer;
import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.connection.client.ClientPacket;
import mmeent.java.main.connection.connection.server.ServerPacket;
import mmeent.java.main.connection.exception.InvalidPacketException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Created by Matthias on 21/01/2015.
 *
 * @author mmeent
 */
public class Packets {
    public static HashMap<String, Class<? extends ServerPacket>> serverpackets = new HashMap<String, Class<? extends ServerPacket>>();
    public static HashMap<String, Class<? extends ClientPacket>> clientpackets = new HashMap<String, Class<? extends ClientPacket>>();

    /**
     * Registers all serverpackets to make the reading of packets easier.
     * It saves the header of each packet type with the class of each packet, so that we can later use reflection to
     * easily create a new packet, using the header as a key.
     */
    public static void registerServerPackets() {
        Packets.serverpackets.put(Protocol.Server.ACCEPT_CONNECT, ServerPacket.AcceptConnectPacket.class);
        Packets.serverpackets.put(Protocol.Server.BOARD, ServerPacket.BoardPacket.class);
        Packets.serverpackets.put(Protocol.Server.CHAT, ServerPacket.ChatPacket.class);
        Packets.serverpackets.put(Protocol.Server.ERROR, ServerPacket.ErrorPacket.class);
        Packets.serverpackets.put(Protocol.Server.GAME_END, ServerPacket.GameEndPacket.class);
        Packets.serverpackets.put(Protocol.Server.GAME_START, ServerPacket.GameStartPacket.class);
        Packets.serverpackets.put(Protocol.Server.INVITE, ServerPacket.InvitePacket.class);
        Packets.serverpackets.put(Protocol.Server.LEADERBOARD, ServerPacket.LeaderBoardPacket.class);
        Packets.serverpackets.put(Protocol.Server.LOBBY, ServerPacket.LobbyPacket.class);
        Packets.serverpackets.put(Protocol.Server.MOVE_OK, ServerPacket.MoveOkPacket.class);
        Packets.serverpackets.put(Protocol.Server.PONG, ServerPacket.PongPacket.class);
        Packets.serverpackets.put(Protocol.Server.REQUEST_MOVE, ServerPacket.RequestMovePacket.class);
    }

    /**
     * Registers all client packets to make the reading of packets easier.
     */
    public static void registerClientPackets() {
        Packets.clientpackets.put(Protocol.Client.ACCEPT_INVITE, ClientPacket.AcceptInvitePacket.class);
        Packets.clientpackets.put(Protocol.Client.CHAT, ClientPacket.ChatPacket.class);
        Packets.clientpackets.put(Protocol.Client.CONNECT, ClientPacket.ConnectPacket.class);
        Packets.clientpackets.put(Protocol.Client.DECLINE_INVITE, ClientPacket.DeclineInvitePacket.class);
        Packets.clientpackets.put(Protocol.Client.ERROR, ClientPacket.ErrorPacket.class);
        Packets.clientpackets.put(Protocol.Client.INVITE, ClientPacket.InvitePacket.class);
        Packets.clientpackets.put(Protocol.Client.MOVE, ClientPacket.MovePacket.class);
        Packets.clientpackets.put(Protocol.Client.PING, ClientPacket.PingPacket.class);
        Packets.clientpackets.put(Protocol.Client.QUIT, ClientPacket.QuitPacket.class);
        Packets.clientpackets.put(Protocol.Client.REQUEST_BOARD, ClientPacket.RequestBoardPacket.class);
        Packets.clientpackets.put(Protocol.Client.REQUEST_LEADERBOARD, ClientPacket.RequestLeaderboardPacket.class);
        Packets.clientpackets.put(Protocol.Client.REQUEST_LOBBY, ClientPacket.RequestLobbyPacket.class);
    }

    /**
     *
     * @param c the connection the packet was sent over
     * @param msg the actual packet that was sent
     * @return a packet if possible, otherwise null;
     */
    public static ServerPacket readServerPacket(Connection c, String msg){
        if(ConnectServer.debug || ConnectClient.debug){
            System.out.println(" :> " + msg);
        }
        try {
            String[] args = msg.split(" ");
            return (ServerPacket) Packets.serverpackets.get(args[0]).getMethod("read", Connection.class, String[].class).invoke(null, c, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace(System.out);
        } catch (InvocationTargetException e){
            if(e.getCause() instanceof InvalidPacketException)
                c.send(new ClientPacket.ErrorPacket(c, msg.split(" ")[0], "Your message does not comply with Protocol v1.3: " + e.getCause().getMessage()));
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
        return null;
    }

    /**
     * Read a client packet from a string.
     * @param c the connection the packet was sent over.
     * @param msg the packet that was sent
     * @return A packet instance that can be used by the system, if fail then null
     */
    public static ClientPacket readClientPacket(Connection c, String msg){
        if(ConnectServer.debug || ConnectClient.debug){
            if(ConnectServer.isServer) System.out.print(c.getClient().getName());
            System.out.println(" :> " + msg);
        }
        try {
            String[] args = msg.split(" ");
            return (ClientPacket) Packets.clientpackets.get(args[0]).getMethod("read", Connection.class, String[].class).invoke(null, c, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace(System.out);
        } catch (InvocationTargetException e){
            if(e.getCause() instanceof InvalidPacketException)
                c.send(new ServerPacket.ErrorPacket(c, msg.split(" ")[0], "Your message does not comply with Protocol v1.3: " + e.getCause().getMessage()));
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace(System.out);
        }
        return null;
    }
}
