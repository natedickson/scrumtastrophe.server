package models.Game;

import models.GameState.GameState;
import models.Player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final long id;
    public long getId() {
        return id;
    }

    private Player owner;
    public Player getOwner() { return  owner; }
    public void setOwner(Player newOwner) { this.owner = newOwner; }

    private List<Player> players;
    public List<Player> getPlayers() {
        return players;
    }
    public void addPlayer(Player newPlayer) { this.players.add(newPlayer); }
    public boolean removePlayer(Player player) {
        return this.players.remove(player);
    }

    private GameState gameState;
    public GameState getGameState() { return gameState; }


    public Game(long id, Player owner) {
        this.id = id;
        this.owner = owner;
        this.players = new ArrayList<>();
        addPlayer(owner);
        this.gameState = new GameState(players);
    }

    public GameView getGameViewModel() {
        GameView gvm = new GameView();
        gvm.id = this.id;
        gvm.owner = this.owner;
        gvm.players = this.players;
        return gvm;
    }
}
