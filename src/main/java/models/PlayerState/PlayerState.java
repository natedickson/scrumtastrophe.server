package models.PlayerState;

import enums.PlayerRole;
import models.Action.Action;

import java.util.ArrayList;
import java.util.List;

public class PlayerState {
    private long id;
    private String name;
    private PlayerRole role;
    private int availableLoad;
    private List<Action> availableActions;

    public PlayerState(long id, String name, PlayerRole role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.availableLoad = 0;
        this.availableActions = new ArrayList<>();
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

    public List<Action> getAvailableActions() {
        return availableActions;
    }
    public void addAvailableAction(Action action) {
        availableActions.add(action);
    }
    public void removeAvailableAction(String actionLabel) {
        Action toRemove = availableActions.stream().filter((action) -> action.label.equals(actionLabel)).findFirst().orElse(null);
        if(toRemove == null) {
            return;
        }
        availableActions.remove(toRemove);
    }
}
