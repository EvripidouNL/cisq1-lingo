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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
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
    @DisplayName("create a new game by trainer service")
    void newGame() {
        GameDTO gameDTO = trainerService.startNewGame();

        Game game = gameRepository.findById(gameDTO.getGameId()).get();

        assertNotNull(game);
    }

    @Test
    @DisplayName("create a extra round in a game")
    void newRound() {
        GameDTO gameDTO = trainerService.startNewGame();

        Game game = gameRepository.findById(gameDTO.getGameId()).get();

        trainerService.newRound(game);

        assertEquals(2, game.getRounds().size());
    }

    @Test
    @DisplayName("the word is guessed in two attempts")
    void guessWord() {
        GameDTO gameDTO = trainerService.startNewGame();

        Game game = gameRepository.findById(gameDTO.getGameId()).get();

        game.lastRound().setWord(new Word("woord"));

        trainerService.makeGuess(game, "moord");
        trainerService.makeGuess(game, "woord");

        assertEquals(game.lastRound().lastFeedback().getAttempt(), game.lastRound().getWord().getValue());
    }
}