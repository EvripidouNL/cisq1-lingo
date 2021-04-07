package nl.hu.cisq1.lingo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.hu.cisq1.lingo.domain.exception.GameEndedException;
import nl.hu.cisq1.lingo.domain.exception.WordAlreadyGuessedException;
import org.hibernate.annotations.Cascade;

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

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "word")
    private Word word;

    @Column(name = "attempts")
    private int attempts;

    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn
    private List<Feedback> feedbacks;

    public Round(Word word, List<Feedback> feedbacks) {
        this.word = word;
        this.feedbacks = feedbacks;
    }

    public Feedback lastFeedback() {
        return this.feedbacks.get(this.feedbacks.size() -1);
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
        if (this.attempts >= 5) {
            throw new GameEndedException(Status.GAME_ENDED);
        }

        if (!this.feedbacks.isEmpty() && lastFeedback().isWordGuessed()) {
            throw new WordAlreadyGuessedException();
        }

        Feedback feedback;

        if (attempt.length() == word.getLength()) {
            feedback = new Feedback(attempt, generateMarks(attempt));
        } else {
            feedback = new Feedback(attempt, generateInvalidMarks());
        }

        this.attempts++;
        this.feedbacks.add(feedback);
        return feedback;
    }

    private List<Mark> generateInvalidMarks() {
        List<Mark> marks = new ArrayList<>();
        // to show the invalid marks to the user

        for (int i=0; i< word.getLength(); i++) {
            marks.add(Mark.INVALID);
        }

        return marks;
    }

    private List<Mark> generateMarks(String attempt) {
        List<Character> lettersRemain = new ArrayList<>();
        // to show the right amount of presented letters
        List<Mark> marks = new ArrayList<>();
        // to show the right marks to the user

        for (int index = 0; index < attempt.length(); index++) {
            Character letterOfGuess = attempt.charAt(index);
            Character letterOfWord = this.word.getValue().charAt(index);

            if (!letterOfGuess.equals(letterOfWord)) {
                // if the match is false add the letter to lettersRemain arrayList
                lettersRemain.add(letterOfWord);
            }
        }

        for (int index = 0; index < attempt.length(); index++) {
            Character letterOfGuess = attempt.charAt(index);
            Character letterOfWord = this.word.getValue().charAt(index);

            if (letterOfGuess.equals(letterOfWord)) {
                marks.add(Mark.CORRECT);
            } else if (lettersRemain.contains(letterOfGuess)) {
                // if the guess has letters that are also in the word
                marks.add(Mark.PRESENT);
                lettersRemain.remove(letterOfGuess);
                // remove the present letters of lettersRemain
            } else {
                marks.add(Mark.ABSENT);
            }
        }

        return marks;
    }
}