package nl.hu.cisq1.lingo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordisGuessed() {
        Feedback feedback = new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));

        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not guessed if one or more letters are incorrect")
    void wordisNotGuessed() {
        Feedback feedback = new Feedback("woord", List.of(Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.PRESENT));

        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("guess is invalid")
    void guessIsInvalid() {
        Feedback feedback = new Feedback("hetzelfde", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));

        assertTrue(feedback.guessIsInvalid());
    }

    @Test
    @DisplayName("guess is not invalid")
    void guessIsNotInvalid() {
        Feedback feedback = new Feedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.PRESENT));

        assertFalse(feedback.guessIsInvalid());
    }

    @ParameterizedTest(name = "Test #{index} | {0} | {1} | {2} " )
    @DisplayName("a hint by a guessed word")
    @MethodSource("provideHintExamples")
    void giveHint(String attempt, List<Mark> marks, Hint hint) {
        Feedback feedback = new Feedback(attempt, marks);

        assertEquals(hint, feedback.giveHint());
    }

    private static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT), new Hint(List.of('w', 'o', 'o', 'r', 'd'))),
                Arguments.of("woord", List.of(Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.ABSENT, Mark.PRESENT), new Hint(List.of('w', '.', 'o', '.', '.'))),
                Arguments.of("woord", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID), new Hint(List.of('.', '.', '.', '.', '.')))
        );
    }
}