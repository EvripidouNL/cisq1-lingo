package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.CannotStartNewRoundException;
import nl.hu.cisq1.lingo.domain.exception.GameEndedException;
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
        game = new Game(0, new ArrayList<>(), Status.WAITING_FOR_ROUND);
        Word word = new Word("woord");
        newRound = game.newRound(word);
    }

    @ParameterizedTest
    @MethodSource("provideGuessExamples")
    @DisplayName("calculate score based on attempts and show game status")
    void calculateScoreAndGiveStatus(int attempts, Status status, int score) {
        Word actualWord = new Word("woord");
        Game game = new Game(0, new ArrayList<>(), Status.WAITING_FOR_ROUND);

        game.newRound(actualWord);

        Round round = game.lastRound();

        round.setAttempts(attempts);
        // to set the round attempts

        round.guessWord(actualWord.getValue());
        // attempts +1 by fuction guessWord

        game.calculateScoreAndGiveStatus();

        assertEquals(score, game.getScore());
        assertEquals(status, game.getStatus());
    }

    private static Stream<Arguments> provideGuessExamples() {
        return Stream.of(
                Arguments.of(0, Status.WAITING_FOR_ROUND, 25),
                Arguments.of(1, Status.WAITING_FOR_ROUND, 20),
                Arguments.of(2, Status.WAITING_FOR_ROUND, 15),
                Arguments.of(3, Status.WAITING_FOR_ROUND, 10),
                Arguments.of(4, Status.WAITING_FOR_ROUND, 5)
        );
    }

    @Test
    @DisplayName("add one new round to game when the word is guessed")
    void newRound() {
        game.lastRound().guessWord("woord");
        assertEquals(1, game.getRounds().size());
    }

    @Test
    @DisplayName("exception: the game is ended because attempts is above 5")
    void gameEnded() {
        game.lastRound().setAttempts(5);

        assertThrows(GameEndedException.class, () -> {
            game.lastRound().guessWord("moord");
        });
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