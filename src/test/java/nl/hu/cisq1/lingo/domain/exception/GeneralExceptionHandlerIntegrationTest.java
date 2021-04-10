package nl.hu.cisq1.lingo.domain.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.presentation.dto.GuessDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
class GeneralExceptionHandlerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringGameRepository gameRepository;

    @MockBean
    private GuessDTO guessDTO;

    private Game game;

    @BeforeEach
    public void init() throws Exception {
        RequestBuilder newGameRequest = MockMvcRequestBuilders
                .post("/lingo/games");

        MockHttpServletResponse response = mockMvc.perform(newGameRequest).andReturn().getResponse();
        Integer gameId = JsonPath.read(response.getContentAsString(), "$.gameId");

        game = gameRepository.getOne(gameId.longValue());
    }

    @Test
    @DisplayName("exception: cannot make new round when the word is not guessed")
    void cannotStartNewRound() throws Exception {
        RequestBuilder newRoundRequest = MockMvcRequestBuilders
                .post("/lingo/game/" + game.getGameId() + "/round");

        mockMvc.perform(newRoundRequest)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type", is(CannotStartNewRoundException.class.getSimpleName())))
                .andExpect(jsonPath("$.message", is("The word is not guessed so a new round is not available! " + "game status: " + game.getStatus())));
    }

    @Test
    @DisplayName("exception: the game has ended")
    void gameEnded() throws Exception {
        guessDTO = new GuessDTO("moord");
        String guessBody = new ObjectMapper().writeValueAsString(guessDTO.getAttempt());

        RequestBuilder guessRequest = MockMvcRequestBuilders
                .post("/lingo/game/" + game.getGameId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(guessBody);

        for (int i = 0; i < 6; i++){
            mockMvc.perform(guessRequest);
        }

        mockMvc.perform(guessRequest)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type", is(GameEndedException.class.getSimpleName())))
                .andExpect(jsonPath("$.message", is("The attempt limit is reached! The game is ended! " + "game status: " + game.getStatus())));
    }

    @Test
    @DisplayName("exception: cannot find the game")
    void gameNotFound() throws Exception {
        guessDTO = new GuessDTO("moord");
        String guessBody = new ObjectMapper().writeValueAsString(guessDTO.getAttempt());

        RequestBuilder guessRequest = MockMvcRequestBuilders
                .post("/lingo/game/" + 0L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(guessBody);

        mockMvc.perform(guessRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is(GameNotFoundException.class.getSimpleName())))
                .andExpect(jsonPath("$.message", is("Could not find Game with this ID " + 0)));
    }

    @Test
    @DisplayName("exception: word length is not supported")
    void wordLengthNotSupportedException() throws Exception {
        RequestBuilder randomWordRequest = MockMvcRequestBuilders
                .get("/words/random")
                .contentType(MediaType.APPLICATION_JSON)
                .param("length", "10");

        mockMvc.perform(randomWordRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is(WordLengthNotSupportedException.class.getSimpleName())))
                .andExpect(jsonPath("$.message", is("Could not find word of length " + 10)));
    }
}