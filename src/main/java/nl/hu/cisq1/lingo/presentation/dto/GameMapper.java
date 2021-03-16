package nl.hu.cisq1.lingo.presentation.dto;

import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Hint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {
    @Mapping(target = "round", expression = "java(game.lastRound())")
    GameDTO toGameDTO(Game game, Hint hint);
}