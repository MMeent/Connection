package mmeent.java.main.connection.player;

import com.sun.istack.internal.Nullable;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.player.ai.AI;

import mmeent.java.main.connection.game.Move;

/**
 * Created by Matthias on 20/12/2014.
 */
public class ComputerPlayer extends Player {
    private AI ai;

    public ComputerPlayer(String name, byte id){
        this(name, id, null);
    }

    public ComputerPlayer(String name, byte id, AI ai){
        super(name, id);
        this.ai = ai;
    }
}
