package mmeent.java.main.connection.connection;

/**
 * Created by Matthias on 14/01/2015.
 */
public interface Packet {

    /**
     * Return an error coused by the packet to the player that sent the message.
     *
     * @param id the error id of the error
     * @param extras some extra stuff that can be sent back
     */
    public void returnError(int id, String extras);

    /**
     * Write a packet to the given Connection
     *
     * @param connection the connection over whom the packet has to be sent.
     */
    public void write(Connection connection);

    /**
     * Respond to a player with the given packet
     *
     * @param packet the packet you are responding with
     */
    public void respond(Packet packet);

    /**
     * A method that is called once the packet has been received and read.
     */
    public void onReceive();
}
