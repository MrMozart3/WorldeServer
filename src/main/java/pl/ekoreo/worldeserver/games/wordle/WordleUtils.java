package pl.ekoreo.worldeserver.games.wordle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class WordleUtils {
    private static ArrayList<String> words = new ArrayList<String>();
    public static void ReadWords(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 5) {
                    words.add(line.toUpperCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject GenerateLobbyJson(Vector<WordlePlayer> players, String hostId) {
        JSONObject json = new JSONObject();
        json.put("type", OutputTypesWordle.LOBBY.value);

        JSONObject data = new JSONObject();
        data.put("playerCount", players.size());
        JSONArray playersJSON = new JSONArray();
        for (WordlePlayer player : players) {
            JSONObject playerJson = new JSONObject();
            playerJson.put("nickname", player.getNickname());
            playerJson.put("host", player.getSession().getId().equals(hostId));
            playersJSON.put(playerJson);
        }
        data.put("players", playersJSON);

        json.put("data", data);
        return json;
    }

    public static JSONObject GenerateWaitingForInputJson(int round, int line, int timeLeft) {
        JSONObject json = new JSONObject();
        json.put("type", OutputTypesWordle.WAITING_FOR_INPUT.value);

        JSONObject data = new JSONObject();
        data.put("round", round);
        data.put("line", line);
        data.put("timeLeft", timeLeft);

        json.put("data", data);
        return json;
    }

    public static JSONObject GenerateWaitingForOthersJson(int round, int line) {
        JSONObject json = new JSONObject();
        json.put("type", OutputTypesWordle.WAITING_FOR_OTHERS.value);

        JSONObject data = new JSONObject();
        data.put("round", round);
        data.put("line", line);

        json.put("data", data);
        return json;
    }

    public static boolean VerifyWord(char[] word) {
        if(word == null) {
            return false;
        }
        if(word.length != 5) {
            return false;
        }
        if(!OnlyPolishLetters(word)){
            return false;
        }
        return true;
    }

    public static JSONObject GeneratePlayerDataJson(WordlePlayer player) {
        JSONObject json = new JSONObject();
        json.put("type", OutputTypesWordle.WAITING_FOR_OTHERS.value);

        JSONObject data = new JSONObject();

        json.put("data", data);
        return json;
    }

    public static JSONArray GenerateWordleBoardJson(WordleBoard board, boolean showLetters) {
        JSONArray json = new JSONArray();
        for (int line = 0; line < 6; line++) {
            JSONArray lineJson = new JSONArray();
            for (int i = 0; i < 5; i++) {
                JSONObject cell = new JSONObject();
                if (showLetters) {
                    cell.put("letter", String.valueOf(board.getBoard()[line][i]));
                } else {
                    cell.put("letter", "-");
                }
                cell.put("color", String.valueOf(board.getColors()[line][i]));
                lineJson.put(cell);
            }
            json.put(lineJson);
        }
        return json;
    }
    public static boolean OnlyPolishLetters(char[] word){
        String alphabet = "AĄBCĆDEĘFGHIJKLŁMNŃOÓPQRSŚTUVWXYZŹŻ";
        for(char c : word){
            if(alphabet.indexOf(c) == -1){
                return false;
            }
        }
        return true;
    }
    public static String getRandomWord(){
        return words.get((int)(Math.random()*words.size()));
    }
}
