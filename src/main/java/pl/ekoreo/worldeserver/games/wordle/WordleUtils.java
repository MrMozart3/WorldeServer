package pl.ekoreo.worldeserver.games.wordle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

public class WordleUtils {
    public static JSONObject GenerateLobbyJson(String typeValue, Vector<WordlePlayer> players, String hostId){
        JSONObject json = new JSONObject();
        json.put("type", typeValue);

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
}
