package pl.ekoreo.worldeserver.games.wordle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

public class WordleUtils {
    public static JSONObject GenerateLobbyJson(Vector<WordlePlayer> players, String hostId){
        JSONObject json = new JSONObject();
        json.put("type", OutputTypesWordle.LOBBY.value);

        JSONObject data = new JSONObject();
        data.put("playerCount", players.size());
        JSONArray playersJSON = new JSONArray();
        for(WordlePlayer player : players){
            JSONObject playerJson = new JSONObject();
            playerJson.put("nickname", player.getNickname());
            playerJson.put("host", player.getSession().getId().equals(hostId));
            playersJSON.put(playerJson);
        }
        data.put("players", playersJSON);

        json.put("data", data);
        return json;
    }
    public static JSONObject GenerateWaitingForInputJson(int round, int line, int timeLeft){
        JSONObject json = new JSONObject();
        json.put("type", OutputTypesWordle.WAITING_FOR_INPUT.value);

        JSONObject data = new JSONObject();
        data.put("round", round);
        data.put("line", line);
        data.put("timeLeft", timeLeft);

        json.put("data", data);
        return json;
    }
    public static boolean VerifyWord(char[] word){
        //TODO verify word
        return true;
    }
    public static JSONObject GenerateWordleBoardJson(WordleBoard board){
        JSONObject json = new JSONObject();
        json.put("type", OutputTypesWordle.NEW_BOARD.value);
        JSONObject data = new JSONObject();
        JSONArray lines = new JSONArray();
        for(int line = 0; line < 6; line++){
            JSONArray lineJson = new JSONArray();
            for(int i = 0; i < 5; i++){
                JSONObject cell = new JSONObject();
                cell.put("letter", String.valueOf(board.getBoard()[line][i]));
                cell.put("color", String.valueOf(board.getColors()[line][i]));
                lineJson.put(cell);
            }
            lines.put(lineJson);
        }
        data.put("board", lines);
        json.put("data", data);
        return json;
    }
}
