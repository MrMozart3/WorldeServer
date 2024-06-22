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
import pl.ekoreo.worldeserver.games.wordle.WordleUtils;
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
                try {
                    iterator.remove();
                } catch (Exception e) {
                    System.out.println("COULD NOT REMOVE PLAYER");
                }


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
        JSONObject inputData;

        try{
            type = InputTypesWordle.valueOf(message.getString("type"));
            inputData = message.getJSONObject("data");
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
                    rounds = inputData.getInt("rounds");
                    timePerRound = inputData.getInt("timePerRound");
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
                    player.AddAnswer(player.getCurrentRow(), inputData.getString("word").toUpperCase().toCharArray());
                } catch (JSONException e){
                    throw new BadRequestException();
                }

                SendPlayersData();

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
        for (int round = 0; round < rounds; round++) {
            String answer = WordleUtils.getRandomWord();
            for (WordlePlayer p : getPlayers()) {
                p.ResetBoard();
                p.setAnswer(answer.toCharArray());
            }

            for (int line = 0; line < 6; line++) {
                for (WordlePlayer p : getPlayers()) {
                    p.setSentWord(false);
                    p.setCurrentRow(line);
                }
                SendPlayersData();
                for (int time = timePerRound; time >= 0; time--) {
                    try {
                        boolean skip = true;
                        for (WordlePlayer p : getPlayers()) {
                            if (!p.isSentWord() && !p.isGuessedWord()) {
                                SendToOne(p, WordleUtils.GenerateWaitingForInputJson(round, line, time).toString());
                                skip = false;
                            } else {
                                SendToOne(p, WordleUtils.GenerateWaitingForOthersJson(round, line).toString());
                            }
                            //TODO send all other players wordle's
                        }
                        if (skip) {
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

    public void SendPlayersData(){
        for(int i = 0; i < getPlayers().size(); i++){
            JSONObject json = new JSONObject();

            json.put("type", OutputTypesWordle.PLAYER_DATA.value);
            JSONArray data = new JSONArray();
            for(int j = 0; j < getPlayers().size(); j++){
                WordlePlayer p = getPlayers().get(j);

                JSONObject playerData = new JSONObject();
                playerData.put("player_id", j);
                playerData.put("nickname", p.getNickname());
                playerData.put("is_host", p.getSession().getId().equals(getHostId()));
                if(i == j){
                    playerData.put("me", true);
                    playerData.put("board", WordleUtils.GenerateWordleBoardJson(p.getBoard(), true));
                } else {
                    playerData.put("me", false);
                    playerData.put("board", WordleUtils.GenerateWordleBoardJson(p.getBoard(), getPlayers().get(i).isGuessedWord()));
                }
                data.put(playerData);
            }
            json.put("data", data);
            getPlayers().get(i).sendText(json.toString());
        }
    }
}
