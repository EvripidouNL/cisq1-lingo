package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Status;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.domain.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import org.junit.jupiter.api.BeforeEach;
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
    private Game game;
    private Word word;

    private SpringGameRepository gameRepository;
    private WordService wordService;
    private TrainerService trainerService;

    private GameDTO gameDTO;

    @BeforeEach
    public void init() {
        game = new Game(0, new ArrayList<>(), Status.WAITING_FOR_ROUND);
        word = new Word("woord");
        game.newRound(word);

        gameRepository = mock(SpringGameRepository.class);
        wordService = mock(WordService.class);
        trainerService = new TrainerService(wordService, gameRepository);

        when(wordService.provideRandomWord(5))
                .thenReturn("woord");
        when(gameRepository.findById(anyLong()))
                .thenReturn(Optional.of(game));
        when(wordService.provideRandomWord(game.wordLengthBasedOnRounds()))
                .thenReturn("gechat");
        when(gameRepository.findById(0L))
                .thenReturn(Optional.of(game));
    }

    @ParameterizedTest
    @DisplayName("find a game by id")
    @MethodSource("gameByIdExamples")
    void findGameById(Long id, Game game) {
        when(gameRepository.findById(id))
                .thenReturn(Optional.of(new Game(0, new ArrayList<>(), Status.WAITING_FOR_ROUND)));

        String result = trainerService.findById(id).toString();

        assertEquals(game.toString(), result);
    }

    static Stream<Arguments> gameByIdExamples() {
        return Stream.of(
                Arguments.of(1L, new Game(0, new ArrayList<>(), Status.WAITING_FOR_ROUND)),
                Arguments.of(2L, new Game(0, new ArrayList<>(), Status.WAITING_FOR_ROUND)),
                Arguments.of(3L, new Game(0, new ArrayList<>(), Status.WAITING_FOR_ROUND))
        );
    }

    @Test
    @DisplayName("exception: the game cannot be found")
    void canNotFindGame() {
        when(gameRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(
                GameNotFoundException.class,
                () -> trainerService.findById(0L)
        );
    }

    @Test
    @DisplayName("create a new game")
    void newGame() {
        gameDTO = trainerService.startNewGame();

        List<Character> expectedHint = List.of('w', '.', '.', '.', '.');

        assertEquals(0, gameDTO.getScore());
        assertEquals(1, gameDTO.getRoundNumber());
        assertEquals(5, gameDTO.getAttemptsLeft());
        assertEquals("PLAYING", gameDTO.getStatus().toString());
        assertNull(gameDTO.getFeedback());
        assertEquals(expectedHint, gameDTO.getHint().getCharacterList());
    }

    @Test
    @DisplayName("create a extra round in a game")
    void startNewRound() {
        trainerService.makeGuess(0L, "woord");

        gameDTO = trainerService.newRound(0L);

        List<Character> expectedHint = List.of('g', '.', '.', '.', '.', '.');

        assertEquals(25, gameDTO.getScore());
        assertEquals(2, gameDTO.getRoundNumber());
        assertEquals(5, gameDTO.getAttemptsLeft());
        assertEquals("PLAYING", gameDTO.getStatus().toString());
        assertNull(gameDTO.getFeedback());
        assertEquals(expectedHint, gameDTO.getHint().getCharacterList());
        assertEquals(6, game.lastRound().getWord().getLength());
    }

    @Test
    @DisplayName("make a guess on a game")
    void makeGuess() {
        gameDTO = trainerService.makeGuess(0L, "moord");

        List<Character> expectedHint = List.of('.', 'o', 'o', 'r', 'd');

        assertEquals(0, gameDTO.getScore());
        assertEquals(1, gameDTO.getRoundNumber());
        assertEquals(4, gameDTO.getAttemptsLeft());
        assertEquals("PLAYING", gameDTO.getStatus().toString());
        assertEquals(expectedHint, gameDTO.getFeedback().giveHint().getCharacterList());
        assertEquals(5, game.lastRound().getWord().getLength());
    }
}