package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.CannotStartNewRoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @ParameterizedTest
    @MethodSource("provideGuessExamples")
    @DisplayName("calculate score based on attempts and show game status")
    void calculateScoreAndGiveStatus(String word, List<Feedback> feedbacks, Status status, int score) {
        Word actualWord = new Word(word);
        Game game = new Game(0, new ArrayList<>());

        game.newRound(actualWord);

        game.setStatus(status);

        Round round = game.lastRound();

        round.setFeedbacks(feedbacks);

        game.calculateScoreAndGiveStatus();

        assertEquals(score, game.getScore());
    }

    private static Stream<Arguments> provideGuessExamples() {
        return Stream.of(
                Arguments.of("woord", List.of(new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), Status.WAITING_FOR_ROUND, 25),
                Arguments.of("woord", List.of(new Feedback(), new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), Status.WAITING_FOR_ROUND, 20),
                Arguments.of("woord", List.of(new Feedback(), new Feedback(), new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), Status.WAITING_FOR_ROUND, 15),
                Arguments.of("woord", List.of(new Feedback(), new Feedback(), new Feedback(), new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), Status.WAITING_FOR_ROUND, 10),
                Arguments.of("woord", List.of(new Feedback(), new Feedback(), new Feedback(), new Feedback(), new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), Status.WAITING_FOR_ROUND, 5),
                Arguments.of("woord", List.of(new Feedback(), new Feedback(), new Feedback(), new Feedback(), new Feedback(), new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), Status.WAITING_FOR_ROUND, 0)
        );
    }

    @Test
    @DisplayName("add one new round to game when the word is guessed")
    void newRound() {
        Game game = new Game(0, new ArrayList<>());

        game.newRound(new Word("woord"));

        game.lastRound().guessWord("woord");

        assertEquals(1, game.getRounds().size());
    }

    @Test
    @DisplayName("cannot add new round to game when the word is not guessed")
    void cannotStartNewRound() {
        Game game = new Game(0, new ArrayList<>());

        game.newRound(new Word("woord"));

        game.lastRound().guessWord("soort");

        Word newRoundWord = new Word("oranje");

        assertThrows(CannotStartNewRoundException.class, () -> {
            game.newRound(newRoundWord);
        });
    }

    @Test
    @DisplayName("last feedback of round")
    void lastRound() {
        Game game = new Game(0, new ArrayList<>());
        String roundWord = "woord";
        Word word = new Word(roundWord);

        Round round = game.newRound(word);

        round.guessWord("moord");

        assertEquals(round, game.lastRound());
    }
}