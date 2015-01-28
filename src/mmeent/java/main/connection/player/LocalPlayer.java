package mmeent.java.main.connection.player;

import com.sun.istack.internal.Nullable;
import mmeent.java.main.connection.ConnectClient;
import mmeent.java.main.connection.ConnectServer;
import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Move;

import java.util.Scanner;

/**
 * Created by Matthias on 20/12/2014.
 * @author mmeent
 */
public class LocalPlayer implements Player {
    private String name;
    private byte id = 0;
    private Game game;

    public LocalPlayer(String name, byte id){
        this.name = name;
        this.id = id;
    }

    /**
     * Get the name of the player
     * @return the name of the player
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get the next move of the player
     * @param turn the turn they are in
     * @return the move the player will do
     */
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

    /**
     * Get the ID of the player
     * @return the id of the player
     */
    public byte getId(){
        return this.id;
    }

    /**
     * Set the ID of the player
     * @param id the id that will be given to the player
     */
    public void setId(byte id){
        this.id = id;
    }

    /**
     * Get the game the player is playing
     * @return the game the player is playing
     */
    public Game getGame(){
        return this.game;
    }

    /**
     * Set the game to let the player play that game
     * @param game the game the player will be playing
     */
    public void setGame(Game game){
        this.game = game;
    }

    /**
     * Get the connection of the player
     * @return the connection of the player
     */
    public Connection getConnection(){
        if(ConnectServer.isServer) return ConnectServer.server.getConnection(this);
        if(ConnectClient.isClient) return ConnectClient.get().connection;
        return null;
    }
}
