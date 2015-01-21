package mmeent.java.main.connection.connection;

import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.connection.client.ClientPacket;
import mmeent.java.main.connection.connection.server.ServerPacket;

import java.util.HashMap;

/**
 * Created by Matthias on 21/01/2015.
 *
 * @author mmeent
 *
 *
 */
public class Packets {
    public static HashMap<String, Class<? extends ServerPacket>> serverpackets = new HashMap<String, Class<? extends ServerPacket>>();
    public static HashMap<String, Class<? extends ClientPacket>> clientpackets = new HashMap<String, Class<? extends ClientPacket>>();

    /**
     * Registers all packets to make the reading of packets easier.
     */
    public static void registerServerPackets(){
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
}
