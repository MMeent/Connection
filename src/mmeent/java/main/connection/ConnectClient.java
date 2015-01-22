package mmeent.java.main.connection;

import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.connection.Packet;
import mmeent.java.main.connection.exception.ConnectFourException;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.render.Renderer;
import mmeent.java.main.connection.render.TextBoardRenderer;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Matthias on 20/12/2014.
 */
public class ConnectClient {
    private String username;
    private Renderer renderer;
    private Boolean debug;
    private static ConnectClient get;
    public static Connection connection = null;
    public static boolean isClient = false;
    public static LinkedBlockingQueue<Packet> packets = new LinkedBlockingQueue<Packet>();

    public ConnectClient(String username, Renderer renderer, Boolean debug){
        this.username = username;
        this.renderer = renderer;
        this.debug = debug;

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

    public Renderer getRenderer(){
        return this.renderer;
    }

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

    public static ConnectClient get(){
        return ConnectClient.get;
    }
}
