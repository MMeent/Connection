package mmeent.java.main.connection.player;

import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Move;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lars on 20-1-2015.
 */
public interface Player {
    public static Map<String, Player> players = new HashMap<String, Player>();

    public static Player get(String name) {
        if(LocalPlayer.players.containsKey(name)) {
            return LocalPlayer.players.get(name);
        }
        LocalPlayer p = new LocalPlayer(name, (byte) (players.size() + 1));
        LocalPlayer.players.put(name, p);
        return p;
    }

    public Move getMove(int turn);
    public String getName();
    public byte getId();
    public void setId(byte id);
    public Game getGame();
    public void setGame(Game game);
    public Connection getConnection();
}
