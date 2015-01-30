package mmeent.test.main.connection.connection;

import junit.framework.TestCase;
import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packets;
import mmeent.java.main.connection.connection.Side;
import mmeent.java.main.connection.connection.client.ClientPacket;
import mmeent.java.main.connection.connection.server.ServerPacket;
import org.junit.Before;

public class PacketsTest extends TestCase {
    @Before
    public void before() {
        Packets.clientPackets.clear();
        Packets.serverPackets.clear();
    }
    
    public void testRegisterServerPackets() throws Exception {
        assertEquals("Server packet list must be clear when started", 0,
                Packets.serverPackets.size());
        Packets.registerServerPackets();
        assertEquals("Server packet list must contain 13 packets after registering",
                13, Packets.serverPackets.size());
        
        assertTrue("Accept Connect should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.ACCEPT_CONNECT));
        assertTrue("Board should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.BOARD));
        assertTrue("Chat should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.CHAT));
        assertTrue("Decline Invite should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.DECLINE_INVITE));
        assertTrue("Error should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.ERROR));
        assertTrue("Game End should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.GAME_END));
        assertTrue("Game Start should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.GAME_START));
        assertTrue("Invite should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.INVITE));
        assertTrue("Leaderboard should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.LEADERBOARD));
        assertTrue("Lobby should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.LOBBY));
        assertTrue("Move OK should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.MOVE_OK));
        assertTrue("Pong should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.PONG));
        assertTrue("Request Move should be in server packet list", 
                Packets.serverPackets.containsKey(Protocol.Server.REQUEST_MOVE));
    }

    public void testRegisterClientPackets() throws Exception {
        assertEquals("Client packet list must be clear when started", 0,
                Packets.clientPackets.size());
        Packets.registerClientPackets();
        assertEquals("Client packet list must contain 12 packets after registering", 12,
                Packets.clientPackets.size());
        
        assertTrue("Accept Invite should be in the client packets list",
                Packets.clientPackets.containsKey(Protocol.Client.ACCEPT_INVITE));
        assertTrue("Chat should be in the client packets list",
                Packets.clientPackets.containsKey(Protocol.Client.CHAT));
        assertTrue("Connect should be in the client packets list",
                Packets.clientPackets.containsKey(Protocol.Client.CONNECT));
        assertTrue("Decline Invite should be in the client packets list",
                Packets.clientPackets.containsKey(Protocol.Client.DECLINE_INVITE));
        assertTrue("Error should be in the client packets list",
                Packets.clientPackets.containsKey(Protocol.Client.ERROR));
        assertTrue("Invite should be in the client packets list",
                Packets.clientPackets.containsKey(Protocol.Client.INVITE));
        assertTrue("Move should be in the client packets list",
                Packets.clientPackets.containsKey(Protocol.Client.MOVE));
        assertTrue("Ping should be in the client packets list",
                Packets.clientPackets.containsKey(Protocol.Client.PING));
        assertTrue("Quit should be in the client packets list",
                Packets.clientPackets.containsKey(Protocol.Client.QUIT));
        assertTrue("Request Board should be in the client packets list",
                Packets.clientPackets.containsKey(Protocol.Client.REQUEST_BOARD));
        assertTrue("Request Leaderboard should be in the client packets list",
                Packets.clientPackets.containsKey(Protocol.Client.REQUEST_LEADERBOARD));
        assertTrue("Request Lobby should be in the client packets list",
                Packets.clientPackets.containsKey(Protocol.Client.REQUEST_LOBBY));
    }

    public void testReadServerPacket() throws Exception {
        Connection c = new Connection(null, Side.CLIENT);
        assertTrue("Packet '" + 
                Protocol.Server.ACCEPT_CONNECT + 
                "' should return Connect Ok packet", 
                Packets.readServerPacket(c, 
                        Protocol.Server.ACCEPT_CONNECT) instanceof 
                        ServerPacket.AcceptConnectPacket);
        assertTrue("Packet '" +
                        Protocol.Server.BOARD + " 1 1 0" +
                        "' should return Board packet",
                Packets.readServerPacket(c,
                        Protocol.Server.BOARD + " 1 1 0") instanceof
                        ServerPacket.BoardPacket);
        assertTrue("Packet '" +
                        Protocol.Server.CHAT + " m Hello" +
                        "' should return Chat packet",
                Packets.readServerPacket(c,
                        Protocol.Server.CHAT + " m Hello") instanceof
                        ServerPacket.ChatPacket);
        assertTrue("Packet '" +
                        Protocol.Server.DECLINE_INVITE + " m" +
                        "' should return Decline Invite packet",
                Packets.readServerPacket(c,
                        Protocol.Server.DECLINE_INVITE + " m") instanceof
                        ServerPacket.DeclineInvitePacket);
        assertTrue("Packet '" +
                        Protocol.Server.ERROR + " CONNECT hi" +
                        "' should return Error packet",
                Packets.readServerPacket(c,
                        Protocol.Server.ERROR + " CONNECT hi") instanceof
                        ServerPacket.ErrorPacket);
        assertTrue("Packet '" +
                        Protocol.Server.INVITE + " m 7 6" +
                        "' should return Invite packet",
                Packets.readServerPacket(c,
                        Protocol.Server.INVITE + " m 7 6") instanceof
                        ServerPacket.InvitePacket);
        assertTrue("Packet '" +
                        Protocol.Server.LEADERBOARD + " m 1 1 3 1" +
                        "' should return Leaderboard packet",
                Packets.readServerPacket(c,
                        Protocol.Server.LEADERBOARD + " m 1 1 3 1") instanceof
                        ServerPacket.LeaderBoardPacket);
        assertTrue("Packet '" +
                        Protocol.Server.LOBBY + " m n" +
                        "' should return Lobby packet",
                Packets.readServerPacket(c,
                        Protocol.Server.LOBBY + " m n") instanceof
                        ServerPacket.LobbyPacket);
        assertTrue("Packet '" +
                        Protocol.Server.MOVE_OK + " 1 1 m" +
                        "' should return Move OK packet",
                Packets.readServerPacket(c,
                        Protocol.Server.MOVE_OK + " 1 1 m") instanceof
                        ServerPacket.MoveOkPacket);
        assertTrue("Packet '" +
                        Protocol.Server.PONG +
                        "' should return Pong packet",
                Packets.readServerPacket(c,
                        Protocol.Server.PONG) instanceof
                        ServerPacket.PongPacket);
        assertTrue("Packet '" +
                        Protocol.Server.REQUEST_MOVE +
                        "' should return Request Move packet",
                Packets.readServerPacket(c,
                        Protocol.Server.REQUEST_MOVE) instanceof
                        ServerPacket.RequestMovePacket);
    }

    public void testReadClientPacket() throws Exception {
        Connection c = new Connection(null, Side.SERVER);

        assertTrue("Packet '" +
                        Protocol.Client.ACCEPT_INVITE + " m" +
                        "' should return Accept Invite packet",
                Packets.readClientPacket(c,
                        Protocol.Client.ACCEPT_INVITE + " m") instanceof
                        ClientPacket.AcceptInvitePacket);
        assertTrue("Packet '" +
                        Protocol.Client.CHAT + " m Hello" +
                        "' should return Chat packet",
                Packets.readClientPacket(c,
                        Protocol.Client.CHAT + " m Hello") instanceof
                        ClientPacket.ChatPacket);
        assertTrue("Packet '" +
                        Protocol.Client.CONNECT + " m" +
                        "' should return Connect packet",
                Packets.readClientPacket(c,
                        Protocol.Client.CONNECT + " m") instanceof
                        ClientPacket.ConnectPacket);
        assertTrue("Packet '" +
                        Protocol.Client.DECLINE_INVITE + " m" +
                        "' should return Decline Invite packet",
                Packets.readClientPacket(c,
                        Protocol.Client.DECLINE_INVITE + " m") instanceof
                        ClientPacket.DeclineInvitePacket);
        assertTrue("Packet '" +
                        Protocol.Client.ERROR + " ERROR m" +
                        "' should return Error packet",
                Packets.readClientPacket(c,
                        Protocol.Client.ERROR + " ERROR m") instanceof
                        ClientPacket.ErrorPacket);
        assertTrue("Packet '" +
                        Protocol.Client.INVITE + " m" +
                        "' should return Invite packet",
                Packets.readClientPacket(c,
                        Protocol.Client.INVITE + " m") instanceof
                        ClientPacket.InvitePacket);
        assertTrue("Packet '" +
                        Protocol.Client.MOVE + " 1" +
                        "' should return Move packet",
                Packets.readClientPacket(c,
                        Protocol.Client.MOVE + " 1") instanceof
                        ClientPacket.MovePacket);
        assertTrue("Packet '" +
                        Protocol.Client.PING +
                        "' should return Ping packet",
                Packets.readClientPacket(c,
                        Protocol.Client.PING) instanceof
                        ClientPacket.PingPacket);
        assertTrue("Packet '" +
                        Protocol.Client.QUIT + " Leave" +
                        "' should return Quit packet",
                Packets.readClientPacket(c,
                        Protocol.Client.QUIT + " Leave") instanceof
                        ClientPacket.QuitPacket);
        assertTrue("Packet '" +
                        Protocol.Client.REQUEST_BOARD +
                        "' should return Request Board packet",
                Packets.readClientPacket(c,
                        Protocol.Client.REQUEST_BOARD) instanceof
                        ClientPacket.RequestBoardPacket);
        assertTrue("Packet '" +
                        Protocol.Client.REQUEST_LEADERBOARD +
                        "' should return Request Leaderboard packet",
                Packets.readClientPacket(c,
                        Protocol.Client.REQUEST_LEADERBOARD) instanceof
                        ClientPacket.RequestLeaderboardPacket);
        assertTrue("Packet '" +
                        Protocol.Client.REQUEST_LOBBY +
                        "' should return Request Lobby packet",
                Packets.readClientPacket(c,
                        Protocol.Client.REQUEST_LOBBY) instanceof
                        ClientPacket.RequestLobbyPacket);
    }
}