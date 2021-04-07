package nl.hu.cisq1.lingo.presentation.dto;

import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Status;
import nl.hu.cisq1.lingo.domain.Word;
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
class GameMapperTest {
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
    @DisplayName("from the gamemapper to the gameDTO")
    void toGameDTO() {
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
    @DisplayName("game exists and hint is null")
    void hintIsNull() {
        game.newRound(word);
        GameDTO gameDTO = gameMapper.toGameDTO(game, null, null);

        assertNull(gameDTO.getFeedback());
        assertNull(gameDTO.getHint());
    }

    @Test
    @DisplayName("game and hint is null")
    void gameAndHintIsNull() {
        GameDTO gameDTO = gameMapper.toGameDTO(null, null, null);

        assertNull(gameDTO);
    }
}