package mmeent.java.main.connection.connection;

import mmeent.java.main.connection.ConnectClient;
import mmeent.java.main.connection.ConnectServer;
import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.player.Player;

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
    private Side side;
    private Connection connection = this;
    private Player player = null;

    public Connection(Socket socket, Side side) throws IOException{
        this.socket = socket;
        this.side = side;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream());

        Thread read = (this.side == Side.SERVER) ? new Thread(){
            public void run(){
                while(true){
                    try{
                        String msg = in.readLine();
                        ConnectServer.packets.put(Packets.readClientPacket(connection, msg));
                    } catch (IOException e){
                        e.printStackTrace(System.out);
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                }
            }
        }: new Thread(){
            public void run(){
                while(true){
                    try{
                        String msg = in.readLine();
                        ConnectClient.packets.put(Packets.readServerPacket(connection, msg));
                    } catch (IOException e){
                        e.printStackTrace(System.out);
                    } catch (Exception e){
                        e.printStackTrace(System.out);
                    }
                }
            }
        };

        read.setDaemon(true);
        read.start();

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

    public void setPlayer(Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }
}
