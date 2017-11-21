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
    private int minLoad;
    private int maxLoad;
    private List<Action> availableActions;

    public PlayerState(long id, String name, PlayerRole role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.availableLoad = 0;
        this.minLoad = 1;
        this.maxLoad = 4;
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

    public void setAvailableLoad(int availableLoad) {
        this.availableLoad = availableLoad;
    }

    public int getAvailableLoad() {
        return availableLoad;
    }

    public int getMaxLoad() {
        return maxLoad;
    }

    public int getMinLoad() {
        return minLoad;
    }

    public List<Action> getAvailableActions() {
        return availableActions;
    }
    public void addAvailableAction(Action action) {
        availableActions.add(action);
    }
    public void removeAvailableAction(String actionToRemove) {
        Action toRemove = availableActions.stream().filter((action) -> action.action.equals(actionToRemove)).findFirst().orElse(null);
        if(toRemove == null) {
            return;
        }
        availableActions.remove(toRemove);
    }
    public boolean hasAvailableActions() {
        return availableActions.size() > 0;
    }
}
