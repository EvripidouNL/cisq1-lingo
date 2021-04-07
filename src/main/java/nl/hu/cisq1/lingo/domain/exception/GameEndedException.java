package nl.hu.cisq1.lingo.domain.exception;

import nl.hu.cisq1.lingo.domain.Status;

public class GameEndedException extends RuntimeException {
    public GameEndedException(Status status) {
        super("The attempt limit is reached! The game is ended! " + "game status: " + status);
    }
}