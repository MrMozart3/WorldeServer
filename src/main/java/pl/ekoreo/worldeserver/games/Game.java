package pl.ekoreo.worldeserver.games;

import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.Vector;

@Data
public abstract class Game<PlayerType>{
    //data about game
    String gameId;
    int gameType;
    //data about players
    int maxPlayers;
    Vector<PlayerType> players = new Vector<>();
    String hostId;
    //data about game state
    boolean gameStarted;
    public Game(String gameId, int maxPlayers){
        this.gameId = gameId;
        this.maxPlayers = maxPlayers;
        gameStarted = false;
    }
    /**
     * Add player to the game
     * @param session WebSocketSession
     * @param nickname player's nickname
     * @return true if player was added, false if not(Either game is full or already started)
     */
    public abstract boolean AddPlayer(WebSocketSession session, String nickname);
    /** Remove player from the game
     * @param sessionId session id of player to remove
     * @return true if player was removed, false if not
     */
    public abstract boolean RemovePlayer(String sessionId);
    /**
     * Start the game (create new thread)
     */
    public abstract void StartGame();
    /**
     * Send message to all players
     * @param message message to send
     */
    public abstract void SendToAll(String message);
    /**
     * Send message to one player
     * @param player Player Object
     * @param message message to send
     */
    public abstract void SendToOne(PlayerType player, String message);
}
