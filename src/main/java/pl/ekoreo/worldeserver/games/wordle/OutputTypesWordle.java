package pl.ekoreo.worldeserver.games.wordle;

import pl.ekoreo.worldeserver.enums.OutputTypes;

public enum OutputTypesWordle{
    LOBBY("LOBBY"),
    WAITING_FOR_INPUT("WAITING_FOR_INPUT"),
    WAITING_FOR_OTHERS("WAITING_FOR_OTHERS"),
    PLAYER_DATA("PLAYER_DATA"),
    END("END");


    public final String value;
    OutputTypesWordle(String value){
        this.value = value;
    }
}
