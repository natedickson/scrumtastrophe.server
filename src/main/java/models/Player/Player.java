package models.Player;

//import models.Cards.CardStack;

import enums.PlayerRole;

public class Player {
    private long id;
    public long getId() {
        return id;
    }

    private String name;
    public String getName() { return name; }

    private long gameId;
    public long getGameId() {
        return gameId;
    }
    public void setGameId(long gameId) { this.gameId = gameId; }

    private PlayerRole role;
    public PlayerRole getRole() {
        return role;
    }
    public void setRole(PlayerRole role) {
        this.role = role;
    }

    public Player(long id, String name, PlayerRole role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.gameId = 0;
//        this.hand = new CardStack();
    }
}
