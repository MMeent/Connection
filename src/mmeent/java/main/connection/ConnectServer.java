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
 * @author mmeent
 */
public class ConnectServer {
    public static boolean isServer = false;
    public static boolean debug = false;
    public static ConnectServer server = null;
    public static LinkedBlockingQueue<Packet> packets = new LinkedBlockingQueue<Packet>();

    public final Chat chat = new Chat();

    private ServerSocket socket;
    private HashMap<String, Player> players = new HashMap<String, Player>();
    public final HashMap<Player, Connection> playerconnections = new HashMap<Player, Connection>();
    public final List<Connection> connections = new ArrayList<Connection>();

    /**
     * Default constructor for <code>Connectserver</code>.
     * @param port the port the server is li stening to
     * @param argDebug whether to get debug or not
     * @throws IOException throws exception when incorrect format is used.
     */
    public ConnectServer(int port, boolean argDebug) throws IOException {
        ConnectServer.isServer = true;
        ConnectServer.server = this;
        ConnectServer.debug = argDebug;

        Packets.registerServerPackets();
        Packets.registerClientPackets();

        this.socket = new ServerSocket(port);

        Thread accept = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Socket s = socket.accept();
                        ConnectServer.server.connections.add(new Connection(s, Side.SERVER));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        accept.setDaemon(true);
        accept.start();

        Thread handling = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Packet p = ConnectServer.packets.take();
                        p.onReceive();
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.out);
                    }
                }
            }
        };


        handling.setDaemon(true);
        handling.start();
    }

    public void run() {
        boolean stopping = false;
        Scanner s = new Scanner(System.in);
        while (!stopping) {
            System.out.println("Input your thoughts here: ");
            String inp = s.nextLine();
            if (inp.toUpperCase().startsWith("STOP")) {
                stopping = true;
            } else {
                this.notifyAll(new ServerPacket.ChatPacket(null, Player.get("Server"), inp));
            }
        }
    }

    /**
     * Start the ConnectServer.
     * @param args -d for debug
     */
    public static void main(String[] args) {
        ConnectClient.debug = false;
        ConnectServer.isServer = true;
        for (String arg: args) {
            if (arg.equals("-d")) {
                debug = true;
            }
        }
        System.out.println("Accept connections on port: ");
        Scanner input = new Scanner(System.in);
        int port = input.hasNextInt() ? input.nextInt() : Protocol.Settings.DEFAULT_PORT;
        ConnectServer s;
        try {
            new ConnectServer(port, debug).run();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    /**
     * Get the player with name name.
     * @param name the name of the player that has to be returned
     * @return player with name name
     */
    public Player getPlayer(String name) {
        return this.players.get(name);
    }

    /**
     * Get the connection of Player player.
     * @param player the player of whom the connection will be returned
     * @return the connection of player
     */
    public Connection getConnection(Player player) {
        return this.playerconnections.get(player);
    }

    /**
     * Get the connection of the player with name name.
     * @param name the name of the player of whom you want the connection
     * @return the connection of the player with name name
     */
    public Connection getConnection(String name) {
        return this.getConnection(this.players.get(name));
    }

    /**
     * Add a player with connection to the registries.
     * @param player the player that will be added
     * @param connection the connection of that player
     */
    public void addPlayerConnection(Player player, Connection connection) {
        if (!(this.players.containsKey(player.getName()))) {
            this.players.put(player.getName(), player);
        }
        if (!(this.playerconnections.containsKey(player))) {
            this.playerconnections.put(player, connection);
        }
    }

    /**
     * Remove player player from the registries.
     * @param player the player to be removed
     */
    public void removePlayer(Player player) {
        this.playerconnections.remove(player);
        this.players.remove(player.getName());
        Invite.invites.remove(player);
        this.connections.remove(player.getConnection());
        this.notifyAll(new ServerPacket.LobbyPacket(null,
                (Player[]) this.players.values().toArray()));
    }

    /**
     * Notify all PLAYER_MAP with packet p.
     * @param packet the packet to be sent to all PLAYER_MAP
     */
    public void notifyAll(Packet packet) {
        for (Connection c: this.connections) {
            c.send(packet);
        }
    }

    /**
     * Get all PLAYER_MAP currently online.
     * @return all PLAYER_MAP online
     */
    public List<Player> getPlayers() {
        return new ArrayList<Player>(this.players.values());
    }

    /**
     * Get all the names of the PLAYER_MAP currently online.
     * @return a list of playernames
     */
    public List<String> getPlayernames() {
        return new ArrayList<String>(this.players.keySet());
    }
}
