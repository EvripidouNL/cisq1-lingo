package nl.hu.cisq1.lingo.domain.exception;

public class NewRoundException extends RuntimeException {
    public NewRoundException() {
        super("The word is not guessed so a new round is not available!");
    }
}