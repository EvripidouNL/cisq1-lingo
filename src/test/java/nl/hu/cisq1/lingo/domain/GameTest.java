package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.CannotStartNewRoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
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

    @ParameterizedTest(name = "Test #{index} | {0} | {1} | {2} " )
    @MethodSource("provideGuessExamples")
    @DisplayName("calculate score based on attempts and show game status")
    void calculateScoreAndGiveStatusWhenWordIsGuessed(int attempts, Status status, int score) {
        Word actualWord = new Word("woord");
        Game game = new Game(0, new ArrayList<>(), Status.WAITING_FOR_ROUND);

        Round round = game.newRound(actualWord);

        for (int i = 1; i < attempts; i++) {
            round.guessWord("moord");
        }

        round.guessWord("woord");

        game.calculateScoreAndGiveStatus();

        assertEquals(score, game.getScore());
        assertEquals(status, game.getStatus());
    }

    private static Stream<Arguments> provideGuessExamples() {
        return Stream.of(
                Arguments.of(1, Status.WAITING_FOR_ROUND, 25),
                Arguments.of(2, Status.WAITING_FOR_ROUND, 20),
                Arguments.of(3, Status.WAITING_FOR_ROUND, 15),
                Arguments.of(4, Status.WAITING_FOR_ROUND, 10),
                Arguments.of(5, Status.WAITING_FOR_ROUND, 5)
        );
    }

    @Test
    @DisplayName("calculate score when word is not guessed and give game status")
    void calculateScoreAndGiveStatusWhenWordIsNotGuessed() {
        Word actualWord = new Word("woord");
        Game game = new Game(0, new ArrayList<>(), Status.WAITING_FOR_ROUND);

        Round round = game.newRound(actualWord);

        round.setAttempts(4);

        round.guessWord("moord");

        game.calculateScoreAndGiveStatus();

        assertEquals(5, game.getScore());
        assertEquals("GAME_ENDED", game.getStatus().toString());
    }

    @Test
    @DisplayName("add one new round to game when the word is guessed")
    void newRound() {
        game.lastRound().guessWord("woord");
        game.calculateScoreAndGiveStatus();

        Word word = new Word("oranje");
        game.newRound(word);

        assertEquals(2, game.getRounds().size());
    }

    @Test
    @DisplayName("exception: cannot add new round to game when the word is not guessed")
    void cannotStartNewRound() {
        game.lastRound().guessWord("soort");

        game.calculateScoreAndGiveStatus();

        Word newRoundWord = new Word("oranje");

        assertThrows(CannotStartNewRoundException.class, () -> {
            game.newRound(newRoundWord);
        });
    }

    @Test
    @DisplayName("make a new round and check if it's the last round of the game")
    void lastRound() {
        assertEquals(newRound, game.lastRound());
    }
}