package nl.hu.cisq1.lingo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Long roundId;

    @Column(name = "number")
    private int number;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "word")
    private Word word;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    public Round(int number, Word word, List<Feedback> feedbacks) {
        this.number = number;
        this.word = word;
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
        if(this.feedbacks.size() >= 5) {
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

        Feedback feedback = new Feedback(attempt, marks);
        feedbacks.add(feedback);

        return feedback;
    }

    public Feedback lastFeedback() {
        return this.feedbacks.get(this.feedbacks.size() -1);
    }
}