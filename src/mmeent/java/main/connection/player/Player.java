package mmeent.java.main.connection.player;

import com.sun.istack.internal.Nullable;

import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Move;

/**
 * Created by Matthias on 20/12/2014.
 */
public interface Player {
    public Move getMove();
    public String getName();
    public byte getId();

    public void setGame(Game game);
    @Nullable public Game getGame();
}
