package pl.ekoreo.worldeserver.exceptions.handleInput.impl;

import pl.ekoreo.worldeserver.exceptions.handleInput.HandleInputException;

public class NotAHostException extends HandleInputException {
    public NotAHostException() {
        super("Not a host");
    }
}
