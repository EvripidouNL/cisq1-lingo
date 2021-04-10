package nl.hu.cisq1.lingo.presentation.dto;

import nl.hu.cisq1.lingo.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class GameMapperIntegrationTest {
    private Game game;
    private Word word;

    @Autowired
    GameMapper gameMapper = Mappers.getMapper(GameMapper.class);

    @BeforeEach
    public void init() {
        word = new Word("woord");
        game = new Game(0, new ArrayList<>(), Status.WAITING_FOR_ROUND);
    }

    @Test
    @DisplayName("new game to gameDTO")
    void newGameToGameDTO() {
        game.newRound(word);
        GameDTO gameDTO = gameMapper.toGameDTO(game, null, game.lastRound().startRound());

        List<Character> expectedHint = List.of('w', '.', '.', '.', '.');

        assertEquals(game.getGameId(), gameDTO.getGameId());
        assertEquals(0, gameDTO.getScore());
        assertEquals(1, gameDTO.getRoundNumber());
        assertEquals(5, gameDTO.getAttemptsLeft());
        assertEquals("PLAYING", gameDTO.getStatus().toString());
        assertNull(gameDTO.getFeedback());
        assertEquals(expectedHint, gameDTO.getHint().getCharacterList());
    }

    @Test
    @DisplayName("game exists and feedback and hint is null")
    void hintAndFeedbackIsNull() {
        game.newRound(word);
        GameDTO gameDTO = gameMapper.toGameDTO(game, null, null);

        assertNull(gameDTO.getFeedback());
        assertNull(gameDTO.getHint());
    }

    @Test
    @DisplayName("game and feedback exists but hint is null")
    void hintIsNull() {
        game.newRound(word);
        Feedback feedback = new Feedback();
        GameDTO gameDTO = gameMapper.toGameDTO(game, feedback, null);

        assertNull(gameDTO.getHint());
    }

    @Test
    @DisplayName("game, feedback and hint is null")
    void gameFeedbackAndHintIsNull() {
        GameDTO gameDTO = gameMapper.toGameDTO(null, null, null);

        assertNull(gameDTO);
    }
}