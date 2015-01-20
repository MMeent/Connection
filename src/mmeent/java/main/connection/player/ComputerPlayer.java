package mmeent.java.main.connection.player;

import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Move;
import mmeent.java.main.connection.player.ai.AI;

/**
 * Created by Matthias on 20/12/2014.
 */
public class ComputerPlayer implements Player, AI {

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
        return null;
    }

    @Override
    public byte getId() {
        return 0;
    }

    @Override
    public Game getGame() {
        return null;
    }

    @Override
    public void setGame(Game game) {

    }
}
