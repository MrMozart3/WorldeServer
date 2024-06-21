package pl.ekoreo.worldeserver.exceptions.handleInput.impl;

import pl.ekoreo.worldeserver.exceptions.handleInput.HandleInputException;

public class WrongTypeException extends HandleInputException {
    public WrongTypeException() {
        super("Wrong type");
    }
}
