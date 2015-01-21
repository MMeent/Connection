package mmeent.java.main.connection;

import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.exception.ConnectFourException;
import mmeent.java.main.connection.render.Renderer;

/**
 * Created by Matthias on 20/12/2014.
 */
public class ConnectClient {
    private String username;
    private Renderer renderer;
    private Boolean debug;
    public static Connection connection = null;
    public static boolean isClient = false;

    public ConnectClient(String username, Renderer renderer, Boolean debug){
        this.username = username;
        this.renderer = renderer;
        this.debug = debug;
/**
        try{
            while(true){}
        } catch (ConnectFourException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
 */
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
        new ConnectClient(username, (Renderer) null, debug);
    }
}
