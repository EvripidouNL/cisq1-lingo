package nl.hu.cisq1.lingo.domain;

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
    @DisplayName("score based on attempts in round")
    void calculateScore(String word, List<Feedback> feedbacks, int score) {
        Word actualWord = new Word(word);
        Game game = new Game(0, new ArrayList<>());

        game.newRound(actualWord);

        Round round = game.lastRound();

        round.setFeedbacks(feedbacks);

        game.calculateScore();

        assertEquals(score, game.getScore());
    }

    private static Stream<Arguments> provideGuessExamples() {
        return Stream.of(
                Arguments.of("woord", List.of(new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), 25),
                Arguments.of("woord", List.of(new Feedback(), new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), 20),
                Arguments.of("woord", List.of(new Feedback(), new Feedback(), new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), 15),
                Arguments.of("woord", List.of(new Feedback(), new Feedback(), new Feedback(), new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), 10),
                Arguments.of("woord", List.of(new Feedback(), new Feedback(), new Feedback(), new Feedback(), new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), 5),
                Arguments.of("woord", List.of(new Feedback(), new Feedback(), new Feedback(), new Feedback(), new Feedback(), new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), 0)
        );
    }

    @Test
    @DisplayName("add one new round to game")
    void newRound() {
        Game game = new Game(0, new ArrayList<>());

        game.newRound(new Word("woord"));

        assertEquals(1, game.getRounds().size());
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