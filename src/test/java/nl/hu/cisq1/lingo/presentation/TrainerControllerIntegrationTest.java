package nl.hu.cisq1.lingo.presentation;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
class TrainerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("create a new lingo game")
    void startNewGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/lingo/games");



        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").exists())
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.roundNumber").value(1))
                .andExpect(jsonPath("$.attemptsLeft").value(5))
                .andExpect(jsonPath("$.feedbackList").value(new ArrayList<>()))
                .andExpect(jsonPath("$.hint").exists())
                .andExpect(jsonPath("$.*", Matchers.hasSize(6)));
    }

    @Test
    @DisplayName("create a new round for a lingo game by id")
    void createRound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/lingo/game/{id}/round", 1);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").exists())
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.roundNumber").exists())
                .andExpect(jsonPath("$.attemptsLeft").value(5))
                .andExpect(jsonPath("$.feedbackList").value(new ArrayList<>()))
                .andExpect(jsonPath("$.hint").exists())
                .andExpect(jsonPath("$.*", Matchers.hasSize(6)));
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