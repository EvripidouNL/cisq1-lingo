package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.RoundAttemptLimitException;
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

    @Test
    @DisplayName("start round and first letter of word is shown")
    void showFirstLetterOfWord() {
        Word word = new Word("woord");

        Round round = new Round(1, word,  new ArrayList<>());

        assertEquals(new Hint(List.of('w', '.', '.', '.', '.')), round.startRound());
    }

    @ParameterizedTest(name = "Test #{index} | {0} | {1} | {2} " )
    @DisplayName("attempt compared to word")
    @MethodSource("provideGuessExamples")
    void guessWord(String word, String attempt, Hint hint) {
        Round round = new Round(1, new Word(word), new ArrayList<>());

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
    @DisplayName("exception: the attempt limit is reached! You may not make another guess!")
    void attemptLimitReached() {
        String word = "woord";
        Round round = new Round(1, new Word(word), new ArrayList<>());

        round.setAttempt(5);

        assertThrows(RoundAttemptLimitException.class, () -> {
            round.guessWord("soort");
        });
    }
}