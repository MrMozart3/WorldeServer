package pl.ekoreo.worldeserver.games.wordle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;
import pl.ekoreo.worldeserver.exceptions.join.JoinGameException;
import pl.ekoreo.worldeserver.exceptions.join.impl.GameFullException;
import pl.ekoreo.worldeserver.exceptions.join.impl.GameStartedException;
import pl.ekoreo.worldeserver.exceptions.join.impl.NicknameTakenException;
import pl.ekoreo.worldeserver.games.Game;
import java.util.Vector;

public class WordleGame extends Game<WordlePlayer> implements Runnable{
    public WordleGame(String gameId, int maxPlayers) {
        super(gameId, maxPlayers);
    }
    @Override
    public synchronized void AddPlayer(WebSocketSession session, String nickname) throws JoinGameException {
        if(isGameStarted()){
            throw new GameStartedException(getGameId());
        }
        if(getPlayers().size() >= getMaxPlayers()){
            throw new GameFullException(getGameId());
        }
        if(getPlayers().stream().map(WordlePlayer::getNickname).toList().contains(nickname)){
            throw new NicknameTakenException(nickname);
        }


        getPlayers().add(new WordlePlayer(session, nickname));
        if(getPlayers().size() == 1){
            setHostId(session.getId());
        }

        SendToAll(WordleUtils.GenerateLobbyJson(OutputTypesWordle.LOBBY.value, getPlayers(), getHostId()).toString());
    }
    @Override
    public synchronized void RemovePlayer(String sessionId) {
        for(WordlePlayer player : getPlayers()){
            if(player.getSession().getId().equals(sessionId)){
                //TODO check if player is host(add later)
                getPlayers().remove(player);

                System.out.println("Player " + sessionId + " has left the game " + getGameId());
            }
        }
        SendToAll(WordleUtils.GenerateLobbyJson(OutputTypesWordle.LOBBY.value, getPlayers(), getHostId()).toString());
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
        //TODO game logic
        while(true){
            try {
                Thread.sleep(1000);
                SendToAll("Game is running");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
