package nl.hu.cisq1.lingo.presentation.dto;

import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Hint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {
    @Mapping(target = "feedbackList", expression = "java(game.lastRound().getFeedbacks())")
    @Mapping(target = "attemptsLeft", expression = "java(5 - game.lastRound().getFeedbacks().size())")
    @Mapping(target = "roundNumber", expression = "java(game.getRounds().size())")
    GameDTO toGameDTO(Game game, Hint hint);
}