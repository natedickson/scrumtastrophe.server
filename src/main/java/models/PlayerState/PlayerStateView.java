package models.PlayerState;

import enums.PlayerRole;

public class PlayerStateView {
    public long id;
    public String name;
    public PlayerRole role;
    public int availableLoad;

    public PlayerStateView(long id, String name, PlayerRole role, int availableLoad) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.availableLoad = availableLoad;
    }
}
