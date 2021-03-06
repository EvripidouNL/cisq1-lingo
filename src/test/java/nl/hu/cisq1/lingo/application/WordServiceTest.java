package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.data.SpringWordRepository;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.domain.exception.WordLengthNotSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * This is a unit test.
 *
 * It tests the behaviors of our system under test,
 * WordService, in complete isolation:
 * - its methods are called by the test framework instead of a controller
 * - the WordService calls a test double instead of an actual repository
 */
class WordServiceTest {
    private SpringWordRepository wordMockRepository;
    private WordService wordService;

    @BeforeEach
    public void init() {
        wordMockRepository = mock(SpringWordRepository.class);
        wordService = new WordService(wordMockRepository);
    }

    @ParameterizedTest
    @DisplayName("requests a random word of a specified length from the repository")
    @MethodSource("randomWordExamples")
    void providesRandomWord(int wordLength, String word) {
        when(wordMockRepository.findRandomWordByLength(wordLength))
                .thenReturn(Optional.of(new Word(word)));

        String result = wordService.provideRandomWord(wordLength);

        assertEquals(word, result);
    }

    @Test
    @DisplayName("throws exception if length not supported")
    void unsupportedLength() {
        when(wordMockRepository.findRandomWordByLength(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(
                WordLengthNotSupportedException.class,
                () -> wordService.provideRandomWord(5)
        );
    }

    static Stream<Arguments> randomWordExamples() {
        return Stream.of(
                Arguments.of(5, "tower"),
                Arguments.of(6, "castle"),
                Arguments.of(7, "knights")
        );
    }
}