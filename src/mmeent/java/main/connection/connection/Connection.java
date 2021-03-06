package mmeent.java.main.connection.connection;

import mmeent.java.main.connection.ConnectClient;
import mmeent.java.main.connection.ConnectServer;
import mmeent.java.main.connection.Protocol;
import mmeent.java.main.connection.player.Player;

import java.io.*;
import java.net.Socket;

/**
 * Created by Matthias on 14/01/2015.
 * @author mmeent
 */
public class Connection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private StringBuilder privBuffer;
    private Side side;
    private Connection c;
    private Player client = Player.get("New");

    /**
     * Default constructor for a connection.
     * @param argSocket the socket over which the connection is sent
     * @param argSide the side this part of the connection is on
     */
    public Connection(Socket argSocket, Side argSide) {
        this.socket = argSocket;
        this.c = this;
        this.side = argSide;
        if (socket == null) {
            return;
        }
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        Thread read = (this.side == Side.SERVER) ? new Thread() {
            /**
             * This is a thread that puts all incoming packets
             * into the server packet stack. Only if the connection side is server
             */
            public void run() {
                boolean done = false;
                while (!done) {
                    try {
                        String msg = in.readLine();
                        ConnectServer.packets.put(Packets.readClientPacket(c, msg));
                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                        try {
                            in.close();
                        } catch (IOException f) {
                            f.printStackTrace();
                        }
                        done = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.out);
                        try {
                            in.close();
                        } catch (IOException f) {
                            f.printStackTrace();
                        }
                        done = true;
                    }
                }
            }
        } : new Thread() {
            /**
             * This thread puts all incoming packets into the client packet stack.
             * Only if the connection side is client.
             */
            public void run() {
                boolean done = false;
                while (!done) {
                    try {
                        String msg = in.readLine();
                        ConnectClient.packets.put(Packets.readServerPacket(c, msg));
                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                        try {
                            in.close();
                        } catch (IOException f) {
                            f.printStackTrace();
                        }
                        done = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.out);
                        try {
                            in.close();
                        } catch (IOException f) {
                            f.printStackTrace();
                        }
                        done = true;
                    }
                }
            }
        };

        read.setDaemon(true);
        read.start();

    }

    /**
     * Start a new packet.
     * This is done with a new <code>StringBuilder</code>
     * to optimize the string handling.
     */
    public synchronized void startPacket() {
        this.privBuffer = new StringBuilder();
    }

    /**
     * Write a string to the packet buffer, and add the delimiter.
     * @param string the string to append to the packet buffer.
     */
    public synchronized void writePartial(String string) {
        if (!(this.privBuffer.length() == 0)) {
            this.privBuffer.append(Protocol.Settings.DELIMITER);
        }
        if (!string.equals("")) {
            this.privBuffer.append(string);
        }
    }

    /**
     * End the packet. Write the PACKET_END to the buffer.
     */
    public synchronized void stopPacket() {
        this.privBuffer.append(Protocol.Settings.PACKET_END);
    }

    /**
     * Send the current packet buffer to the other side.
     */
    public synchronized void sendBuffer() {
        this.sendCharSequence(this.privBuffer);
    }

    /**
     * Clears the current packet buffer.
     */
    public synchronized void clearBuffer() {
        this.privBuffer = new StringBuilder();
    }

    /**
     * Sends the charsequence.
     * @param chars the CharSequence that has to be sent.
     */
    public synchronized void sendCharSequence(CharSequence chars) {
        if (ConnectServer.debug || ConnectClient.debug) {
            if (this.side == Side.SERVER) {
                System.out.print(this.client.getName());
            }
            System.out.print(" <: " + chars);
        }
        this.out.append(chars);
        this.out.flush();
    }

    /**
     * Sent the given packet over this connection.
     * @param packet the packet to be sent.
     */
    public synchronized void send(Packet packet) {
        packet.write(this);
    }

    /**
     * Set the corresponding player to the given player.
     * @param argClient the player that will be the owner of the connection.
     */
    public synchronized void setClient(Player argClient) {
        this.client = argClient;
    }

    /**
     * Get the player that corresponds with this.
     * @return the player that corresponds to the client side of this connection
     */
    public synchronized Player getClient() {
        return this.client;
    }

    public synchronized void quit() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
}
