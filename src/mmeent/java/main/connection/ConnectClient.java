package mmeent.java.main.connection;

import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.connection.Packets;
import mmeent.java.main.connection.connection.Side;
import mmeent.java.main.connection.connection.client.ClientPacket;
import mmeent.java.main.connection.exception.ConnectFourException;
import mmeent.java.main.connection.game.Game;
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
    private String username;
    private Renderer renderer;
    private Boolean debug;
    public final Connection connection;
    public boolean shutdown;
    private static ConnectClient get;
    public static boolean isClient = false;
    public static LinkedBlockingQueue<Packet> packets = new LinkedBlockingQueue<Packet>();

    /**
     * Default constructor of ConnectClient
     * @param username the username of the player that will be playing
     * @param renderer the renderer that will be used to render the different parts of client
     * @param debug whether there has to be debug output or not
     */
    public ConnectClient(String username, Renderer renderer, Boolean debug){
        this.username = username;
        this.renderer = renderer;
        this.debug = debug;

        Packets.registerServerPackets();
        Packets.registerClientPackets();

        Scanner in = new Scanner(System.in);
        System.out.println("IP to connect to: ");
        String IP = in.nextLine();
        System.out.println("Port to connect to: ");
        int port = in.hasNextInt() ? Integer.parseInt(in.nextLine()) : Protocol.Settings.DEFAULT_PORT;
        System.out.println("Connecting to " + IP + " on port " + port);
        Socket s = null;
        try {
            s = new Socket(IP, port);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        this.connection = new Connection(s, Side.CLIENT);

        Thread packetHandler = new Thread(){
            public void run(){
                while(true){
                    try{
                        Packet p = packets.take();
                        p.onReceive();
                    } catch (InterruptedException e){
                        e.printStackTrace(System.out);
                    }
                }
            }
        };

        packetHandler.setDaemon(true);
        packetHandler.start();

        String[] options = {Protocol.Features.CHAT, Protocol.Features.CUSTOM_BOARD_SIZE, Protocol.Features.LEADERBOARD};
        this.connection.send(new ClientPacket.ConnectPacket(this.connection, username, options));
    }

    public void run(){
        Scanner in = new Scanner(System.in);
        while(!this.shutdown){
            String s = in.nextLine();
            this.shutdown = s.toUpperCase().startsWith("QUIT");
            String[] args = s.split(" ");
            switch(args[0].toUpperCase()) {
                case "INVITE": this.connection.send(args.length > 2
                        ? new ClientPacket.InvitePacket(this.connection, args[1], Short.parseShort(args[2]), Short.parseShort(args[3]))
                        : new ClientPacket.InvitePacket(this.connection, args[1]));break;
                case "ACCEPT": this.connection.send(new ClientPacket.AcceptInvitePacket(this.connection, args[1]));break;
                case "DECLINE": this.connection.send(new ClientPacket.DeclineInvitePacket(this.connection, args[1]));break;
                case "PING": this.connection.send(new ClientPacket.PingPacket(this.connection)); break;
                case "QUIT": this.connection.send(new ClientPacket.QuitPacket(this.connection, s.replaceFirst(args[0], "")));break;
                case "MOVE": System.out.println(Short.parseShort(args[1]));
                    this.connection.send(new ClientPacket.MovePacket(this.connection, Short.parseShort(args[1])));break;
                default: this.connection.send(new ClientPacket.ChatPacket(this.connection, s));break;
            }
        }
        this.connection.send(new ClientPacket.QuitPacket(this.connection, "Leave"));
    }

    /**
     * Get the renderer used in the ConnectClient
     * @return the renderer used
     */
    public Renderer getRenderer(){
        return this.renderer;
    }

    /**
     * Start the ConnectClient
     * @param args -u 'username' for username, -d for debug, -g for GUI
     */
    public static void main(String[] args){
        String username = null;
        Boolean debug = false;
        Boolean graphical = false;
        for(int i = 0; i < args.length; i++){
            switch(args[i]){
                case "-u": username = args[++i];
                case "-d": debug = true;
                case "-g": graphical = true;
            }
        }
        if(username == null){
            Scanner s = new Scanner(System.in);
            System.out.println("Username: ");
            String un = s.next();
            username = un.length() > 15 ? un.substring(0, 14) : un;
        }
        ConnectClient.isClient = true;
        ConnectClient.get = new ConnectClient(username, new TextBoardRenderer(null), debug);
        ConnectClient.get.run();
    }

    /**
     * Get the ConnectClient
     * @return the connect client
     */
    public static ConnectClient get(){
        return ConnectClient.get;
    }
}
