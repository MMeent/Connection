package mmeent.java.main.connection.player;

import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Move;

import java.util.List;
import java.util.Random;

/**
 * Created by Matthias on 20/12/2014.
 */

public class ComputerPlayerRandom implements Player{
    private String name;
    private byte id = 0;
    private Game game;

    public ComputerPlayerRandom(String name, byte id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public Move getMove(int turn) {
        Random randomGenerator = new Random();
        List<Integer> rows = game.getBoard().availableRows();
        short index = (short) randomGenerator.nextInt(rows.size());
        return new Move(this,index,turn);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public byte getId() {
        return id;
    }

    @Override
    public void setId(byte id) {

    }

    @Override
    public Game getGame() {
        return game;
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
