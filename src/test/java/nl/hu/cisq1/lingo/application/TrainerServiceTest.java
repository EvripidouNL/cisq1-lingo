package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.domain.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainerServiceTest {
    @ParameterizedTest
    @DisplayName("requests a game by id from the repository")
    @MethodSource("gameByIdExamples")
    void findGameById(Long id, Game game) {
        SpringGameRepository gameRepository = mock(SpringGameRepository.class);

        when(gameRepository.findById(id))
                .thenReturn(Optional.of(new Game(id)));

        TrainerService trainerService = new TrainerService(gameRepository);

        String result = trainerService.findById(id).toString();

        assertEquals(game.toString(), result);
    }

    static Stream<Arguments> gameByIdExamples() {
        return Stream.of(
                Arguments.of(1L, new Game(1L)),
                Arguments.of(2L, new Game(2L)),
                Arguments.of(3L, new Game(3L))
        );
    }
}