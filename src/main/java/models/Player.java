package models;

import models.Cards.CardStack;

public class Player {
    private long id;
    public long getId() {
        return id;
    }

    private String name;
    public String getName() { return name; }

    private CardStack hand;
    public void draw(CardStack deck) {
        hand.draw(deck);
    }


    public Player(long id, String name) {
        this.id = id;
        this.name = name;
        this.hand = new CardStack();
    }
}
