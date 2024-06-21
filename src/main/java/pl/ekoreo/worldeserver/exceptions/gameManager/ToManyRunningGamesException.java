package pl.ekoreo.worldeserver.exceptions.gameManager;

public class ToManyRunningGamesException extends Exception{
    public ToManyRunningGamesException(String message) {
        super(message);
    }
}
