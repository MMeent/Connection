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
    private String name;
    private Game game;
    private byte id;

    /**
     *
     * @param name
     * @param game
     * @param id
     */
    public ComputerPlayer(String name, Game game, byte id){
        this.id = id;
        this.name = name;
        this.game = game;
    }

    public ComputerPlayer(String name, Game game){
        this(name, game, (byte) 0);
    }

    public ComputerPlayer(String name){
        this(name, null, (byte) 0);
    }

    @Override
    public Move getMove(int turn) {
        Random randomGenerator = new Random();
        List<Short> availableRows = game.getBoard().availableCols();
        int index = randomGenerator.nextInt(availableRows.size());
        int choice= availableRows.get(index);
        return new Move(this,(short) choice,turn);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public byte getId() {
        return this.id;
    }

    @Override
    public void setId(byte id){
        this.id = id;
    }

    @Override
    @Nullable
    public Game getGame() {
        return this.game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
