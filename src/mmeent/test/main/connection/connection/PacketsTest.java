package mmeent.test.main.connection.connection;

import junit.framework.TestCase;
import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.connection.Packets;

public class PacketsTest extends TestCase {
    
    public void testRegisterServerPackets() throws Exception {
        assertEquals("Client packet list must be clear when started", 
                Packets.clientPackets.size(), 0);
        Packets.registerClientPackets();
        assertEquals("Client packet list must contain 12 packets after registering",
                Packets.clientPackets.size(), 12);
        assertEquals(Packets.clientPackets.containsKey(Protocol.Client.ACCEPT_INVITE), true);
        assertEquals(Packets.clientPackets.containsKey(Protocol.Client.CHAT), true);
        assertEquals(Packets.clientPackets.containsKey(Protocol.Client.CONNECT), true);
        assertEquals(Packets.clientPackets.containsKey(Protocol.Client.DECLINE_INVITE), true);
        assertEquals(Packets.clientPackets.containsKey(Protocol.Client.ERROR), true);
        assertEquals(Packets.clientPackets.containsKey(Protocol.Client.INVITE), true);
        assertEquals(Packets.clientPackets.containsKey(Protocol.Client.MOVE), true);
        assertEquals(Packets.clientPackets.containsKey(Protocol.Client.PING), true);
        assertEquals(Packets.clientPackets.containsKey(Protocol.Client.QUIT), true);
        assertEquals(Packets.clientPackets.containsKey(Protocol.Client.REQUEST_BOARD), true);
        assertEquals(Packets.clientPackets.containsKey(Protocol.Client.REQUEST_LEADERBOARD), true);
        assertEquals(Packets.clientPackets.containsKey(Protocol.Client.REQUEST_LOBBY), true);
    }

    public void testRegisterClientPackets() throws Exception {

    }

    public void testReadServerPacket() throws Exception {

    }

    public void testReadClientPacket() throws Exception {

    }
}