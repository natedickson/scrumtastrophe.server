package models;

import models.Cards.*;

import java.util.ArrayList;

public class GameState {
    private CardStack deck;

    public GameState() {
        deck = new CardStack(this.createDeck());
    }

    public CardStack getDeck() {
        return deck;
    }

    private ArrayList<Card> createDeck() {
        ActionCard card1 = new ActionCard("action1","description1");
        BugCard card2 = new BugCard("bug1","description1");
        ArrayList<Card> newDeck = new ArrayList<>();
        newDeck.add(card1);
        newDeck.add(card2);
        return newDeck;
    }
}
