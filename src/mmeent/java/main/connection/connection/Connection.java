package mmeent.java.main.connection.connection;

import mmeent.java.main.connection.Protocol;

import java.io.*;
import java.net.Socket;

/**
 * Created by Matthias on 14/01/2015.
 */
public class Connection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private StringBuilder privBuffer;

    public Connection(Socket socket) throws IOException{
        this.socket = socket
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream());
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

    public synchronized void clearBuffer(){
        this.privBuffer = new StringBuilder();
    }

    public synchronized void sendCharSequence(CharSequence chars){
        if(chars.charAt(chars.length() - 1) == Protocol.Settings.PACKET_END) this.out.append(chars);
    }

    public void send(Packet packet){
        packet.write(this);
    }
}
