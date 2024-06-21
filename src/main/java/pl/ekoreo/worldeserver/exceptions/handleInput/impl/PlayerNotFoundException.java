package pl.ekoreo.worldeserver.exceptions.handleInput.impl;

import pl.ekoreo.worldeserver.exceptions.handleInput.HandleInputException;

public class PlayerNotFoundException extends HandleInputException {
    public PlayerNotFoundException() {
        super("Player not found");
    }
}
