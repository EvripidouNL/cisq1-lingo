package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.data.SpringWordRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.domain.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
class TrainerServiceIntegrationTest {
    @Autowired
    private TrainerService trainerService;

    @Autowired
    private SpringGameRepository gameRepository;

    private GameDTO gameDTONewGame;
    private Game game;

    @BeforeEach
    public void init() {
        gameDTONewGame = trainerService.startNewGame();

        game = gameRepository.getOne(gameDTONewGame.getGameId());
    }

    @Test
    @DisplayName("can not find the game with this id")
    void canNotFindGame() {
        assertThrows(GameNotFoundException.class, () -> trainerService.findById(0L));
    }

    @Test
    @DisplayName("create a new game")
    void newGame() {
        List<Character> expectedHint = List.of('p', '.', '.', '.', '.');

        assertEquals(0, gameDTONewGame.getScore());
        assertEquals(5, gameDTONewGame.getAttemptsLeft());
        assertEquals("PLAYING", gameDTONewGame.getStatus().toString());
        assertEquals(1, gameDTONewGame.getRoundNumber());
        assertEquals(expectedHint, gameDTONewGame.getHint().getCharacterList());
    }

    @Test
    @DisplayName("create a extra round in a game")
    void newRound() {
        trainerService.makeGuess(game.getGameId(), "pizza");

        GameDTO gameDTONewRound = trainerService.newRound(game.getGameId());

        List<Character> expectedHint = List.of('o', '.', '.', '.', '.', '.');

        assertEquals(25, gameDTONewRound.getScore());
        assertEquals(5, gameDTONewRound.getAttemptsLeft());
        assertEquals("PLAYING", gameDTONewRound.getStatus().toString());
        assertEquals(2, gameDTONewRound.getRoundNumber());
        assertEquals(expectedHint, gameDTONewRound.getHint().getCharacterList());
    }

    @Test
    @DisplayName("the word is guessed in two attempts")
    void guessWord() {
        trainerService.makeGuess(game.getGameId(), "moord");

        GameDTO gameDTOGuess = trainerService.makeGuess(game.getGameId(), "pizza");

        List<Character> expectedHint = List.of('p', 'i', 'z', 'z', 'a');

        assertEquals(game.lastRound().lastFeedback().getAttempt(), game.lastRound().getWord().getValue());
        assertEquals(20, gameDTOGuess.getScore());
        assertEquals(3, gameDTOGuess.getAttemptsLeft());
        assertEquals("WAITING_FOR_ROUND", gameDTOGuess.getStatus().toString());
        assertEquals(1, gameDTOGuess.getRoundNumber());
        assertEquals(expectedHint, gameDTOGuess.getHint().getCharacterList());
    }
}