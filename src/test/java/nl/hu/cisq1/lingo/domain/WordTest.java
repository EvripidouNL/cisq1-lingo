package nl.hu.cisq1.lingo.domain;

import nl.hu.cisq1.lingo.domain.exception.RoundAttemptLimitException;
import nl.hu.cisq1.lingo.domain.exception.WordLengthNotSupportedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WordTest {

    @Test
    @DisplayName("length is based on given word")
    void lengthBasedOnWord() {
        Word word = new Word("woord");
        int length = word.getLength();
        assertEquals(5, length);
    }

    @Test
    @DisplayName("exception: word must be between 5 or 7 letters!")
    void wordLengthNotSupported() {
        Word word = new Word("gebakjes");
        int length = word.getLength();

        assertThrows(WordLengthNotSupportedException.class, () -> {

        });
    }
}