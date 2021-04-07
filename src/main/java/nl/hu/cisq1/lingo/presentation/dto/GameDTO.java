package nl.hu.cisq1.lingo.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.hu.cisq1.lingo.domain.Feedback;
import nl.hu.cisq1.lingo.domain.Hint;
import nl.hu.cisq1.lingo.domain.Status;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameDTO {
    private Long gameId;

    private int score;

    private int roundNumber;

    private int attemptsLeft;

    private Status status;

    private Feedback feedback;

    private Hint hint;
}