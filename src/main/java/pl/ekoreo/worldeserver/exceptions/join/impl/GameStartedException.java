package pl.ekoreo.worldeserver.exceptions.join.impl;

import pl.ekoreo.worldeserver.exceptions.join.JoinGameException;

public class GameStartedException extends JoinGameException {
    public GameStartedException(String gameId) {
        super("Game " + gameId + " has already started");
    }
}
