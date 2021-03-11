package nl.hu.cisq1.lingo.domain;

import lombok.*;
import nl.hu.cisq1.lingo.domain.exception.RoundAttemptLimitException;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "rounds")
public class Round {
    @Id
    @Column(name = "round_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    @OneToOne
    @JoinColumn(name = "word")
    private Word word;

    @Column(name = "attempt")
    private int attempt;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "round_id")
    private List<Feedback> feedbacks;

    public Round(Long number, Word word, List<Feedback> feedbacks) {
        this.number = number;
        this.word = word;
        this.attempt = 0;
        this.feedbacks = feedbacks;
    }

    public Hint startRound() {
        List<Character> characters = new ArrayList<>();

        for (int i=0; i< this.word.getLength(); i++) {
            char letter = word.getValue().charAt(0);

            if(i == 0) {
                characters.add(letter);
            } else {
                characters.add('.');
            }
        }

        return new Hint(characters);
    }

    public Feedback guessWord(String attempt) {

        if(this.attempt >= 5) {
            throw new RoundAttemptLimitException();
        }

        List<Mark> marks = new ArrayList<>();

        for (int i=0; i< this.word.getLength(); i++) {

            if (attempt.charAt(i) == word.getValue().charAt(i)) {
                marks.add(Mark.CORRECT);
            } else if (word.getValue().contains(attempt.charAt(i) + "")) {
              marks.add(Mark.PRESENT);
            } else {
                marks.add(Mark.ABSENT);
            }
        }

        this.attempt++;

        return new Feedback(attempt, marks);
    }
}