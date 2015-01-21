package mmeent.java.main.connection.game;

import mmeent.java.main.connection.player.LocalPlayer;
import mmeent.java.main.connection.player.Player;

/**
 * Created by Matthias on 20/12/2014.
 */
public class Move {
    private short column;
    private int turn;

    private LocalPlayer localPlayer;
    private byte symbol;

    public Move(Player player, short column, int turn){
        this.column = column;
        this.turn = turn;
        this.localPlayer = localPlayer;
        this.symbol = localPlayer.getId();
    }

    public byte getSymbol(){
        return this.symbol;
    }

    public LocalPlayer getLocalPlayer(){
        return this.localPlayer;
    }

    public int getTurn(){
        return this.turn;
    }

    public int getColumn(){
        return this.column;
    }

    public boolean isValid() {
        return (this.column >= 0 && this.column < this.localPlayer.getGame().getBoard().getWidth() && !this.localPlayer.getGame().getBoard().rowIsFull(this.column));
    }

    public void makeMove() {
        localPlayer.getGame().getBoard().move((short) column,localPlayer.getId());
    }
}
