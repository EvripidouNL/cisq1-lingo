package nl.hu.cisq1.lingo.presentation.dto;

import nl.hu.cisq1.lingo.domain.Feedback;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Hint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {
    @Mapping(target = "attemptsLeft", expression = "java(game.attemptsLeft())")
    @Mapping(target = "roundNumber", expression = "java(game.getRounds().size())")
    @Mapping(target = "status", expression = "java(game.getStatus())")
    GameDTO toGameDTO(Game game, Feedback feedback, Hint hint);
}