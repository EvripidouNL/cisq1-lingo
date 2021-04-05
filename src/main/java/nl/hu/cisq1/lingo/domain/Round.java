package nl.hu.cisq1.lingo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import nl.hu.cisq1.lingo.domain.exception.RoundAttemptLimitException;
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
        if (this.feedbacks.size() >= 5) {
            throw new RoundAttemptLimitException();
        }

        if (!this.feedbacks.isEmpty() && lastFeedback().isWordGuessed()) {
            throw new WordAlreadyGuessedException();
        }

        return generateMarks(attempt);
    }

    private Feedback generateMarks(String attempt) {
        List<Character> lettersRemain = new ArrayList<>();
        // lettersRemain to check if a guessedLetter is present or absent
        List<Mark> marks = new ArrayList<>();
        // to show the right marks to the user

        if (attempt.length() == word.getLength()) {
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

                if (lettersRemain.contains(letterOfGuess)) {
                    // if the guess has letters that are also in the lettersRemain
                    marks.add(Mark.PRESENT);
                    // remove the present letters of lettersRemain
                    lettersRemain.remove(letterOfGuess);
                    } else if (!letterOfGuess.equals(letterOfWord)) {
                        // if the guess has no letters that are in lettersRemain
                        marks.add(Mark.ABSENT);
                    } else {
                    // if the guessedLetter is equal to the letterMatch
                    marks.add(Mark.CORRECT);
                }
            }
        } else {
            // if the attempt length is not the same as the word length
            for (int i=0; i< attempt.length(); i++) {
                if (attempt.length() != word.getLength())
                    marks.add(Mark.INVALID);
            }
        }

        Feedback feedback = new Feedback(attempt, marks);
        feedbacks.add(feedback);

        return feedback;
    }
}