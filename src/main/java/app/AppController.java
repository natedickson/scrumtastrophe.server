package app;

import logging.Log;
import models.Game;
import models.Player;
import org.springframework.web.bind.annotation.*;
import viewmodels.GameViewModel;

import java.util.ArrayList;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AppController {

    private ArrayList<Game> gameList = new ArrayList<>();
    private ArrayList<Player> playerList = new ArrayList<>();
    private long counter = 1;

    @RequestMapping("/")
    public String index() {
        return "Server init complete";
    }

    //region Games
    @GetMapping("/games")
    @ResponseBody()
    public ArrayList<Game> getGames() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.that("getting list of games");
        return gameList;
    }

    @PostMapping("/games/create")
    @ResponseBody()
    public GameViewModel createGame(@RequestBody() long playerId) {
        Log.that("creating a new game for player: ", Long.toString(playerId));
        Player player = getPlayerById(playerId);
        long gameId = counter++;
        player.setGameId(gameId);
        Game newGame = new Game(gameId, player);
        gameList.add(newGame);
        return newGame.getGameViewModel();
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
    public Player createPlayer(@RequestBody() String playerName) {
        Log.that("creating player with name ", playerName);
        Player player = new Player(counter++, playerName);
        playerList.add(player);
        return player;
    }

    @GetMapping("/player")
    @ResponseBody()
    public Player getPlayerById(@RequestBody() long playerId) {
        Log.that("finding player with id #", Long.toString(playerId));
        Optional<Player> foundPlayer = playerList.stream().filter(g -> g.getId() == playerId).findFirst();
        if(foundPlayer.isPresent()) { return foundPlayer.get(); }
        return null;
    }
}