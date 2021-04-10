package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.GameEndedException;
import nl.hu.cisq1.lingo.domain.exception.WordAlreadyGuessedException;
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

class RoundTest {
    private Round round;

    @BeforeEach
    public void init() {
        Word word = new Word("woord");

        round = new Round(word,  new ArrayList<>());
    }

    @Test
    @DisplayName("start a new round and give a hint")
    void showFirstLetterOfWord() {
        assertEquals(new Hint(List.of('w', '.', '.', '.', '.')), round.startRound());
    }

    @ParameterizedTest(name = "Test #{index} | {0} | {1} | {2} " )
    @DisplayName("compare the guess to the word and give a hint")
    @MethodSource("provideGuessExamples")
    void guessWord(String word, String attempt, Hint hint) {
        Round round = new Round(new Word(word), new ArrayList<>());

        round.startRound();
        Feedback feedback = round.guessWord(attempt);

        assertEquals(hint, feedback.giveHint());
    }

    private static Stream<Arguments> provideGuessExamples() {
        return Stream.of(
                Arguments.of("woord", "woord", new Hint(List.of('w', 'o', 'o', 'r', 'd'))),
                Arguments.of("woord", "soort", new Hint(List.of('.', 'o', 'o', 'r', '.'))),
                Arguments.of("woord", "breuk", new Hint(List.of('.', '.', '.', '.', '.')))
        );
    }

    @Test
    @DisplayName("exception: the attempt limit is reached! The game is ended!")
    void attemptLimitReached() {
        round.setAttempts(5);

        assertThrows(GameEndedException.class, () -> {
            round.guessWord("soort");
        });
    }

    @Test
    @DisplayName("attempt not same as word length")
    void attemptNotSameAsWordLength() {
        round.guessWord("kort");
        assertNotEquals(round.lastFeedback().getAttempt().length(), round.getWord().getLength());
    }

    @Test
    @DisplayName("make a guess and check if the feedback is the same as the last of the round")
    void lastFeedback() {
        Feedback feedback = round.guessWord("moord");
        assertEquals(feedback, round.lastFeedback());
    }

    @Test
    @DisplayName("exception: word is already guessed")
    void wordAlreadyGuessed() {
        round.guessWord("woord");
        assertThrows(WordAlreadyGuessedException.class, () -> {
            round.guessWord("soort");
        });
    }
}