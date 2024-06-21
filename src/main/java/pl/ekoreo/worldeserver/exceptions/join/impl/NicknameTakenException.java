package pl.ekoreo.worldeserver.exceptions.join.impl;

import pl.ekoreo.worldeserver.exceptions.join.JoinGameException;

public class NicknameTakenException extends JoinGameException {
    public NicknameTakenException(String nickname) {
        super("Nickname " + nickname + " is already taken");
    }
}
