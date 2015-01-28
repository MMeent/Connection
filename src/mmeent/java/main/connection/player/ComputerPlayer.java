package mmeent.java.main.connection.player;

import com.sun.istack.internal.Nullable;
import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Move;

import java.util.List;
import java.util.Random;

/**
 * Created by Matthias on 20/12/2014.
 */

public class ComputerPlayer implements Player {
    /*@
        public_invariant id >= 0;
        public_invariant game != null;
     */

    private String name;
    private Game game;
    private byte id;

    /**
     * Constructor of <code>ComputerPlayer</code>
     * @param name Name of the <code>ComputerPlayer</code>
     * @param game <code>Game</code> of the <code>ComputerPlayer</code>
     * @param id Id of the <code>ComputerPlayer</code>
     */
    /*@
        ensures this.getId() = id && this.getName() == name; && this.getGame == game;
     */
    public ComputerPlayer(String name, Game game, byte id){
        this.id = id;
        this.name = name;
        this.game = game;
    }

    /**
     * Constructor of <code>ComputerPlayer</code>
     * @param name Name of the <code>ComputerPlayer</code>
     * @param game <code>Game</code> of the <code>ComputerPlayer</code>
     */
    public ComputerPlayer(String name, Game game){
        this(name, game, (byte) 0);
    }

    /**
     * Constructor of <code>ComputerPlayer</code>
     * @param name Name of the <code>ComputerPlayer</code>
     */
    public ComputerPlayer(String name){
        this(name, null, (byte) 0);
    }

    /**
     * Constructor of <code>ComputerPlayer</code>
     */
    public ComputerPlayer() {
        super();
    }

    /**
     * Function that asks the ComputerPlayer for a <code>Move</code>
     * @param turn the turn they are in
     * @return returns the next <code>Move</code> of the <code>ComputerPlayer</code>
     */
    @Override
    public Move getMove(int turn) {
        Random randomGenerator = new Random();
        List<Short> availableRows = game.getBoard().availableCols();
        int index = randomGenerator.nextInt(availableRows.size());
        int choice= availableRows.get(index);
        return new Move(this,(short) choice,turn);
    }

    /**
     * Get the name of the player
     *
     * @return the name of the player
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Get the ID of the player
     *
     * @return the ID of the player
     */
    @Override
    public byte getId() {
        return this.id;
    }

    /**
     * Set the id of the player
     *
     * @param id the id that will be given to the player
     */
    @Override
    public void setId(byte id) {
        this.id = id;
    }

    /**
     * Get the game the player is currently playing
     *
     * @return the game the player is playing
     */
    @Override
    public Game getGame() {
        return this.game;
    }

    /**
     * Set the game the player is playing
     *
     * @param game the game the player will be playing
     */
    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Get the connection of the player
     *
     * @return the player's connection
     */
    @Override
    public Connection getConnection() {
        return null;
    }

}
