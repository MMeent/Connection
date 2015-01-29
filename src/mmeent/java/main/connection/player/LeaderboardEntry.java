package mmeent.java.main.connection.player;

/**
 * Created by Matthias on 19/01/2015.
 * @author mmeent
 */
public class LeaderboardEntry {
    private Player player;
    private int wins;
    private int loss;
    private int total;
    private int ranking;

    public LeaderboardEntry(String name, int argWins, int argLoss, int argTotal, int argRanking) {
        this.player = Player.get(name);
        this.wins = argWins;
        this.loss = argLoss;
        this.total = argTotal;
        this.ranking = argRanking;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getWins() {
        return this.wins;
    }

    public int getLoss() {
        return this.loss;
    }

    public int getTotal() {
        return this.total;
    }

    public int getRanking() {
        return this.ranking;
    }
}
