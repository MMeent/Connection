package mmeent.java.main.connection;

import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.connection.Packets;
import mmeent.java.main.connection.exception.ConnectFourException;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.render.Renderer;
import mmeent.java.main.connection.render.TextBoardRenderer;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Matthias on 20/12/2014.
 * @author mmeent
 */
public class ConnectClient {
    private String username;
    private Renderer renderer;
    private Boolean debug;
    private static ConnectClient get;
    public static Connection connection = null;
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
     * @param args -u <username> for username, -d for debug, -g for GUI
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
        ConnectClient.isClient = true;
        ConnectClient.get = new ConnectClient(username, new TextBoardRenderer(null), debug);
    }

    /**
     * Get the ConnectClient
     * @return the connect client
     */
    public static ConnectClient get(){
        return ConnectClient.get;
    }
}
