package mmeent.java.main.connection.connection;

import mmeent.java.main.connection.player.Player;

/**
 * Created by Matthias on 14/01/2015.
 */
public interface Packet {

    /**
     * Return an error caused by the packet to the player that sent the message.
     *
     * @param extras some extra stuff that can be sent back
     */
    public void returnError(String extras);

    /**
     * Write a packet to the given Connection.
     *
     * @param connection the connection over whom the packet has to be sent.
     */
    public void write(Connection connection);

    /**
     * Respond to a player with the given packet.
     *
     * @param packet the packet you are responding with
     */
    public void respond(Packet packet);

    /**
     * A method that is called once the packet has been received and read.
     */
    public void onReceive();

    /**
     * Get the connection of the packet.
     *
     * @return the connection the packet was sent over;
     */
    public Connection getConnection();

    /**
     * Easy way to get the player that sent the packet.
     *
     * @return the player that sent the packet
     */
    public Player getClient();
}
