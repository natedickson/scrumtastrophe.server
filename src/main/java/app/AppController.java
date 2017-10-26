package app;

import logging.Log;
import models.Game;
import models.Player;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AppController {

    private ArrayList<Game> gameList = new ArrayList<>();
    private ArrayList<Player> playerList = new ArrayList<>();
    private long counter = 0;

    @RequestMapping("/")
    public String index() {
        return "hey its me ur code";
    }

    //region Games
    @GetMapping("/games")
    @ResponseBody()
    public Object[] getGames() {
        Log.that("getting list of games");
        return gameList.toArray();
    }

    @PostMapping("/games/create")
    @ResponseBody()
    public Game createGame(@RequestParam() Player player) {
        Log.that("creating a new game for player: ", player.getName());
        Game newGame = new Game(counter++, player);
        gameList.add(newGame);
        return newGame;
    }

    @PatchMapping("/games/join")
    @ResponseBody()
    public Game joinGameById(@RequestParam() Player player, @RequestParam() long gameId) {
        Log.that(player.getName(), " requests to join game #", Long.toString(gameId));
        Game game = getGameById(gameId);
        if (game != null) {
            if (game.getPlayers().stream().anyMatch(p -> p.getName() ==  player.getName())) {
                return null;
            }
            game.addPlayer(player);
        }
        return game;

    }

    @GetMapping("/game")
    @ResponseBody()
    public Game getGameById(@RequestParam() long gameId) {
        Log.that("finding game with id #", Long.toString(gameId));
        Optional<Game> foundGame = gameList.stream().filter(g -> g.getId() == gameId).findFirst();
        if(foundGame.isPresent()) { return foundGame.get(); }
        return null;
    }
    //endregion

    @PostMapping("/player/create")
    @ResponseBody()
    public Player createPlayer(@RequestParam() String playerName) {
        Player player = new Player(counter++, playerName);
        playerList.add(player);
        return player;
    }

    @GetMapping("/player")
    @ResponseBody()
    public Player getPlayerById(@RequestParam() long playerId) {
        Log.that("finding game with id #", Long.toString(playerId));
        Optional<Player> foundPlayer = playerList.stream().filter(g -> g.getId() == playerId).findFirst();
        if(foundPlayer.isPresent()) { return foundPlayer.get(); }
        return null;
    }
}