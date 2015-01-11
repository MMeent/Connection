package mmeent.java.main.connection.game;

import mmeent.java.main.connection.player.Player;

/**
 * Created by Matthias on 20/12/2014.
 */
public class Move {
    private int column;
    private int turn;

    private Player player;
    private byte symbol;

    public Move(Player player, int column, int turn){
        this.column = column;
        this.turn = turn;
        this.player = player;
        this.symbol = player.getId();
    }

    public byte getSymbol(){
        return this.symbol;
    }

    public Player getPlayer(){
        return this.player;
    }

    public int getTurn(){
        return this.turn;
    }

    public int getColumn(){
        return this.column;
    }
}
