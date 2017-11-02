package models.PlayerState;

import enums.PlayerRole;

public class PlayerState {
    private long id;
    private String name;
    private PlayerRole role;
    private int availableLoad;

    public PlayerState(long id, String name, PlayerRole role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.availableLoad = 0;
    }

    public PlayerStateView getPlayerStateView() {
        return new PlayerStateView(this.id, this.name, this.role, this.availableLoad);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PlayerRole getRole() {
        return role;
    }

    public int getAvailableLoad() {
        return availableLoad;
    }
}
