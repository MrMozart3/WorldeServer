package pl.ekoreo.worldeserver.games.wordle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

public class WordleUtils {
    public static JSONObject sendLobbyJsonObject(Vector<WordlePlayer> players, String hostId){
        JSONObject json = new JSONObject();
        json.put("playerCount", players.size());
        JSONArray playersJSON = new JSONArray();
        for(WordlePlayer player : players){
            JSONObject playerJson = new JSONObject();
            playerJson.put("nickname", player.getNickname());
            playerJson.put("host", player.getSession().getId().equals(hostId));
            playersJSON.put(playerJson);
        }
        json.put("players", playersJSON);
        return json;
    }
}
