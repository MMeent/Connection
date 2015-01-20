package mmeent.java.main.connection.player;

import com.sun.istack.internal.Nullable;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Move;
import mmeent.java.main.connection.player.ai.AI;

/**
 * Created by Matthias on 20/12/2014.
 */
public class ComputerPlayer implements Player, AI {
    private String name;
    private Game game;
    private byte id;

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
        return null;
    }

    @Override
    public Move determineNextMove() {
        return null;
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
}
