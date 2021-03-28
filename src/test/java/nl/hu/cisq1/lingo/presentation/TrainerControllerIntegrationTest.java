package nl.hu.cisq1.lingo.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.data.SpringWordRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.presentation.dto.GuessDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
class TrainerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpringGameRepository gameRepository;

    @MockBean
    private SpringWordRepository wordRepository;

    @Test
    @DisplayName("create a new lingo game")
    void startNewGame() throws Exception {
        when(wordRepository.findRandomWordByLength(5))
                .thenReturn(Optional.of(new Word("woord")));

        RequestBuilder request = MockMvcRequestBuilders
                .post("/lingo/games");

        String[] expectedHint = { "w", ".", ".", ".", "." };

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score", is(0)))
                .andExpect(jsonPath("$.roundNumber", is(1)))
                .andExpect(jsonPath("$.attemptsLeft", is(5)))
                .andExpect(jsonPath("$.feedbacks", hasSize(0)))
                .andExpect(jsonPath("$.hint.characterList", hasSize(5)))
                .andExpect(jsonPath("$.hint.characterList", containsInRelativeOrder(expectedHint)));

    }

    @Test
    @DisplayName("create a new round for a lingo game by id")
    void createRound() throws Exception {
        Game game = new Game(0, new ArrayList<>());
        Word word = new Word("woord");
        game.newRound(word);

        when(gameRepository.findById(0L))
                .thenReturn(Optional.of(game));
        when(wordRepository.findRandomWordByLength(6))
                .thenReturn(Optional.of(new Word("aanleg")));

        RequestBuilder request = MockMvcRequestBuilders
                .post("/lingo/game/0/round");

        String[] expectedHint = { "a", ".", ".", ".", ".", "." };

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score", is(0)))
                .andExpect(jsonPath("$.roundNumber", is(2)))
                .andExpect(jsonPath("$.attemptsLeft", is(5)))
                .andExpect(jsonPath("$.feedbacks", hasSize(0)))
                .andExpect(jsonPath("$.hint.characterList", hasSize(6)))
                .andExpect(jsonPath("$.hint.characterList", containsInRelativeOrder(expectedHint)));
    }

    @Test
    @DisplayName("make a guess on a lingo game")
    void makeGuess() throws Exception {
        Game game = new Game(0, new ArrayList<>());
        Word word = new Word("woord");
        when(wordRepository.findRandomWordByLength(5))
                .thenReturn(Optional.of(word));

        game.newRound(word);

        when(gameRepository.findById(0L))
                .thenReturn(Optional.of(game));

        GuessDTO guessDTO = new GuessDTO();
        guessDTO.setAttempt("moord");
        String guessBody = new ObjectMapper().writeValueAsString(guessDTO);

        String[] expectedHint = { ".", "o", "o", "r", "d" };

        RequestBuilder guessRequest = MockMvcRequestBuilders
                .post("/lingo/game/" + 0L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(guessBody);

        mockMvc.perform(guessRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score", is(0)))
                .andExpect(jsonPath("$.roundNumber", is(1)))
                .andExpect(jsonPath("$.attemptsLeft", is(4)))
                .andExpect(jsonPath("$.feedbacks", hasSize(1)))
                .andExpect(jsonPath("$.hint.characterList", hasSize(5)))
                .andExpect(jsonPath("$.hint.characterList", containsInRelativeOrder(expectedHint)));
    }
}