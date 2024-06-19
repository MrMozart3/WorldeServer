package pl.ekoreo.worldeserver;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.websocket.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class GameManager {
    private int maxGames = 2;
    private ConcurrentHashMap<String, Game> games = new ConcurrentHashMap<>();
    //create uuid for games later
    public boolean CreateGame(int maxPlayers, WebSocketSession creatorSession){
        if(games.size() < maxGames){
            games.put("1", new Game(maxPlayers, creatorSession));
            return true;
        }
        return false;
    }

}
