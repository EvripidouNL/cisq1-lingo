package nl.hu.cisq1.lingo.presentation.dto;

import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Hint;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GameMapper {
    GameDTO toGameDTO(Game game, Hint hint);

    List<GameDTO> toGameDTOs(List<Game> games);

    Game toGame(GameDTO gameDTO);
}