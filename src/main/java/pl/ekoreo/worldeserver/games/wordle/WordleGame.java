package pl.ekoreo.worldeserver.games.wordle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;
import pl.ekoreo.worldeserver.enums.OutputTypes;
import pl.ekoreo.worldeserver.exceptions.handleInput.HandleInputException;
import pl.ekoreo.worldeserver.exceptions.handleInput.impl.BadRequestException;
import pl.ekoreo.worldeserver.exceptions.handleInput.impl.NotAHostException;
import pl.ekoreo.worldeserver.exceptions.handleInput.impl.PlayerNotFoundException;
import pl.ekoreo.worldeserver.exceptions.handleInput.impl.WrongTypeException;
import pl.ekoreo.worldeserver.exceptions.join.JoinGameException;
import pl.ekoreo.worldeserver.exceptions.join.impl.GameFullException;
import pl.ekoreo.worldeserver.exceptions.join.impl.GameStartedException;
import pl.ekoreo.worldeserver.exceptions.join.impl.NicknameTakenException;
import pl.ekoreo.worldeserver.games.Game;
import pl.ekoreo.worldeserver.games.Player;
import pl.ekoreo.worldeserver.utils.JsonUtils;

import java.util.Iterator;
import java.util.Vector;

public class WordleGame extends Game<WordlePlayer> implements Runnable{
    private int rounds;
    private int timePerRound;
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

        getPlayers().add(new WordlePlayer(this, session, nickname));
        if(getPlayers().size() == 1){
            setHostId(session.getId());
        }

        SendToAll(WordleUtils.GenerateLobbyJson(getPlayers(), getHostId()).toString());
    }
    @Override
    public synchronized void RemovePlayer(String sessionId) {
        Iterator<WordlePlayer> iterator = getPlayers().iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            if(player.getSession().getId().equals(sessionId)){
                //TODO check if player is host(add later)
                iterator.remove();

                System.out.println("Player " + sessionId + " has left the game " + getGameId());
            }
        }
        SendToAll(WordleUtils.GenerateLobbyJson(getPlayers(), getHostId()).toString());
    }

    @Override
    public void StartGame() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public synchronized void SendToAll(String message) {
        for(WordlePlayer player : getPlayers()){
            player.sendText(message);
        }
    }

    @Override
    public synchronized void SendToOne(WordlePlayer player, String message) {
        player.sendText(message);
    }

    @Override
    public synchronized void HandleMessage(WebSocketSession session, JSONObject message) throws HandleInputException {
        WordlePlayer player = FindPlayerBySession(session);
        if(player == null){
            throw new PlayerNotFoundException();
        }

        InputTypesWordle type;
        JSONObject data;

        try{
            type = InputTypesWordle.valueOf(message.getString("type"));
            data = message.getJSONObject("data");
        } catch (JSONException e){
            throw new BadRequestException();
        } catch (IllegalArgumentException e){
            throw new WrongTypeException();
        }

        switch(type){
            case START_GAME:
                if(!player.getSession().getId().equals(getHostId())){
                    throw new NotAHostException();
                }

                try{
                    rounds = data.getInt("rounds");
                    timePerRound = data.getInt("timePerRound");
                    if(rounds <= 0 || rounds >= 30){
                        throw new BadRequestException();
                    }
                    if(timePerRound <= 0 || timePerRound >= 300){
                        throw new BadRequestException();
                    }
                } catch (JSONException e){
                    throw new BadRequestException();
                }
                StartGame();
                break;
            case WORD:
                if(!isGameStarted()){
                    throw new HandleInputException("Game has not started yet");
                }
                if(player.isSentWord()){
                    throw new HandleInputException("You have already sent your word");
                }
                if(player.isGuessedWord()){
                    throw new HandleInputException("You have already guessed the word");
                }
                try{
                    player.AddAnswer(player.getCurrentRow(), data.getString("word").toCharArray());
                } catch (JSONException e){
                    throw new BadRequestException();
                }
                player.sendText(WordleUtils.GenerateWordleBoardJson(player.getBoard()).toString());
                //TODO send to all players

                break;
            default:
                throw new WrongTypeException();
        }


    }
    @Override
    public synchronized WordlePlayer FindPlayerBySession(WebSocketSession session){
        for(WordlePlayer player : getPlayers()){
            if(player.getSession().getId().equals(session.getId())){
                return player;
            }
        }
        return null;
    }
    @Override
    public void run() {
        setGameStarted(true);
        for(int round = 0; round < rounds; round++){
            //TODO randomly select word
            String answer = "KUTAS";
            for(WordlePlayer p : getPlayers()){
                p.ResetBoard();
                p.setAnswer(answer.toCharArray());

            }

            for(int line = 0; line < 6; line++) {
                for(WordlePlayer p : getPlayers()){
                    p.setSentWord(false);
                    p.setCurrentRow(line);
                    p.sendText(WordleUtils.GenerateWordleBoardJson(p.getBoard()).toString());
                }
                for (int time = timePerRound; time >= 0; time--) {
                    try {
                        boolean skip = true;
                        for (WordlePlayer p : getPlayers()) {
                            if(!p.isSentWord() && !p.isGuessedWord()){
                                p.sendText(WordleUtils.GenerateWaitingForInputJson(round, line, time).toString());
                                skip = false;
                            }
                            //TODO send all other players wordles
                        }
                        if(skip) {
                            break;
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        SendToAll(JsonUtils.createErrorJsonMessage("Serveside error, shutting game down"));
                        getPlayers().clear();
                    }
                }
            }
        }
        //TODO end show leaderboard
        SendToAll(JsonUtils.createJsonSingleMessage(OutputTypesWordle.END.value, "Game has ended(TEMPORARY MESSAGE)").toString());
    }

}
