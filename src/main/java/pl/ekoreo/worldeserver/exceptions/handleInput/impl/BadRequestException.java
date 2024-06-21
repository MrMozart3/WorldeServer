package pl.ekoreo.worldeserver.exceptions.handleInput.impl;

import pl.ekoreo.worldeserver.exceptions.handleInput.HandleInputException;

public class BadRequestException extends HandleInputException {
    public BadRequestException(){
        super("Bad request");
    }
}
