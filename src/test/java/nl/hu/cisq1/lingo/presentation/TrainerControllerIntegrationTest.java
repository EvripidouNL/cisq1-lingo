package nl.hu.cisq1.lingo.presentation;

import nl.hu.cisq1.lingo.CiTestConfiguration;

import nl.hu.cisq1.lingo.domain.Hint;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
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
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
class TrainerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("create a new lingo game")
    void createGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/lingo/create");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("{'score' : 0, 'roundNumber' : 1, 'feedbackList' :[], 'attemptsLeft' : 5}"))
                .andExpect(jsonPath("$.*", Matchers.hasSize(6)));
    }

    @Test
    @DisplayName("create a new round for a lingo game by id")
    void createRound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/lingo/game/{gameById}/round", 1L);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("{'gameId' : 1, 'feedbackList' :[], attemptsLeft : 5}"))
                .andExpect(jsonPath("$.*", Matchers.hasSize(6)));
    }
}