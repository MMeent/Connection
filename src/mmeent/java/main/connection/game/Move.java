package mmeent.java.main.connection.game;

import mmeent.java.main.connection.player.LocalPlayer;

/**
 * Created by Matthias on 20/12/2014.
 */
public class Move {
    private int column;
    private int turn;

    private LocalPlayer localPlayer;
    private byte symbol;

    public Move(LocalPlayer localPlayer, int column, int turn){
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
        return (column <= 0 && column < localPlayer.getGame().getBoard().getWidth() && !localPlayer.getGame().getBoard().rowIsFull(column));
    }

    public void makeMove() {
        for (int i = 0; i < localPlayer.getGame().getBoard().getHeight(); i++) {
            if(localPlayer.getGame().getBoard().getField(column,i) == 0) {
                localPlayer.getGame().getBoard().setField(column,i,localPlayer.getId());
            }
        }
    }
}
