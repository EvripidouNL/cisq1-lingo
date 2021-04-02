package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.CannotStartNewRoundException;
import org.junit.jupiter.api.BeforeEach;
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
    private Game game;
    private Round newRound;

    @BeforeEach
    public void init() {
        game = new Game(0, new ArrayList<>());
        Word word = new Word("woord");
        newRound = game.newRound(word);
    }

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
                Arguments.of("woord", List.of(new Feedback(), new Feedback(), new Feedback(), new Feedback(), new Feedback(), new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), Status.WAITING_FOR_ROUND, 0),
                Arguments.of("woord", List.of(new Feedback(), new Feedback(), new Feedback(), new Feedback(), new Feedback(), new Feedback(), new Feedback("noord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))), Status.GAME_ENDED, 0)
        );
    }

    @Test
    @DisplayName("add one new round to game when the word is guessed")
    void newRound() {
        game.lastRound().guessWord("woord");
        assertEquals(1, game.getRounds().size());
    }

    @Test
    @DisplayName("cannot add new round to game when the word is not guessed")
    void cannotStartNewRound() {
        game.lastRound().guessWord("soort");

        Word newRoundWord = new Word("oranje");

        assertThrows(CannotStartNewRoundException.class, () -> {
            game.newRound(newRoundWord);
        });
    }

    @Test
    @DisplayName("check if the newround the last round is")
    void lastRound() {
        assertEquals(newRound, game.lastRound());
    }
}