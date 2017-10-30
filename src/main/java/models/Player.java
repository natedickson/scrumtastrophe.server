package models;

//import models.Cards.CardStack;

public class Player {
    private long id;
    public long getId() {
        return id;
    }

    private String name;
    public String getName() { return name; }

    private long gameId;
    public long getGameId() {
        return gameId;
    }
    public void setGameId(long gameId) { this.gameId = gameId; }
    //    private CardStack hand;
//    public void draw(CardStack deck) {
//        hand.draw(deck);
//    }


    public Player(long id, String name) {
        this.id = id;
        this.name = name;
        this.gameId = 0;
//        this.hand = new CardStack();
    }
}
