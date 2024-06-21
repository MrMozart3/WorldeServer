package pl.ekoreo.worldeserver.exceptions.join.impl;

import pl.ekoreo.worldeserver.exceptions.join.JoinGameException;

public class GameFullException extends JoinGameException {
    public GameFullException(String gameId) {
        super("Game " + gameId + " is full");
    }
}
