package nl.hu.cisq1.lingo.presentation.dto;

import lombok.*;
import nl.hu.cisq1.lingo.domain.Feedback;
import nl.hu.cisq1.lingo.domain.Hint;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameDTO {
    private Long gameId;

    private int score;

    private int roundNumber;

    private int attemptsLeft;

    private List<Feedback> feedbacks;

    private Hint hint;
}