package models.GameState;

//import models.Cards.*;

import models.Player.Player;
import models.PlayerState.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
//    private CardStack deck;
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

    private List<PlayerState> playerStates;

    public GameState(List<Player> players) {
//        deck = new CardStack(this.createDeck());
        gameStateCounter = 0;
        history = new ArrayList<>();
        history.add("init");
        playerStates = players.stream().map((player) -> {
            PlayerState ps = new PlayerState(player.getId(), player.getName(), player.getRole());
            return ps;
        }).collect(Collectors.toList());
    }

    public List<PlayerState> getPlayerStates() {
        return playerStates;
    }

    public void addGamePlayer(PlayerState playerState) {
        storeHistory("addGamePlayer");
        this.playerStates.add(playerState);
    }

    public GameStateView getGameStateView(long playerId) {
        GameStateView gsv = new GameStateView();
        gsv.currentState = gameStateCounter;
        gsv.playerSummaries = new ArrayList<>();
        gsv.availableLoad = 0;
        for(PlayerState player : playerStates) {
            PlayerStateView psv = new PlayerStateView();
            psv.availableLoad = player.getAvailableLoad();
            psv.id = player.getId();
            psv.name = player.getName();
            psv.role = player.getRole();
            gsv.playerSummaries.add(psv);

            if(playerId == psv.id) gsv.availableLoad = psv.availableLoad;
        }
        return gsv;
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
