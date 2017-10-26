package models.Cards;

import java.util.ArrayList;

public class CardStack {
    private ArrayList<Card> cards;
    public Card[] get() {
        int stackLength = cards.size();
        Card[] currentCards = new Card[stackLength];
        for(int i=0; i<stackLength; i++) {
            currentCards[i] = cards.get(i);
        }
        return currentCards;
    }
    public void draw(CardStack from) {
        cards.add(from.discard(0));
    }
    public Card discard(int index) {
        return cards.remove(index);
    }

    public CardStack() {
        this(new ArrayList<>());
    }
    public CardStack(ArrayList<Card> startingCards) {
        this.cards = startingCards;
    }
}
