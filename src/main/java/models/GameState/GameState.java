package models.GameState;

//import models.Cards.*;

import java.util.ArrayList;
import java.util.List;

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

    public GameState() {
//        deck = new CardStack(this.createDeck());
        gameStateCounter = 0;
        history = new ArrayList<>();
        history.add("init");
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
