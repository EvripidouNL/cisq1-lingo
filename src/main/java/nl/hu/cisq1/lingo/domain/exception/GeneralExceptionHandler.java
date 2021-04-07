package nl.hu.cisq1.lingo.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler({GameNotFoundException.class, WordLengthNotSupportedException.class})
    protected ResponseEntity<Object> handleNotFoundException(Exception exception) {
        Map<String, Object> body = new HashMap<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        body.put("type", exception.getClass().getSimpleName());
        body.put("timestamp", localDateTime);
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CannotStartNewRoundException.class, GameEndedException.class, WordAlreadyGuessedException.class})
    protected ResponseEntity<Object> handleConflictException(Exception exception) {
        Map<String, Object> body = new HashMap<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        body.put("type", exception.getClass().getSimpleName());
        body.put("timestamp", localDateTime);
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}