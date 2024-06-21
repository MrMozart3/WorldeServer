package pl.ekoreo.worldeserver.exceptions;

public class ToManyRunningGamesException extends Exception{
    public ToManyRunningGamesException(String message) {
        super(message);
    }
}
