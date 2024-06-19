package pl.ekoreo.worldeserver;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private int maxPlayers;
    private List<Player> players = new ArrayList<>();
    Game(int maxPlayers, WebSocketSession creatorSession){
        try {
            creatorSession.sendMessage(new TextMessage("Game created"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean AddPlayer(WebSocketSession session, String name){
        if(players.size() < maxPlayers){
            players.add(new Player(session, name));
            return true;
        }
        return false;
    }
}
