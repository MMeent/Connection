package mmeent.java.main.connection;

import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.connection.Packets;
import mmeent.java.main.connection.connection.Side;
import mmeent.java.main.connection.connection.server.ServerPacket;
import mmeent.java.main.connection.game.Invite;
import mmeent.java.main.connection.player.Player;
import mmeent.java.main.connection.server.Chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Matthias on 20/01/2015.
 */
public class ConnectServer {
    public static boolean isServer = false;
    public static boolean debug = false;
    public static ConnectServer server = null;
    public static LinkedBlockingQueue<Packet> packets = new LinkedBlockingQueue<Packet>();

    public final Chat chat = new Chat();

    private ServerSocket socket;
    private HashMap<String, Player> players = new HashMap<String, Player>();
    private HashMap<Player, Connection> playerconnections = new HashMap<Player, Connection>();
    private List<Connection> connections = new ArrayList<Connection>();

    public ConnectServer(int port, boolean debug) throws IOException{
        ConnectServer.server = this;
        ConnectServer.debug = debug;

        Packets.registerServerPackets();
        Packets.registerClientPackets();

        this.socket = new ServerSocket(port);

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
        boolean debug = false;
        ConnectServer.isServer = true;
        for(String arg: args) {
            if(arg.equals("-d")) debug = true;
        }
        int port = Protocol.Settings.DEFAULT_PORT;
        Scanner input = new Scanner(System.in);
        String next = input.next();
        int i = Integer.getInteger(next, -1);
        port = (i > 0) ? i : port;
        try{
            new ConnectServer(port, debug);
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

    public void addPlayerConnection(Player player, Connection connection){
        if(!this.playerconnections.containsKey(player)) this.playerconnections.put(player, connection);
        if(!this.players.containsKey(player.getName())) this.players.put(player.getName(), player);
    }

    public void removePlayer(Player player){
        this.playerconnections.remove(player);
        this.players.remove(player.getName());
        Invite.invites.remove(player);
        this.connections.remove(player.getConnection());
        this.notifyAll(new ServerPacket.LobbyPacket(null, (Player[]) this.players.values().toArray()));
    }

    public void notifyAll(Packet packet){
        for(Connection c: this.connections){
            c.send(packet);
        }
    }

    public List<Player> getPlayers(){
        return new ArrayList<Player>(this.players.values());
    }

    public List<String> getPlayernames(){
        return new ArrayList<String>(this.players.keySet());
    }
}
