package models.GameState;

import models.PlayerState.PlayerStateView;
import models.StoryState.StoryStateView;

import java.util.List;

public class GameStateView {
    public long currentState;
    public List<PlayerStateView> playerSummaries;
    public int availableLoad;
    public List<String> chatMessages;
    public List<StoryStateView> sprintStories;
}
