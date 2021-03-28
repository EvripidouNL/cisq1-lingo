package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.domain.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainerServiceTest {
    @ParameterizedTest
    @DisplayName("requests a game by id from the repository")
    @MethodSource("gameByIdExamples")
    void findGameById(Long id, Game game) {
        SpringGameRepository gameRepository = mock(SpringGameRepository.class);
        WordService wordService = mock(WordService.class);

        when(gameRepository.findById(id))
                .thenReturn(Optional.of(new Game(0, new ArrayList<>())));

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        String result = trainerService.findById(id).toString();

        assertEquals(game.toString(), result);
    }

    static Stream<Arguments> gameByIdExamples() {
        return Stream.of(
                Arguments.of(1L, new Game(0, new ArrayList<>())),
                Arguments.of(2L, new Game(0, new ArrayList<>())),
                Arguments.of(3L, new Game(0, new ArrayList<>()))
        );
    }

    @Test
    @DisplayName("throws exception if game is not found")
    void canNotFindGame() {
        SpringGameRepository gameRepository = mock(SpringGameRepository.class);
        WordService wordService = mock(WordService.class);

        when(gameRepository.findById(anyLong())).thenReturn(Optional.empty());

        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        assertThrows(
                GameNotFoundException.class,
                () -> trainerService.findById(0L)
        );
    }

    @Test
    @DisplayName("starting a new game")
    void newGame() {
        WordService wordService = mock(WordService.class);
        SpringGameRepository gameRepository = mock(SpringGameRepository.class);
        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        when(wordService.provideRandomWord(5))
                .thenReturn("woord");

        GameDTO gameDTO = trainerService.startNewGame();

        List<Character> expectedHint = List.of('w', '.', '.', '.', '.');

        assertEquals(0, gameDTO.getScore());
        assertEquals(1, gameDTO.getRoundNumber());
        assertEquals(5, gameDTO.getAttemptsLeft());
        assertEquals(0, gameDTO.getFeedbacks().size());
        assertEquals(expectedHint, gameDTO.getHint().getCharacterList());
    }

    @Test
    @DisplayName("starting a new round")
    void startNewRound() {
        WordService wordService = mock(WordService.class);
        SpringGameRepository gameRepository = mock(SpringGameRepository.class);
        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        Game game = new Game(0, new ArrayList<>());
        String randomword = "woord";
        Word word = new Word(randomword);
        game.newRound(word);

        when(gameRepository.findById(anyLong()))
                .thenReturn(Optional.of(game));
        when(wordService.provideRandomWord(game.totalRounds()))
                .thenReturn("gechat");

        GameDTO gameDTO = trainerService.newRound(0L);

        List<Character> expectedHint = List.of('g', '.', '.', '.', '.', '.');

        assertEquals(0, gameDTO.getScore());
        assertEquals(2, gameDTO.getRoundNumber());
        assertEquals(5, gameDTO.getAttemptsLeft());
        assertEquals(0, gameDTO.getFeedbacks().size());
        assertEquals(expectedHint, gameDTO.getHint().getCharacterList());
        assertEquals(6, game.lastRound().getWord().getLength());
    }

    @Test
    @DisplayName("make a guess on a lingo game")
    void makeGuess() {
        WordService wordService = mock(WordService.class);
        SpringGameRepository gameRepository = mock(SpringGameRepository.class);
        TrainerService trainerService = new TrainerService(wordService, gameRepository);

        Game game = new Game(0, new ArrayList<>());
        String randomword = "woord";
        Word word = new Word(randomword);
        game.newRound(word);

        when(gameRepository.findById(0L))
                .thenReturn(Optional.of(game));

        GameDTO gameDTO = trainerService.makeGuess(0L, "moord");

        List<Character> expectedHint = List.of('.', 'o', 'o', 'r', 'd');

        assertEquals(0, gameDTO.getScore());
        assertEquals(1, gameDTO.getRoundNumber());
        assertEquals(4, gameDTO.getAttemptsLeft());
        assertEquals(1, gameDTO.getFeedbacks().size());
        assertEquals(expectedHint, gameDTO.getHint().getCharacterList());
        assertEquals(5, game.lastRound().getWord().getLength());
    }
}