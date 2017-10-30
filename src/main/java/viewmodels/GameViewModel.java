package viewmodels;

import models.Player;
import java.util.ArrayList;

public class GameViewModel {
    public long id;
    public long gameStateCounter;
    public Player owner;
    public ArrayList<Player> players;
}
