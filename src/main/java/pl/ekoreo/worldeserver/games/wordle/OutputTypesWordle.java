package pl.ekoreo.worldeserver.games.wordle;

import pl.ekoreo.worldeserver.enums.OutputTypes;

public enum OutputTypesWordle{
    LOBBY("LOBBY"),
    WAITING_FOR_INPUT("WAITING_FOR_INPUT"),
    NEW_BOARD("NEW_BOARD"),
    END("END");


    public final String value;
    OutputTypesWordle(String value){
        this.value = value;
    }
}
