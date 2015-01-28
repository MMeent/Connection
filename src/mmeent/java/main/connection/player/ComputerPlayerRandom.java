package mmeent.java.main.connection.player;

import mmeent.java.main.connection.ConnectClient;
import mmeent.java.main.connection.ConnectServer;
import mmeent.java.main.connection.connection.Connection;
import mmeent.java.main.connection.game.Game;
import mmeent.java.main.connection.game.Move;

import java.util.List;
import java.util.Random;

/**
 * Created by Matthias on 20/12/2014.
 * @author mmeent
 */

public class ComputerPlayerRandom implements Player{
    private String name;
    private byte id;
    private Game game;

    /**
     * Constructor of <code>ComputerPlayerRandom</code>
     * @param name Name of the new <code>ComputerPlayerRandom</code>
     * @param id Id of the new<code>ComputerPlayerRandom</code>
     */
    public ComputerPlayerRandom(String name, byte id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Function that asks the <code>ComputerPlayerRandom</code> for it's next <code>Move</code>
     * @param turn The nth turn the <code>Game</code> is currently in
     * @return Returns the next <code>Move</code> of this <code>ComputerPlayerRandom</code>
     */
    @Override
    public Move getMove(int turn) {
        Random randomGenerator = new Random();
        List<Short> rows = game.getBoard().availableCols();
        short index = (short) randomGenerator.nextInt(rows.size());
        return new Move(this,index,turn);
    }

    /**
     * Function that returns the name of this <code>ComputerPlayerRandom</code>
     * @return Returns a <code>String</code> containing the name of this <code>ComputerPlayerRandom</code>
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Function that returns the id of this <code>ComputerPlayerRandom</code>
     * @return Returns a byte containing the id of this <code>ComputerPlayerRandom</code>
     */
    @Override
    public byte getId() {
        return id;
    }

    /**
     * Function that changes the id of this<code>ComputerPlayerRandom</code> to the given id
     * @param id A byte containing the new id for this<code>ComputerPlayerRandom</code>
     */
    @Override
    public void setId(byte id) {

    }

    /**
     * Function that returns the <code>Game</code> of this<code>ComputerPlayerRandom</code>
     * @return returns the <code>Game</code> of this<code>ComputerPlayerRandom</code>
     */
    @Override
    public Game getGame() {
        return game;
    }

    /**
     * Function that changes the <code>Game</code> of this <code>ComuterPlayerRandom</code>
     * @param game new <code>Game</code> for this <code>ComputerPlayerRandom</code>
     */
    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Function that returns the <code>Connection</code> of this <code>ComputerPlayerRandom</code>
     * @return Returns the <code>Connection</code> of this <code>ComputerPlayerRandom</code>
     */
    @Override
    public Connection getConnection(){
        if(ConnectServer.isServer) return ConnectServer.server.getConnection(this);
        if(ConnectClient.isClient) return ConnectClient.get().getConnection();
        return null;
    }
}
