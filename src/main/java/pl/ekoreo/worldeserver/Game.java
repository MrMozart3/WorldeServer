/*package pl.ekoreo.worldeserver;

import lombok.Getter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Game extends Thread {
    @Getter
    String gameId;
    private int maxPlayers;
    private int round;

    private ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();
    Game(String gameId, int maxPlayers){
        this.gameId = gameId;
        this.maxPlayers = maxPlayers;
        this.round = -1;
    }
    public boolean AddPlayer(WebSocketSession session, String nickname){
        if(players.size() < maxPlayers){
            players.put(session.getId(), new Player(session, nickname, players.isEmpty()));
            System.out.println("Player " + nickname + " has joined the game" + gameId);
            return true;
        }
        return false;
    }
    public boolean PlayerExists(String sessionId){
        return players.get(sessionId) != null;
    }
    public void RemovePlayer(String sessionId) throws IOException {
        players.remove(sessionId);
        System.out.println("Player " + sessionId + " has left the game " + gameId);
        return;
    }
    public String getHostId(){
        for(Player player : players.values()){
            if(player.isHost()){
                return player.getSessionId();
            }
        }
        return null;
    }
    public int currentPlayers(){
        return players.size();
    }
    @Override
    public void run(){
        while(round == -1){
            try {
                Thread.sleep(500);
                String hostId = getHostId() == null ? "" : getHostId();
                String nickname = players.get(hostId) != null ? players.get(hostId).getNickname() : "";
                String message = "STATUS_LOBBY|" + nickname;
                for(Player player : players.values()){
                    message += "#" + player.getNickname() + "$" + (player.isHost() ? 1 : 0);
                }
                message+= '|';
                SendAll(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void StartGame(){
        round = 0;
    }
    public void SendAll(String text){
        for(Player player : players.values()){
            player.sendText(text);
        }
    }
}
*/