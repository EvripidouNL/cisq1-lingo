package nl.hu.cisq1.lingo.presentation;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.data.SpringWordRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score", is(0)))
                .andExpect(jsonPath("$.roundNumber", is(1)))
                .andExpect(jsonPath("$.attemptsLeft", is(5)))
                .andExpect(jsonPath("$.feedbacks", hasSize(0)))
                .andExpect(jsonPath("$.hint").exists());

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

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score", is(0)))
                .andExpect(jsonPath("$.roundNumber", is(2)))
                .andExpect(jsonPath("$.attemptsLeft", is(5)))
                .andExpect(jsonPath("$.feedbacks", hasSize(0)))
                .andExpect(jsonPath("$.hint").exists());
    }

    /*
    @Test
    @DisplayName("make a guess on existing lingo game")
    void makeGuess() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/lingo/game/{id}", 1).contentType(MediaType.APPLICATION_JSON).content("{\"attempt\": \"woord\"}");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").exists())
                .andExpect(jsonPath("$.score").exists())
                .andExpect(jsonPath("$.roundNumber").exists())
                .andExpect(jsonPath("$.attemptsLeft").exists())
                .andExpect(jsonPath("$.feedbackList").exists())
                .andExpect(jsonPath("$.hint").exists())
                .andExpect(jsonPath("$.*", Matchers.hasSize(6)));
    }

     */
}