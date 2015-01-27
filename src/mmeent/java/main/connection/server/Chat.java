package mmeent.java.main.connection.server;

import mmeent.java.main.connection.ConnectServer;
import mmeent.java.main.connection.connection.server.ServerPacket;
import mmeent.java.main.connection.player.LocalPlayer;
import mmeent.java.main.connection.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthias on 19/01/2015.
 * @author mmeent;
 *
 * A class that will handle the chat and chatrooms.
 */
public class Chat {
    private List<String> msgs = new ArrayList<String>();
    private List<Player> plrs = new ArrayList<Player>();

    /**
     * the Chat object does not need any arguments
     */
    public Chat() {

    }

    /**
     * Add a message to the chat
     *
     * @param msg The message that is sent to the currently active chatroom
     */
    public synchronized void addMessage(Player player, String msg) {
        this.msgs.add(msg);
        this.plrs.add(player);
        ServerPacket.ChatPacket packet = new ServerPacket.ChatPacket(null, player, msg);
        for(Player p: ConnectServer.server.getPlayers()){
            p.getConnection().send(packet);
        }
    }

}
