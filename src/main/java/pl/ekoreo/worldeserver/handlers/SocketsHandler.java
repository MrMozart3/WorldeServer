package pl.ekoreo.worldeserver.handlers;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pl.ekoreo.worldeserver.enums.OutputTypes;
import pl.ekoreo.worldeserver.exceptions.handleInput.HandleInputException;
import pl.ekoreo.worldeserver.exceptions.join.JoinGameException;
import pl.ekoreo.worldeserver.games.Game;
import pl.ekoreo.worldeserver.games.wordle.WordleBoard;
import pl.ekoreo.worldeserver.services.GameManager;
import pl.ekoreo.worldeserver.utils.JsonUtils;

@Repository
public class SocketsHandler extends TextWebSocketHandler{
    GameManager gameManager;

    public SocketsHandler(GameManager gameManager) {
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
        try{
            game.AddPlayer(session, nickname);
        } catch (JoinGameException e){
            session.sendMessage(new TextMessage(JsonUtils.createJsonSingleMessage(OutputTypes.ERROR.value, e.getMessage()).toString()));
            session.close();
            return;
        }
    }
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject json;
        try{
            json = new JSONObject(message.getPayload());
        } catch (JSONException e){
            session.sendMessage(new TextMessage(JsonUtils.createJsonSingleMessage(OutputTypes.ERROR.value, "Invalid JSON REQUEST").toString()));
            return;
        }

        Game<?> game = gameManager.getGameBySession(session);

        if(game == null){
            session.sendMessage(new TextMessage(JsonUtils.createJsonSingleMessage(OutputTypes.ERROR.value, "Game does not exist").toString()));
            session.close();
            return;
        }

        try{
            new JSONTokener(message.getPayload()).nextValue();
            try{
                game.HandleMessage(session, new JSONObject(message.getPayload()));
            }catch (HandleInputException e){
                session.sendMessage(new TextMessage(JsonUtils.createJsonSingleMessage(OutputTypes.ERROR.value, e.getMessage()).toString()));
            }
        }
        catch (JSONException e) {
            session.sendMessage(new TextMessage(JsonUtils.createJsonSingleMessage(OutputTypes.ERROR.value, "Invalid JSON REQUEST").toString()));
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Game<?> game = gameManager.getGameBySession(session);
        if(game != null){
            game.RemovePlayer(session.getId());
        }
    }
}
