package mmeent.java.main.connection.connection;

/**
 * Created by Matthias on 14/01/2015.
 */
public interface Packet {
    public void returnError(int id, String extras);
    public void write(Connection connection);
    public void respond(Packet packet);
    public void onReceive();
}
