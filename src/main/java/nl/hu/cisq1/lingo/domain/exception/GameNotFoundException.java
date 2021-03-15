package nl.hu.cisq1.lingo.domain.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Long id) {
        super("Could not find Game with this ID " + id);
    }
}