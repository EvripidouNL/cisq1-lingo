package nl.hu.cisq1.lingo.domain.exception;

public class WordAlreadyGuessedException extends RuntimeException {
    public WordAlreadyGuessedException() {
        super("Word is already guessed start a new round ");
    }
}