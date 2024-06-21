package pl.ekoreo.worldeserver.games.wordle;

import org.json.JSONObject;

public enum InputTypesWordle {
    START_GAME("START_GAME"),
    WORD("WORD");

    public final String value;
    InputTypesWordle(String value){
        this.value = value;
    }
}
