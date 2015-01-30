package mmeent.test.main.connection.connection;

import junit.framework.TestCase;
import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Side;
import mmeent.java.main.connection.player.Player;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;

public class ConnectionTest extends TestCase {
    
    public Socket get() {
        try {
            return new Socket("Google.com", 80);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void testStartPacket() throws Exception {
        Connection c = new Connection(get(), Side.CLIENT);
        c.startPacket();
        Field f = Connection.class.getDeclaredField("privBuffer");
        f.setAccessible(true);
        StringBuilder b = (StringBuilder) f.get(c);
        assertEquals("When a packet is started the buffer should contain nothing",
                b.toString(), "");
    }

    public void testWritePartial() throws Exception {
        String text = "Text";
        Connection c = new Connection(get(), Side.CLIENT);
        c.startPacket();
        c.writePartial(text);
        Field f = Connection.class.getDeclaredField("privBuffer");
        f.setAccessible(true);
        StringBuilder b = (StringBuilder) f.get(c);
        assertEquals("When something is written to an clear packet it should only write the data",
                b.toString(), text);
        c.writePartial(text);
        assertEquals("When something is written to an packet containing text" + 
                " it should write a [space] too", b.toString(), text + " " + text);
    }

    public void testStopPacket() throws Exception {
        Connection c = new Connection(get(), Side.CLIENT);
        c.startPacket();
        c.stopPacket();
        Field f = Connection.class.getDeclaredField("privBuffer");
        f.setAccessible(true);
        StringBuilder b = (StringBuilder) f.get(c);
        assertTrue("When a packet is ended the buffer should end with " +
                        Protocol.Settings.PACKET_END,
                b.toString().endsWith(String.valueOf(Protocol.Settings.PACKET_END)));
    }

    public void testSendBuffer() throws Exception {

    }

    public void testClearBuffer() throws Exception {
        String text = "TEXT";
        Connection c = new Connection(get(), Side.CLIENT);
        c.startPacket();
        c.writePartial(text);
        c.clearBuffer();
        Field f = Connection.class.getDeclaredField("privBuffer");
        f.setAccessible(true);
        StringBuilder b = (StringBuilder) f.get(c);
        assertEquals("After clearbuffer the buffer should contain nothing", b.toString(), "");
    }

    public void testSendCharSequence() throws Exception {

    }

    public void testSend() throws Exception {

    }

    public void testSetClient() throws Exception {
        Player p = Player.get("Hi");
        Player q = Player.get("hello");
        Connection c = new Connection(get(), Side.CLIENT);
        c.setClient(p);
        assertEquals("The client of the connection should get set to the given player",
                p, c.getClient());
        c.setClient(q);
        assertEquals("The client of the connection should get set to the given player",
                q, c.getClient());
    }

    public void testGetClient() throws Exception {
        Player p = Player.get("Hi");
        Player q = Player.get("Hello");
        Connection c = new Connection(get(), Side.CLIENT);
        c.setClient(p);
        assertEquals("The client of the connection should get set to the given player",
                p, c.getClient());
        c.setClient(q);
        assertEquals("The client of the connection should get set to the given player",
                q, c.getClient());
    }

    public void testQuit() throws Exception {

    }
}