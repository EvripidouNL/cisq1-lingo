package nl.hu.cisq1.lingo.presentation.dto;

import lombok.*;
import nl.hu.cisq1.lingo.domain.Hint;
import nl.hu.cisq1.lingo.domain.Round;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameDTO {
    private Long gameId;

    private int score;

    private List<Round> rounds;

    private Hint hint;
}