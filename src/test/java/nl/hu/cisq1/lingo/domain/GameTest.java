package nl.hu.cisq1.lingo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

/*
    @ParameterizedTest(name = "Test #{index} | {0} | {1} | {2} " )
    @DisplayName("score based on attempts in round")
    @MethodSource("provideAttemptExamples")
    void calculateScore(String word, List<Feedback> feedbacks, int score) {
        Game game = new Game(0, new ArrayList<>());

        game.newRound(new Word(word));


        game.calculateScore();

        assertEquals(score, game.getScore());
    }

    private static Stream<Arguments> provideAttemptExamples() {
        return Stream.of(
                Arguments.of("woord",  25),
                Arguments.of("woord", 2, 20),
                Arguments.of("woord", 3, 15),
                Arguments.of("woord", 4, 10),
                Arguments.of("woord", 5, 5)
        );
    }

 */

    @Test
    @DisplayName("add one new round to game")
    void newRound() {
        Game game = new Game(0, new ArrayList<>());

        game.newRound(new Word("woord"));

        assertEquals(1, game.getRounds().size());
    }

    @Test
    @DisplayName("last Feedback of round")
    void lastRound() {
        Game game = new Game(0, new ArrayList<>());
        String roundWord = "woord";
        Word word = new Word(roundWord);

        Round round = game.newRound(word);

        round.guessWord("moord");

        assertEquals(round, game.lastRound());
    }
}