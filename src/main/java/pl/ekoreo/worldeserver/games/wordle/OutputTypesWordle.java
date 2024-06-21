package pl.ekoreo.worldeserver.games.wordle;

import pl.ekoreo.worldeserver.enums.OutputTypes;

public enum OutputTypesWordle{
    LOBBY("LOBBY");

    public final String value;
    OutputTypesWordle(String value){
        this.value = value;
    }
}
