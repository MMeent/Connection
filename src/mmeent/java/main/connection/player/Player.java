package mmeent.java.main.connection.player;

import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Move;

/**
 * Created by Lars on 20-1-2015.
 */
public interface Player {
    public Move getMove(int turn);
    public String getName();
    public byte getId();
    public void setId(byte id);
    public Game getGame();
    public void setGame(Game game);
}
