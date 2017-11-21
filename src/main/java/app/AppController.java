package app;

import enums.PlayerRole;
import logging.Log;
import models.Action.Action;
import models.Chat.ChatPost;
import models.Game.*;
import models.GameState.*;
import models.Player.*;
import models.PlayerState.PlayerState;
import models.StoryState.StoryState;
import org.springframework.web.bind.annotation.*;
import roller.Roll;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
public class AppController {

    private Map<Long,Game> gameList = new HashMap<>();
    private Map<Long,Player> playerList = new HashMap<>();
    private long counter = 1;

    @RequestMapping("/")
    public String index() {
        return "Server init complete";
    }
    //region Game Lobbies
    @GetMapping("/games")
    @ResponseBody()
    public List<GameView> getGames() {
        Log.that("getting list of games");
        return gameList.values().stream().filter(g -> g.getGameState().getGameStateCounter() == 0).map(Game::getGameViewModel).collect(Collectors.toList());
    }

    @PostMapping("/games/create")
    @ResponseBody()
    public GameView createGame(@RequestBody() long playerId) {
        Player player = playerList.get(playerId);
        Log.that("creating a new game for player: ", player.getName());
        long gameId = counter++;
        player.setGameId(gameId);
        Game newGame = new Game(gameId, player);
        gameList.put(gameId, newGame);
        return newGame.getGameViewModel();
    }

    @PatchMapping("/games/join")
    @ResponseBody()
    public GameView joinGameById(@RequestBody() PlayerGameContext gameJoinPost) {
        long playerId = gameJoinPost.playerId;
        long gameId = gameJoinPost.gameId;
        Player player = playerList.get(playerId);
        Log.that(player.getName(), " requests to join game #", Long.toString(gameId));
        Game game = gameList.get(gameId);
        if (game != null && game.getGameState().getGameStateCounter() == 0) {
            game.addPlayer(player);
            game.getGameState().addGamePlayer(new PlayerState(player.getId(),player.getName(),player.getRole()));
            player.setGameId(gameId);
            return game.getGameViewModel();
        }
        return null;
    }

//    @GetMapping("/game")
//    @ResponseBody()
//    public Game getGameById(@RequestBody() long gameId) {
//        return gameList.stream().filter(g -> g.getId() == gameId).findFirst().orElse(null);
//        // .findFirst returns an Optional<Game>; .orElse allows us to give a default value when nothing is found
//    }

    //endregion

    //region GameState
    @GetMapping("/game/state/update")
    @ResponseBody()
    public GameStateResponse getGameStateUpdates(@RequestParam() long gameId, @RequestParam() long playerId, @RequestParam() long currentGameState) {
        Log.that("getting game state for game #", Long.toString(gameId));
        Game game = gameList.get(gameId);
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

    @GetMapping("/game/state")
    @ResponseBody()
    public GameStateView getGameState(@RequestParam() long gameId, @RequestParam() long playerId) {
        Game game = gameList.get(gameId);
        if(game == null) throw new IndexOutOfBoundsException("no game found.");

        GameState state = game.getGameState();
        return state.getGameStateView(playerId);
    }

    @PostMapping("/game/chat")
    @ResponseBody()
    public List<String> sendChatMessage(@RequestBody() ChatPost chatObj){
        Player player = playerList.get(chatObj.playerId);
        Log.that("send chat message of ", chatObj.message,
                "for game #", Long.toString(chatObj.gameId),
                "for player ", player.getName());
        Game game = gameList.get(chatObj.gameId);
        if(game == null){
            throw new IndexOutOfBoundsException("no game found.");
        }

        GameState state = game.getGameState();
        String message = player.getName() + ": " + chatObj.message;
        state.addChatMessage(message);
        return state.getRecentChatMessages();
    }

    //endregion

    //region Player
    private PlayerRole getPlayerRole(String fromString) {
        if(fromString.equals("QA")) return PlayerRole.QA;
        if(fromString.equals("DEV")) return PlayerRole.DEV;
        return null;
    }
    @GetMapping("/player/roles")
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
        playerList.put(player.getId(), player);
        return player;
    }

//    @GetMapping("/player")
//    @ResponseBody()
//    public Player getPlayerById(@RequestBody() long playerId) {
//        Log.that("finding player with id #", Long.toString(playerId));
//        return playerList.stream().filter(g -> g.getId() == playerId).findFirst().orElse(null);
//    }
    //endregion

    //region Action
    @PostMapping("/{gameId}/{playerId}/action/beginGame")
    public void actionBeginGame(@PathVariable() long gameId, @PathVariable() long playerId) {
        Log.that("beginning game #", Long.toString(gameId));
        GameState gameState = gameList.get(gameId).getGameState();
        PlayerState player = gameState.getPlayerStateById(playerId);
        if(!validateAction(player,"beginGame")) return;
        gameState.beginGame();
        gameState.getPlayerStateById(playerId).removeAvailableAction("beginGame");
    }

    @PostMapping("/{gameId}/{playerId}/action/rollLoad")
    public void actionRollLoad(@PathVariable() long gameId, @PathVariable() long playerId) {
        GameState gameState = gameList.get(gameId).getGameState();
        PlayerState player = gameState.getPlayerStateById(playerId);
        if(!validateAction(player, "rollLoad")) return;
        Log.that("rolling load for player ", player.getName());
        player.setAvailableLoad(Roll.from(player.getMinLoad(), player.getMaxLoad()));
        player.removeAvailableAction("rollLoad");
        player.addAvailableAction(new Action("Work a Story", "spendLoad", "story"));
    }

    @PostMapping("/{gameId}/{playerId}/action/spendLoad")
    public void actionSpendLoad(@PathVariable() long gameId, @PathVariable() long playerId, @RequestBody() long storyId) {
        GameState gameState = gameList.get(gameId).getGameState();
        PlayerState player = gameState.getPlayerStateById(playerId);
        if(!validateAction(player, "spendLoad")) return;
        Log.that( player.getName(), " works on story #", Long.toString(storyId));
        StoryState story = gameState.getStoryStateById(storyId);
        int availLoad = player.getAvailableLoad();
        int roleDenominator = 2;
        switch(story.getProgress()) {
            case "dev":
                if(player.getRole() == PlayerRole.DEV) roleDenominator = 1;
                int worked = availLoad/roleDenominator;
                story.dev -= worked > story.dev ? story.dev : worked;
                player.setAvailableLoad(0);
                break;
            case "cr":
                story.cr -= 1;
                player.setAvailableLoad(availLoad - 1);
                break;
            case "qa":
                if(player.getRole() == PlayerRole.QA) roleDenominator = 1;
                worked = availLoad/roleDenominator;
                story.qa -= worked > story.qa ? story.qa : worked;
                player.setAvailableLoad(0);
                break;
            default:
                Log.that("story #", Long.toString(storyId), " is complete");
        }
        story.inProgress = true;
        if(player.getAvailableLoad() <= 0) player.removeAvailableAction("spendLoad");
        if(!gameState.anyAvailablePlayerActions()) gameState.beginNextTurn();
    }

    private boolean validateAction(PlayerState player, String forAction) {
        return player.getAvailableActions().stream().anyMatch((action) -> action.action.equals(forAction));
    }
    //endregion
}