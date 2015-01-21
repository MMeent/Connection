package mmeent.java.main.connection.game;

import mmeent.java.main.connection.player.Player;

import java.util.HashMap;

/**
 * Created by Matthias on 21/01/2015.
 */
public class Invite {
    public static HashMap<Player, HashMap<Player, Invite>> invites = new HashMap<Player, HashMap<Player, Invite>>();
    private Player player1;
    private Player player2;
    private short boardWidth;
    private short boardHeight;

    public Invite(Player player1, Player player2, short boardHeight, short boardWidth){
        this.player1 = player1;
        this.player2 = player2;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        if(!Invite.invites.containsKey(player1)) Invite.invites.put(player1, new HashMap<Player, Invite>());
        Invite.invites.get(player1).put(player2, this);
    }

    public Game startGame(){
        Player[] players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        Invite.invites.get(player1).remove(player2);
        return new Game(players, this.boardWidth, this.boardHeight);
    }
}
