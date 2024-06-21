package pl.ekoreo.worldeserver.handlers;

import org.springframework.stereotype.Repository;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pl.ekoreo.worldeserver.games.Game;
import pl.ekoreo.worldeserver.services.GameManager;

@Repository
public class GameHandler extends TextWebSocketHandler{
    GameManager gameManager;

    public GameHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String game_id = session.getAttributes().get("game_id").toString();
        String nickname = session.getAttributes().get("nickname").toString();

        Game<?> game = gameManager.getGame(game_id);

        if(game == null){
            session.sendMessage(new TextMessage("Game does not exist"));
            session.close();
            return;
        }
        if(!game.AddPlayer(session, nickname)){
            session.sendMessage(new TextMessage("Game is full"));
            session.close();
            return;
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        /*
        Game game = gameManager.findPlayerGame(session.getId());
        if (game == null) {
            session.sendMessage(new TextMessage("You are not in a game"));
            return;
        }
        boolean isHost = game.getHostId().equals(session.getId());
        if(message.getPayload().equals("START GAME") && isHost){
            game.StartGame();
        }*/
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        /*
        Game game = gameManager.findPlayerGame(session.getId());
        if(game != null){
            game.RemovePlayer(session.getId());
        }*/
    }
}
