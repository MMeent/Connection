package mmeent.java.main.connection.player;

import com.sun.istack.internal.Nullable;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.player.ai.AI;

import mmeent.java.main.connection.game.Move;

/**
 * Created by Matthias on 20/12/2014.
 */
public class ComputerPlayer implements Player {
    private String name;
    private byte id;
    private AI ai;
    private Game game;

    public ComputerPlayer(String name, byte id){
        this(name, id, null);
    }

    public ComputerPlayer(String name, byte id, AI ai){
        this.name = name;
        this.id = id;
        this.ai = ai;
    }

    public Move getMove(){
        return null;
    }

    public String getName(){
        return this.name;
    }

    public byte getId(){
        return this.id;
    }

    @Nullable
    public Game getGame(){
        return this.game;
    }

    public void setGame(Game game){
        this.game = game;
    }
}
