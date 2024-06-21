package pl.ekoreo.worldeserver.games.wordle;

import org.springframework.web.socket.WebSocketSession;
import pl.ekoreo.worldeserver.games.Player;

public class WordlePlayer extends Player {
    public WordlePlayer(WebSocketSession session, String nickname) {
        super(session, nickname);
    }
}
