package mmeent.java.main.connection.connection;

import mmeent.java.main.connection.Protocol;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Matthias on 14/01/2015.
 */
public class Connection {
    private StringBuffer in;
    private StringWriter out;
    private StringBuilder privBuffer;

    public Connection(StringBuffer in, StringWriter out){
        this.in = in;
        this.out = out;
    }

    public synchronized void startPacket(){
        this.privBuffer = new StringBuilder();
    }

    public synchronized void writePartial(String string){
        this.privBuffer.append(Protocol.Settings.DELIMITER).append(string);
    }

    public synchronized void stopPacket(){
        this.privBuffer.append(Protocol.Settings.PACKET_END);
    }

    public synchronized void sendBuffer(){
        this.sendCharSequence(this.privBuffer);
    }

    public synchronized void sendCharSequence(CharSequence chars){
        if(chars.charAt(chars.length() - 1) == Protocol.Settings.PACKET_END) this.out.append(chars);
    }

    public void send(Packet packet){
        packet.write(this);
    }
}
