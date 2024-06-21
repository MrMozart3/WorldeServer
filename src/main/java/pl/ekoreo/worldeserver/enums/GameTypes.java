package pl.ekoreo.worldeserver.enums;

public enum GameTypes {
    WORDLE("WORDLE", 5);
    public final String value;
    public final int maxPlayers;
    GameTypes(String value, int maxPlayers){
        this.value = value;
        this.maxPlayers = maxPlayers;
    }
}
