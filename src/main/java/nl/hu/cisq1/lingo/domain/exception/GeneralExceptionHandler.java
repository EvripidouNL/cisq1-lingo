package nl.hu.cisq1.lingo.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler({GameNotFoundException.class, WordLengthNotSupportedException.class})
    protected ResponseEntity<Object> handleNotFoundException(Exception exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CannotStartNewRoundException.class, RoundAttemptLimitException.class, WordAlreadyGuessedException.class})
    protected ResponseEntity<Object> handleConflictException(Exception exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}