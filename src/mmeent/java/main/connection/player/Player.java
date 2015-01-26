package mmeent.java.main.connection.player;

import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Move;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lars on 20-1-2015.
 * @author lars
 * @author mmeent
 *
 * This interface includes the registry of players. If an non-registered user is requested, it will create a new one.
 */
public interface Player {
    public static Map<String, Player> players = new HashMap<String, Player>();

    /**
     * Get or create a player. This player is directly registered into the system.
     * @param name the name of the player
     * @param id the id of the player
     * @return the player
     */
    public static Player get(String name, byte id) {
        if(LocalPlayer.players.containsKey(name)) {
            return LocalPlayer.players.get(name);
        }
        LocalPlayer p = new LocalPlayer(name, id);
        LocalPlayer.players.put(name, p);
        return p;
    }

    /**
     * Get a player by just the name. This function is preferred, since the ID is not checked at any of the functions.
     * @param name
     * @return
     */
    public static Player get(String name){
        return Player.get(name, (byte) 0);
    }

    /**
     * Get the next move that the player will make.
     * @param turn the turn they are in
     * @return the move the player will make
     */
    public Move getMove(int turn);

    /**
     * Get the name of the player
     * @return the name of the player
     */
    public String getName();

    /**
     * Get the ID of the player
     * @return the ID of the player
     */
    public byte getId();

    /**
     * Set the id of the player
     * @param id the id that will be given to the player
     */
    public void setId(byte id);

    /**
     * Get the game the player is currently playing
     * @return the game the player is playing
     */
    public Game getGame();

    /**
     * Set the game the player is playing
     * @param game the game the player will be playing
     */
    public void setGame(Game game);

    /**
     * Get the connection of the player
     * @return the player's connection
     */
    public Connection getConnection();
}
