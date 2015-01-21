package mmeent.java.main.connection.player;

import com.sun.istack.internal.Nullable;
import mmeent.java.main.connection.ConnectClient;
import mmeent.java.main.connection.ConnectServer;
import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.game.Game;

import mmeent.java.main.connection.game.Move;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Matthias on 20/12/2014.
 */
public class LocalPlayer implements Player {
    private String name;
    private byte id = 0;
    private Game game = null;

    public static Map<String, LocalPlayer> players = new HashMap<String, LocalPlayer>();

    public LocalPlayer(String name){
        this.name = name;
    }

    public static LocalPlayer get(String name, byte id){
        if(LocalPlayer.players.containsKey(name)) return LocalPlayer.players.get(name);
        LocalPlayer p = new LocalPlayer(name, id);
        LocalPlayer.players.put(name, p);
        return p;
    }

    public static LocalPlayer get(String name){
        return LocalPlayer.get(name, (byte) 0);
    }

    public LocalPlayer(String name, byte id){
        this(name);
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public Move getMove(int turn){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Make a move: ");
        Scanner input = new Scanner(System.in);
        String in = input.next();
        short number = in.matches("[0-9]*") ? Short.valueOf(in) : -1;
        Move move = new Move(this,number,turn);
        if(move.isValid()) {
            return move;
        }
        else {
            System.out.println("Wrong choice!");
            return this.getMove(turn);
        }
    }

    public byte getId(){
        return this.id;
    }

    public void setId(byte id){
        this.id = id;
    }

    @Nullable
    public Game getGame(){
        return this.game;
    }

    public void setGame(Game game){
        this.game = game;
    }

    public Connection getConnection(){
        if(ConnectServer.isServer) return ConnectServer.getConnection(this);
        if(ConnectClient.isClient) return ConnectClient.connection;
    }
}
