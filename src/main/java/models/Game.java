package models;

import java.util.ArrayList;

public class Game {
    private final long id;
    public long getId() {
        return id;
    }

    private Player owner;
    public Player getOwner() { return  owner; }
    public void setOwner(Player newOwner) { this.owner = newOwner; }

    private ArrayList<Player> players;
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public void addPlayer(Player newPlayer) { this.players.add(newPlayer); }
    public boolean removePlayer(Player player) {
        return this.players.remove(player);
    }

    public Game(long id, Player owner) {
        this.id = id;
        this.owner = owner;
        this.players = new ArrayList<>();
        addPlayer(owner);
    }


}
