package mmeent.java.main.connection.player.ai;

import mmeent.java.main.connection.game.Move;

/**
 * Created by Matthias on 20/12/2014.
 */
public interface AI {
    public Move determineNextMove();
    public String getName();
}
