package mmeent.java.main.connection;

import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.player.Player;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Matthias on 20/01/2015.
 */
public class ConnectServer {
    public static boolean isServer = false;
    public static ConnectServer server = null;
    private HashMap<String, Player> players = new HashMap<String, Player>();
    private HashMap<Player, Connection> connections = new HashMap<Player, Connection>();
    private BlockingQueue<Packet> packets = new LinkedBlockingDeque<Packet>();

    public ConnectServer(int port){
        ConnectServer.server = this;

    }

    public static void main(String[] args){
        ConnectServer.isServer = true;
        int port = Protocol.Settings.DEFAULT_PORT;
        new ConnectServer(port);
    }

    public Player getPlayer(String name){
        return this.players.get(name);
    }

    public Connection getConnection(Player player){
        return this.connections.get(player);
    }

    public Connection getConnection(String name){
        return this.getConnection(this.players.get(name));
    }
}
