package mmeent.java.main.connection.player;

import mmeent.java.main.connection.player.ai.AI;

/**
 * Created by Matthias on 20/12/2014.
 */
public class ComputerPlayer extends LocalPlayer {
    private AI ai;

    public ComputerPlayer(String name, byte id){
        this(name, id, null);
    }

    public ComputerPlayer(String name, byte id, AI ai){
        super(name, id);
        this.ai = ai;
    }

    public ComputerPlayer(byte id, AI ai){
        this(ai.getName(), id);
    }
}
