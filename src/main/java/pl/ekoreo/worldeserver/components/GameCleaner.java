package pl.ekoreo.worldeserver.components;

import pl.ekoreo.worldeserver.games.Game;
import pl.ekoreo.worldeserver.services.GameManager;

import java.util.ArrayList;

public class GameCleaner extends Thread{
    GameManager gameManager;
    public GameCleaner(GameManager gameManager){
        this.gameManager = gameManager;
    }


    @Override
    public void run(){
        while(true){
            try {
                Thread.sleep(5000);
                ArrayList<String> gameIds = new ArrayList<>();
                for(Game<?> game : gameManager.getGames().values()){
                    if(game.getPlayers().size() == 0 || game.getHostId() == null){
                        gameIds.add(game.getGameId());
                    }
                }
                Thread.sleep(5000);
                for(Game<?> game : gameManager.getGames().values()){
                    if(!(game.getPlayers().size() == 0 || game.getHostId() == null)){
                        gameIds.remove(game.getGameId());
                    }
                }
                gameManager.CleanGames(gameIds);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
