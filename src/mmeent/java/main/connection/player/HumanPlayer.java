package mmeent.java.main.connection.player;

import com.sun.istack.internal.Nullable;
import mmeent.java.main.connection.game.Game;

import mmeent.java.main.connection.game.Move;

/**
 * Created by Matthias on 20/12/2014.
 */
public class HumanPlayer implements Player{
    private String name;
    private byte id;
    private Game game = null;

    public HumanPlayer(String name, byte id){
        this.name = name;
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public Move getMove(){
        return null;
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
