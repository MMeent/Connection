package mmeent.java.main.connection.connection;

import mmeent.java.main.connection.Protocol
import mmeent.java.main.connection.connection.server.ServerPacket;

import java.util.HashMap;

/**
 * Created by Matthias on 21/01/2015.
 */
public class Packets {
    public static HashMap<String, Class<? extends ServerPacket>> serverpackets = new HashMap<String, Class<? extends ServerPacket>>();

    public static void registerServerPackets(){
        serverpackets.put(Protocol.Server.ACCEPT_CONNECT, ServerPacket.AcceptConnectPacket.class);
        serverpackets.put(Protocol.Server.BOARD, ServerPacket.BoardPacket.class);
        serverpackets.put(Protocol.Server.CHAT, ServerPacket.ChatPacket.class);
        serverpackets.put(Protocol.Server.ACCEPT_CONNECT, ServerPacket.AcceptConnectPacket.class);
        serverpackets.put(Protocol.Server.ACCEPT_CONNECT, ServerPacket.AcceptConnectPacket.class);
        serverpackets.put(Protocol.Server.ACCEPT_CONNECT, ServerPacket.AcceptConnectPacket.class);
        serverpackets.put(Protocol.Server.ACCEPT_CONNECT, ServerPacket.AcceptConnectPacket.class);
    }
}
