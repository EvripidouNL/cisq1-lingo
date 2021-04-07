package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.WordLengthNotSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WordTest {
    private int length;

    @BeforeEach
    public void init() {
        Word word = new Word("woord");
        length = word.getLength();
    }

    @Test
    @DisplayName("length is based on given word")
    void lengthBasedOnWord() {
        assertEquals(5, length);
    }

    @ParameterizedTest
    @MethodSource("provideWordExamples")
    @DisplayName("exception: the word length must be between 5 and 7 letters")
    void invalidWordLength(String word) {
        assertThrows(
                WordLengthNotSupportedException.class,
                () -> new Word(word)
        );
    }

    private static Stream<Arguments> provideWordExamples() {
        return Stream.of(
                Arguments.of("gast"),
                Arguments.of("gebakjes")
        );
    }
}