package nl.hu.cisq1.lingo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import nl.hu.cisq1.lingo.domain.exception.WordAlreadyGuessedException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @Column(name = "feedback_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long feedbackId;

    @Column(name = "attempt")
    private String attempt;

    @Column(name = "marks", nullable = false)
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Mark.class)
    private List<Mark> marks;

    public Feedback(String attempt, List<Mark> marks) {
        this.attempt = attempt;
        this.marks = marks;
    }

    public boolean isWordGuessed() {
        return marks.stream().allMatch(mark -> mark.equals(Mark.CORRECT));
    }

    public boolean guessIsInvalid() {
        return marks.stream().allMatch(mark -> mark.equals(Mark.INVALID));
    }

    public Hint giveHint() {
        List<Character> characters = new ArrayList<>();
        int index = 0;

        for (Mark mark : this.marks) {
            if (mark.equals(Mark.CORRECT)) {
                characters.add(this.attempt.charAt(index));
            } else {
                characters.add('.');
            }

            index++;
        }

        return new Hint(characters);
    }
}