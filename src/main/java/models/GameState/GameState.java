package models.GameState;

//import models.Cards.*;
import models.StoryState.StoryStateView;
import roller.Roll;

import models.Player.Player;
import models.PlayerState.*;
import models.StoryState.StoryState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

    private List<PlayerState> playerStates;
    private List<StoryState> generatedBacklog;
    private List<StoryState> sprintStories;
    private List<StoryState> successfullyReleased;

    public GameState(List<Player> players) {
//        deck = new CardStack(this.createDeck());
        storyIdCounter = 1;
        gameStateCounter = 0;
        history = new ArrayList<>();
        history.add("init");
        chatLog = new ArrayList<>();
        playerStates = players.stream().map((player) -> {
            PlayerState ps = new PlayerState(player.getId(), player.getName(), player.getRole());
            return ps;
        }).collect(Collectors.toList());
    }

    public List<PlayerState> getPlayerStates() {
        return playerStates;
    }

    public void addGamePlayer(PlayerState playerState) {
//        storeHistory("addGamePlayer");
        this.playerStates.add(playerState);
    }

    public void beginGame() {
        this.generatedBacklog = generateBacklog(playerStates.size() * 10); //10 times # players is the perfect amount
    }

    public GameStateView getGameStateView(long playerId) {
        GameStateView gsv = new GameStateView();
        gsv.currentState = gameStateCounter;
        gsv.playerSummaries = new ArrayList<>();
        gsv.sprintStories = new ArrayList<>();
        gsv.availableLoad = 0;
        gsv.chatMessages = getRecentChatMessages();

        for(PlayerState player : playerStates) {
            PlayerStateView psv = player.getPlayerStateView();
            gsv.playerSummaries.add(psv);
            if(playerId == psv.id) gsv.availableLoad = psv.availableLoad;
        }
        for(StoryState story : sprintStories) {
            gsv.sprintStories.add(story.getStoryStateView());
        }
        return gsv;
    }

    private List<StoryState> generateBacklog(int total) {
        List<StoryState> stories = new ArrayList<>();
        for(int i=0; i<total; i++) {
            StoryState story = new StoryState(storyIdCounter++, "StoryTitle", Roll.from(5,10), Roll.from(5,10));
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
