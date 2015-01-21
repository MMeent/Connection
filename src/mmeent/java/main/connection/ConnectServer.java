package mmeent.java.main.connection;

import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.connection.Side;
import mmeent.java.main.connection.player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Matthias on 20/01/2015.
 */
public class ConnectServer {
    public static boolean isServer = false;
    public static ConnectServer server = null;
    private ServerSocket socket;
    private HashMap<String, Player> players = new HashMap<String, Player>();
    private HashMap<Player, Connection> playerconnections = new HashMap<Player, Connection>();
    private List<Connection> connections = new ArrayList<Connection>();
    public static LinkedBlockingQueue<Packet> packets = new LinkedBlockingQueue<Packet>();

    public ConnectServer(int port) throws IOException{
        ConnectServer.server = this;
        socket = new ServerSocket(port);

        Thread accept = new Thread() {
            public void run(){
                while(true){
                    try{
                        Socket s = socket.accept();
                        connections.add(new Connection(s, Side.SERVER));
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        accept.setDaemon(true);
        accept.start();

        Thread handling = new Thread(){
            public void run(){
                while(true){
                    try{
                        Packet p = ConnectServer.packets.take();
                        p.onReceive();
                    } catch (InterruptedException e){
                        e.printStackTrace(System.out);
                    }
                }
            }
        };


        handling.setDaemon(true);
        handling.start();
    }

    public static void main(String[] args){
        ConnectServer.isServer = true;
        int port = Protocol.Settings.DEFAULT_PORT;
        try{
            new ConnectServer(port);
        } catch (IOException e){
            e.printStackTrace(System.out);
        }
    }

    public Player getPlayer(String name){
        return this.players.get(name);
    }

    public Connection getConnection(Player player){
        return this.playerconnections.get(player);
    }

    public Connection getConnection(String name){
        return this.getConnection(this.players.get(name));
    }
}
