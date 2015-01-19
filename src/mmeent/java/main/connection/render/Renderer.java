package mmeent.java.main.connection.render;

import mmeent.java.main.connection.board.Board;

/**
 * Created by Matthias on 14/01/2015.
 */
public interface Renderer {
    public void setBoard(Board board);
    public void addChatMessage(String msg);
    public void render();
    public void addErrorMessage(String msg);
}
