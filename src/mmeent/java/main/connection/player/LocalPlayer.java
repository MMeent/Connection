package mmeent.java.main.connection.player;

import com.sun.istack.internal.Nullable;
import mmeent.java.main.connection.game.Game;

import mmeent.java.main.connection.game.Move;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Matthias on 20/12/2014.
 */
public class LocalPlayer {
    private String name;
    private byte id = 0;
    private Game game = null;

    public static Map<String, LocalPlayer> players = new HashMap<String, LocalPlayer>();

    public LocalPlayer(String name){
        this.name = name;
    }

    public static LocalPlayer get(String name){
        return LocalPlayer.players.get(name);
    }

    public LocalPlayer(String name, byte id){
        this(name);
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
