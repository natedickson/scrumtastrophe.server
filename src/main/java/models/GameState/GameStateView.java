package models.GameState;

import models.PlayerState.PlayerStateView;

import java.util.List;

public class GameStateView {
    public long currentState;
    public List<PlayerStateView> playerSummaries;
    public int availableLoad;

}
