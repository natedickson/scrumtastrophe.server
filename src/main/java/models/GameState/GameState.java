package models.GameState;

//import models.Cards.*;
import models.Action.Action;
import roller.Roll;

import models.Player.Player;
import models.PlayerState.*;
import models.StoryState.StoryState;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameState {
//    private CardStack deck;
    private long storyIdCounter;
    private long gameStateCounter;
    public long getGameStateCounter() {
        return gameStateCounter;
    }

    private List<String> history;
    public List<String> getHistory(long fromGameState) {
        return history.subList((int) fromGameState+1, (int) gameStateCounter);
    }
    private void storeHistory(String update) {
        gameStateCounter++;
        history.add((int)gameStateCounter, update);
    }

    private List<String> chatLog;
    public void addChatMessage(String message){
        this.chatLog.add(message);
    }
    public List<String> getRecentChatMessages(){
        return this.chatLog.size() < 10 ?
                this.chatLog :
                this.chatLog.subList(chatLog.size() - 10, chatLog.size());
    }

    private Map<Long,PlayerState> playerStates;
    private List<StoryState> generatedBacklog;
    private Map<Long,StoryState> sprintStories;
    private List<StoryState> successfullyReleased;

    public GameState(Player owner) {
//        deck = new CardStack(this.createDeck());
        storyIdCounter = 1;
        gameStateCounter = 0;
        history = new ArrayList<>();
        history.add("init");
        chatLog = new ArrayList<>();
        playerStates = new HashMap<>();
        PlayerState ownersState = new PlayerState(owner.getId(), owner.getName(), owner.getRole());
        ownersState.addAvailableAction(new Action("Start Game", "beginGame", ""));
        playerStates.put(owner.getId(), ownersState);
        generatedBacklog = new ArrayList<>();
        sprintStories = new HashMap<>();
        successfullyReleased = new ArrayList<>();
    }

    public Map<Long, PlayerState> getPlayerStates() {
        return playerStates;
    }
    public PlayerState getPlayerStateById(long id) {
        return playerStates.get(id);
    }
    public StoryState getStoryStateById(long id) { return sprintStories.get(id); }
    public boolean anyAvailablePlayerActions() {
        return playerStates.values().stream().anyMatch(PlayerState::hasAvailableActions);
    }

    public void addGamePlayer(PlayerState playerState) {
//        storeHistory("addGamePlayer");
        this.playerStates.put(playerState.getId(), playerState);
    }

    public void beginGame() {
        this.generatedBacklog.addAll(generateBacklog(playerStates.size() * 10)); //10 times # players is the perfect amount
        //Ideally then the players would pick which stories to pull in, but for now we'll give them the first 10;
        for(StoryState story : this.generatedBacklog.subList(0,10)) {
            this.sprintStories.put(story.id, story);
        }
        this.playerStates.forEach((id,ps) -> {
            ps.addAvailableAction(new Action("Roll for Load", "rollLoad", ""));
        });
        gameStateCounter++;
    }

    public void beginNextTurn() {
        this.playerStates.forEach((id,ps) -> {
            ps.setAvailableLoad(0);
            ps.addAvailableAction(new Action("Roll for Load", "rollLoad", ""));
        });
        gameStateCounter++;
    }

    public GameStateView getGameStateView(long playerId) {
        GameStateView gsv = new GameStateView();
        gsv.currentState = gameStateCounter;
        gsv.playerSummaries = playerStates.values().stream().map(PlayerState::getPlayerStateView).collect(Collectors.toList());
        gsv.sprintStories = sprintStories.values().stream().map(StoryState::getStoryStateView).collect(Collectors.toList());
        gsv.chatMessages = getRecentChatMessages();
        PlayerState player = playerStates.get(playerId);
        gsv.availableLoad = player.getAvailableLoad();
        gsv.availableActions = player.getAvailableActions();
        return gsv;
    }

    private List<StoryState> generateBacklog(int total) {
        List<StoryState> stories = new ArrayList<>();
        for(int i=0; i<total; i++) {
            StoryState story = new StoryState(storyIdCounter++, "StoryTitle", Roll.from(1,5), Roll.from(1,5));
            stories.add(story);
        }
        return stories;
    }

//    public CardStack getDeck() {
//        return deck;
//    }


//    private List<Card> createDeck() {
//        ActionCard card1 = new ActionCard("action1","description1");
//        BugCard card2 = new BugCard("bug1","description1");
//        List<Card> newDeck = new List<>();
//        newDeck.add(card1);
//        newDeck.add(card2);
//        return newDeck;
//    }
}
