package pl.ekoreo.worldeserver.games.wordle;

public enum OutputTypesWordle {
    LOBBY("LOBBY"),
    GAME("GAME"),
    END("END");

    public final String value;
    OutputTypesWordle(String value){
        this.value = value;
    }
}
