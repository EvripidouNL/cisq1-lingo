package nl.hu.cisq1.lingo.presentation.dto;

import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Hint;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameMapper {
    GameDTO toGameDTO(Game game, Hint hint);
}