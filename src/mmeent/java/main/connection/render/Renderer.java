package mmeent.java.main.connection.render;

import mmeent.java.main.connection.board.Board;

/**
 * Created by Matthias on 14/01/2015.
 */
public interface Renderer {
    /**
     * Changes the <code>Board</code> it renders.
     * @param board <code>Board</code> that it has to render.
     */
    public void setBoard(Board board);

    /**
     * Displays the given message.
     * @param msg String containging the message that should be displayed.
     */
    public void addChatMessage(String msg);

    /**
     * Function that renders the current status of the <code>Board</code>
     */
    public void render();

    /**
     * Function that displays an error message.
     * @param id Id of the error
     * @param msg String containing an errormessage that should be displayed
     */
    public void addErrorMessage(String id, String msg);

    /**
     * Function that shows a message.
     * @param msg String containing a message that should be displayed.
     */
    public void addMessage(String msg);
}
