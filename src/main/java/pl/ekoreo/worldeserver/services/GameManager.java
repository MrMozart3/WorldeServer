package pl.ekoreo.worldeserver.services;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;
import pl.ekoreo.worldeserver.components.GameCleaner;
import pl.ekoreo.worldeserver.enums.GameTypes;
import pl.ekoreo.worldeserver.exceptions.ToManyRunningGamesException;
import pl.ekoreo.worldeserver.games.Game;
import pl.ekoreo.worldeserver.games.Player;
import pl.ekoreo.worldeserver.games.wordle.WordleGame;

@Data
public class GameManager {
    private GameCleaner gameCleaner;
    private int maxGames;
    private final ConcurrentHashMap<String, Game<? extends Player>> games = new ConcurrentHashMap<>();

    public GameManager(int maxGames, boolean cleanGames){
        this.maxGames = maxGames;
        if(cleanGames) {
            gameCleaner = new GameCleaner(this);
            gameCleaner.start();
        } else gameCleaner = null;
    }

    public String CreateGame(GameTypes gameType) throws ToManyRunningGamesException {
        if(games.size() < maxGames){
            String gameId = UUID.randomUUID().toString();
            switch (gameType){
                case WORDLE:
                    games.put(gameId, new WordleGame(gameId, gameType.maxPlayers));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid game type");
            }

            System.out.println("Game " + gameId +":" + gameType.value + " has been created");
            //later should return game object in json
            return gameId;
        }
        throw new ToManyRunningGamesException("To many running games");
    }
    public Game<?> getGame(String gameId){
        return games.get(gameId);
    }
    /*
    public Game findPlayerGame(String sessionId){
        for(Game game : games.values()){
            if(game.PlayerExists(sessionId)){
                return game;
            }
        }
        return null;
    }*/
    public void CleanGames(ArrayList<String> ids){
        for(String id : ids){
            games.remove(id);
            System.out.println("Game " + id + " has been removed");
        }
    }
}
