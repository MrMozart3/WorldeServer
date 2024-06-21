package pl.ekoreo.worldeserver.games.wordle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;
import pl.ekoreo.worldeserver.games.Game;
import java.util.Vector;

public class WordleGame extends Game<WordlePlayer> implements Runnable{
    public WordleGame(String gameId, int maxPlayers) {
        super(gameId, maxPlayers);
    }
    @Override
    public synchronized boolean AddPlayer(WebSocketSession session, String nickname) {
        if(getPlayers().size() < getMaxPlayers() && !isGameStarted()){
            getPlayers().add(new WordlePlayer(session, nickname));
            if(getPlayers().size() == 1){
                setHostId(session.getId());
            }

            //test message to all
            SendToAll(getCurrentLobbyJSON().toString());

            System.out.println("Player " + nickname + " has joined the game" + getGameId());
            return true;
        }
        return false;
    }
    @Override
    public synchronized boolean RemovePlayer(String sessionId) {
        for(WordlePlayer player : getPlayers()){
            if(player.getSession().getId().equals(sessionId)){
                /*check if player is host(add later)*/

                getPlayers().remove(player);

                //test message to all
                SendToAll(getCurrentLobbyJSON().toString());

                System.out.println("Player " + sessionId + " has left the game " + getGameId());
                return true;
            }
        }
        return false;
    }

    @Override
    public void StartGame() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void SendToAll(String message) {
        for(WordlePlayer player : getPlayers()){
            player.sendText(message);
        }
    }

    @Override
    public void SendToOne(WordlePlayer player, String message) {
        player.sendText(message);
    }

    @Override
    public void run() {

    }

    public JSONObject getCurrentLobbyJSON(){
        JSONObject json = new JSONObject();
        json.put("type", OutputTypesWordle.LOBBY.value);
        json.put("data", WordleUtils.sendLobbyJsonObject(getPlayers(), getHostId()));
        return json;
    }
}
