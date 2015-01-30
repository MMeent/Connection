package mmeent.java.main.connection;

import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.connection.Packets;
import mmeent.java.main.connection.connection.Side;
import mmeent.java.main.connection.connection.client.ClientPacket;
import mmeent.java.main.connection.player.ComputerPlayerRandom;
import mmeent.java.main.connection.player.ComputerPlayerSmart;
import mmeent.java.main.connection.player.Player;
import mmeent.java.main.connection.render.Renderer;
import mmeent.java.main.connection.render.TextBoardRenderer;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Matthias on 20/12/2014.
 * @author mmeent
 */
public class ConnectClient {
    private Player user;
    private Renderer renderer;
    public static Boolean debug = false;
    private Connection connection;
    public boolean shutdown;
    private static ConnectClient get;
    public static boolean isClient = false;
    public static LinkedBlockingQueue<Packet> packets = new LinkedBlockingQueue<Packet>();

    /**
     * Default constructor of ConnectClient.
     * @param argUsername the username of the player that will be playing
     * @param argRenderer the renderer that will be used to render the different parts of client
     * @param argDebug whether there has to be debug output or not
     */
    public ConnectClient(String argUsername, Renderer argRenderer , Boolean argDebug) {
        this.user = Player.get(argUsername);
        this.renderer = argRenderer;
        ConnectClient.debug = argDebug;

        Packets.registerServerPackets();
        Packets.registerClientPackets();

        Scanner in = new Scanner(System.in);
        System.out.println("IP to connect to: ");
        String iP = in.nextLine();
        System.out.println("Port to connect to: ");
        int port = in.hasNextInt() ?
                Integer.parseInt(in.nextLine()) : Protocol.Settings.DEFAULT_PORT;
        System.out.println("Connecting to " + iP + " on port " + port);
        Socket s;
        try {
            s = new Socket(iP, port);
            this.connection = new Connection(s, Side.CLIENT);

            Thread packetHandler = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            Packet p = packets.take();
                            p.onReceive();
                        } catch (InterruptedException e) {
                            e.printStackTrace(System.out);
                        }
                    }
                }
            };

            packetHandler.setDaemon(true);
            packetHandler.start();

            String[] options = {Protocol.Features.CHAT,
                                Protocol.Features.CUSTOM_BOARD_SIZE,
                                Protocol.Features.LEADERBOARD};
            this.connection.send(new ClientPacket.ConnectPacket(this.connection,
                                                                argUsername,
                                                                options));

        } catch (IOException e) {
            e.printStackTrace(System.out);
            this.shutdown = true;
            this.renderer.addErrorMessage("", e.getStackTrace().toString());
        }
    }

    public void run() {
        Scanner in = new Scanner(System.in);
        while (!this.shutdown) {
            String s = in.nextLine();
            this.shutdown = s.toUpperCase().startsWith("QUIT");
            String[] args = s.split(" ");
            switch (args[0].toUpperCase()) {
                case "INVITE": this.connection.send(args.length > 2
                                    ? new ClientPacket.InvitePacket(this.connection, args[1],
                                                                  Short.parseShort(args[2]),
                                                                  Short.parseShort(args[3]))
                                    : new ClientPacket.InvitePacket(this.connection, args[1]));
                    break;
                case "ACCEPT" :
                    this.connection.send(new ClientPacket.AcceptInvitePacket(this.connection,
                            args[1]));
                    break;
                case "DECLINE":
                    this.connection.send(new ClientPacket.DeclineInvitePacket(this.connection,
                            args[1]));
                    break;
                case "PING":
                    this.connection.send(new ClientPacket.PingPacket(this.connection));
                    break;
                case "QUIT":
                    this.connection.send(new ClientPacket.QuitPacket(this.connection,
                            s.replaceFirst(args[0], "")));
                    break;
                case "MOVE":
                    System.out.println(Short.parseShort(args[1]));
                    this.connection.send(new ClientPacket.MovePacket(this.connection,
                            Short.parseShort(args[1])));
                    break;
                case "HINT":
                    ComputerPlayerRandom p = new ComputerPlayerRandom(this.user.getName(),
                            (byte) 0);
                    p.setGame(Player.get(this.user.getName()).getGame());
                    System.out.println("HINT: you can move into column " + 
                            p.getMove(p.getGame().getTurn()).getColumn());
                    break;
                case "INTELLIGENCE":
                    ComputerPlayerSmart.searchDepth = Math.max(0, Math.min(15, 
                            Integer.parseInt(args[2])));
                    break;
                case "AI": // Artificial Intelligence
                    this.user = new ComputerPlayerSmart(this.user.getName(), this.user.getId());
                    break;
                case "HI": // Human Intelligence
                    this.user = Player.get(this.user.getName());
                    break;
                case "HELP":
                    this.renderer.addMessage("The following commands are available: ");
                    this.renderer.addMessage("Accept, Ai, Decline, Help, Hi, Hint, " + 
                            "Intelligence, Invite, Move, Ping and Quit");
                    break;
                default:
                    this.connection.send(new ClientPacket.ChatPacket(this.connection, s));
                    break;
            }
        }
        this.connection.send(new ClientPacket.QuitPacket(this.connection, "Leave"));
    }

    /**
     * Get the renderer used in the ConnectClient.
     * @return the renderer used
     */
    public Renderer getRenderer() {
        return this.renderer;
    }

    /**
     * Start the ConnectClient.
     * @param args -u 'username' for username, -d for debug, -g for GUI
     */
    public static void main(String[] args) {
        String username = null;
        ConnectClient.debug = false;

        int offset = 0;
        for (int i = 0; i + offset < args.length; i++) {
            switch (args[i + offset]) {
                case "-u": username = args[i + offset + 1];
                    offset++;
                    break;
                case "-d": debug = true;
                    break;
            }

        }
        if (username == null) {
            Scanner s = new Scanner(System.in);
            System.out.println("Username: ");
            String un = s.next();
            username = un.length() > 15 ? un.substring(0, 14) : un;
        }
        ConnectClient.isClient = true;
        ConnectClient.get = new ConnectClient(username, new TextBoardRenderer(null), debug);
        ConnectClient.get.run();
    }

    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Get the ConnectClient.
     * @return the connect client
     */
    public static ConnectClient get() {
        return ConnectClient.get;
    }
}
