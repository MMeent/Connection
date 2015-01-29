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

public class ComputerPlayerRandom implements Player {
    private String name;
    private byte id;
    private Game game;

    /**
     * Constructor of <code>ComputerPlayerRandom</code>.
     * @param argName Name of the new <code>ComputerPlayerRandom</code>
     * @param argId Id of the new<code>ComputerPlayerRandom</code>
     */
    public ComputerPlayerRandom(String argName, byte argId) {
        this.name = argName;
        this.id = argId;
    }

    /**
     * Function that asks the <code>ComputerPlayerRandom</code> for it's next <code>Move</code>.
     * @param turn The nth turn the <code>Game</code> is currently in
     * @return Returns the next <code>Move</code> of this <code>ComputerPlayerRandom</code>
     */
    @Override
    public Move getMove(int turn) {
        Random randomGenerator = new Random();
        List<Short> rows = game.getBoard().availableCols();
        short index = (short) randomGenerator.nextInt(rows.size());
        return new Move(this, index, turn);
    }

    /**
     * Function that returns the name of this <code>ComputerPlayerRandom</code>.
     * @return Returns a <code>String</code> containing
     * the name of this <code>ComputerPlayerRandom</code>
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Function that returns the id of this <code>ComputerPlayerRandom</code>.
     * @return Returns a byte containing the id of this <code>ComputerPlayerRandom</code>
     */
    @Override
    public byte getId() {
        return id;
    }

    /**
     * Function that changes the id of this<code>ComputerPlayerRandom</code> to the given id.
     * @param argId A byte containing the new id for this<code>ComputerPlayerRandom</code>
     */
    @Override
    public void setId(byte argId) {

    }

    /**
     * Function that returns the <code>Game</code> of this<code>ComputerPlayerRandom</code>.
     * @return returns the <code>Game</code> of this<code>ComputerPlayerRandom</code>
     */
    @Override
    public Game getGame() {
        return game;
    }

    /**
     * Function that changes the <code>Game</code> of this <code>ComuterPlayerRandom</code>.
     * @param argGame new <code>Game</code> for this <code>ComputerPlayerRandom</code>
     */
    @Override
    public void setGame(Game argGame) {
        this.game = argGame;
    }

    /**
     * Function that returns the <code>Connection</code> of this <code>ComputerPlayerRandom</code>.
     * @return Returns the <code>Connection</code> of this <code>ComputerPlayerRandom</code>
     */
    @Override
    public Connection getConnection() {
        if (ConnectServer.isServer) {
            return ConnectServer.server.getConnection(this);
        }
        if (ConnectClient.isClient) {
            return ConnectClient.get().getConnection();
        }
        return null;
    }
}
