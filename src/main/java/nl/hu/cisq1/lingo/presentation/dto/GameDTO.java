package nl.hu.cisq1.lingo.presentation.dto;

import lombok.*;
import nl.hu.cisq1.lingo.domain.Hint;
import nl.hu.cisq1.lingo.domain.Round;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {
    public Long gameId;

    public int score;

    public List<Round> rounds;

    public Hint hint;
}