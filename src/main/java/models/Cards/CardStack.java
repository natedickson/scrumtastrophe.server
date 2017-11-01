package models.Cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardStack implements Serializable{
    private List<Card> cards;
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
    public CardStack(List<Card> startingCards) {
        this.cards = startingCards;
    }
}
