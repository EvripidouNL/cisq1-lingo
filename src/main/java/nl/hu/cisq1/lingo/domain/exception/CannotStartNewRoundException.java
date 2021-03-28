package nl.hu.cisq1.lingo.domain.exception;

import nl.hu.cisq1.lingo.domain.Status;

public class CannotStartNewRoundException extends RuntimeException {
    public CannotStartNewRoundException(Status status) {
        super("The word is not guessed so a new round is not available! " + "game status: " + status);
    }
}