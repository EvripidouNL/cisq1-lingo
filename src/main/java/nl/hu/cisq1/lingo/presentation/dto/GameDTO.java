package nl.hu.cisq1.lingo.presentation.dto;

import lombok.*;
import nl.hu.cisq1.lingo.domain.Hint;
import nl.hu.cisq1.lingo.domain.Round;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameDTO {
    private Long gameId;

    private int score;

    private Round round;

    private Hint hint;
}