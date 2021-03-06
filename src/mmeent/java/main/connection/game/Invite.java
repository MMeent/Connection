package mmeent.java.main.connection.game;

import mmeent.java.main.connection.connection.server.ServerPacket;
import mmeent.java.main.connection.player.Player;

import java.util.HashMap;

/**
 * Created by Matthias on 21/01/2015.
 * @author mmeent
 *
 * A class to represent an invite, and general registry for invites.
 */
public class Invite {
    /**
     * This registers all invites.
     */
    public static HashMap<Player, HashMap<Player, Invite>> invites =
            new HashMap<Player, HashMap<Player, Invite>>();

    private Player player1;
    private Player player2;
    private short boardWidth;
    private short boardHeight;

    /**
     * An general invite object.
     * @param argPlayer1 the player that invites player 2
     * @param argPlayer2 the player that is invited to a game
     * @param argBoardHeight the height of the board
     * @param argBoardWidth the width of the board
     */
    public Invite(Player argPlayer1, Player argPlayer2, short argBoardHeight, short argBoardWidth) {
        this.player1 = argPlayer1;
        this.player2 = argPlayer2;
        this.boardWidth = argBoardWidth;
        this.boardHeight = argBoardHeight;
        if (!Invite.invites.containsKey(player1))  {
            Invite.invites.put(player1, new HashMap<Player, Invite>());
        }
        Invite.invites.get(player1).put(player2, this);
    }

    /**
     * Start a game that has the properties given in the invite properties.
     * @return a <code>Game</code> object with preset values.
     */
    public Game startGame() {
        Player[] players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        Invite.invites.get(player1).remove(player2);
        ServerPacket.GameStartPacket p = new ServerPacket.GameStartPacket(null, player1, player2);
        player1.getConnection().send(p);
        player2.getConnection().send(p);
        return new Game(players, this.boardWidth, this.boardHeight);
    }
}
