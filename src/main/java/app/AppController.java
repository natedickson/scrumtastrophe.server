package app;

import enums.PlayerRole;
import logging.Log;
import models.Game.*;
import models.GameState.*;
import models.Player.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class AppController {

    private List<Game> gameList = new ArrayList<>();
    private List<Player> playerList = new ArrayList<>();
    private long counter = 1;

    @RequestMapping("/")
    public String index() {
        return "Server init complete";
    }

    //region Games
    @GetMapping("/games")
    @ResponseBody()
    public List<GameView> getGames() {
        Log.that("getting list of games");
        return gameList.stream().map(Game::getGameViewModel).filter(g -> g.gameStateCounter == 0).collect(Collectors.toList());
    }

    @PostMapping("/games/create")
    @ResponseBody()
    public GameView createGame(@RequestBody() long playerId) {
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
    public GameView joinGameById(@RequestBody() GameJoinPost gameJoinPost) {
        long playerId = gameJoinPost.playerId;
        long gameId = gameJoinPost.gameId;
        Player player = getPlayerById(playerId);
        Log.that(player.getName(), " requests to join game #", Long.toString(gameId));
        Game game = getGameById(gameId);
        if (game != null && game.getGameState().getGameStateCounter() == 0) {
            game.addPlayer(player);
            return game.getGameViewModel();
        }
        return null;
    }

    @GetMapping("/game")
    @ResponseBody()
    public Game getGameById(@RequestBody() long gameId) {
        Log.that("finding game with id #", Long.toString(gameId));
        return gameList.stream().filter(g -> g.getId() == gameId).findFirst().orElse(null);
        // .findFirst returns an Optional<Game>; .orElse allows us to give a default value when nothing is found
    }

    @GetMapping("/game/state/")
    @ResponseBody()
    public GameStateResponse getGameState(@RequestParam() long gameId, @RequestParam() long currentGameState) {
        Log.that("getting game state for game #", Long.toString(gameId));
        Game game = getGameById(gameId);
        if(game == null) throw new IndexOutOfBoundsException("no game found.");

        GameStateResponse response = new GameStateResponse();
        response.updateHistory = new ArrayList<>();
        GameState gameState = game.getGameState();
        long trueGameState = gameState.getGameStateCounter();

        if(currentGameState == trueGameState) return response;

        if(currentGameState > trueGameState) throw new IllegalStateException("player at invalid state.");

        response.gameState = trueGameState;
        response.updateHistory.addAll(gameState.getHistory(currentGameState));
        return response;
    }
    //endregion
    private PlayerRole getPlayerRole(String fromString) {
        if(fromString.equals("QA")) return PlayerRole.QA;
        if(fromString.equals("DEV")) return PlayerRole.DEV;
        return null;
    }

    @PostMapping("/player/roles")
    @ResponseBody()
    public List<String> getPlayerRoles() {
        Log.that("getting player roles");
        List<String> playerRoles = new ArrayList<>();
        playerRoles.add("QA");
        playerRoles.add("DEV");
        return playerRoles;
    }

    @PostMapping("/player/create")
    @ResponseBody()
    public Player createPlayer(@RequestBody() PlayerCreatePost playerCreatePost) {
        Log.that("creating player with name ", playerCreatePost.name);
        Player player = new Player(counter++, playerCreatePost.name, getPlayerRole(playerCreatePost.role));
        playerList.add(player);
        return player;
    }

    @GetMapping("/player")
    @ResponseBody()
    public Player getPlayerById(@RequestBody() long playerId) {
        Log.that("finding player with id #", Long.toString(playerId));
        return playerList.stream().filter(g -> g.getId() == playerId).findFirst().orElse(null);
    }
}