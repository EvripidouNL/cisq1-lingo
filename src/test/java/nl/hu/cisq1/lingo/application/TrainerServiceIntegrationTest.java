package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.domain.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
class TrainerServiceIntegrationTest {
    @Autowired
    private TrainerService trainerService;

    @Autowired
    private SpringGameRepository gameRepository;

    @Test
    @DisplayName("can not find the game with this id")
    void canNotFindGame() {
        assertThrows(GameNotFoundException.class, () -> trainerService.findById(0L));
    }

    @Test
    @DisplayName("create a new game")
    void newGame() {
        GameDTO gameDTO = trainerService.startNewGame();

        assertEquals(0, gameDTO.getScore());
        assertEquals(5, gameDTO.getAttemptsLeft());
        assertEquals("PLAYING", gameDTO.getStatus().toString());
        assertEquals(1, gameDTO.getRoundNumber());
        assertEquals(5, gameDTO.getHint().getCharacterList().size());
    }

    @Test
    @DisplayName("create a extra round in a game")
    void newRound() {
        GameDTO gameDTONewGame = trainerService.startNewGame();

        Game game = gameRepository.findById(gameDTONewGame.getGameId()).orElseThrow();

        Word word = game.lastRound().getWord();

        trainerService.makeGuess(game.getGameId(), word.getValue());

        GameDTO gameDTONewRound = trainerService.newRound(game.getGameId());

        assertEquals(25, gameDTONewRound.getScore());
        assertEquals(5, gameDTONewRound.getAttemptsLeft());
        assertEquals("PLAYING", gameDTONewRound.getStatus().toString());
        assertEquals(2, gameDTONewRound.getRoundNumber());
        assertEquals(6, gameDTONewRound.getHint().getCharacterList().size());
    }

    @Test
    @DisplayName("the word is guessed in two attempts")
    void guessWord() {
        GameDTO gameDTONewGame = trainerService.startNewGame();

        Game game = gameRepository.findById(gameDTONewGame.getGameId()).get();

        Word word = game.lastRound().getWord();

        trainerService.makeGuess(game.getGameId(), "moord");
        GameDTO gameDTOLastGuess = trainerService.makeGuess(game.getGameId(), word.getValue());

        assertEquals(game.lastRound().lastFeedback().getAttempt(), game.lastRound().getWord().getValue());
        assertEquals(20, gameDTOLastGuess.getScore());
        assertEquals(3, gameDTOLastGuess.getAttemptsLeft());
        assertEquals("WAITING_FOR_ROUND", gameDTOLastGuess.getStatus().toString());
        assertEquals(1, gameDTOLastGuess.getRoundNumber());
        assertEquals(5, gameDTOLastGuess.getHint().getCharacterList().size());
    }
}