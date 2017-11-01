package models.Game;

import models.Player.Player;
import java.util.List;

public class GameView {
    public long id;
    public long gameStateCounter;
    public Player owner;
    public List<Player> players;
}
