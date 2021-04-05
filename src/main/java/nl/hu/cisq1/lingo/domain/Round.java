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
        List<Integer> letterMatch = new ArrayList<>();
        // letterMatch to check if a guessedLetter is correct or not correct
        List<Character> lettersRemain = new ArrayList<>();
        // lettersRemain to check if a guessedLetter is present or absent
        List<Mark> marks = new ArrayList<>();
        // to show the right marks to the user

        for (int index = 0; index < this.word.getLength(); index++) {
            Character letterOfGuess = attempt.charAt(index);
            Character letterOfWord = this.word.getValue().charAt(index);

            if(word.getLength() != attempt.length()) {
                marks.add(Mark.INVALID);
                continue;
            }

            if (!letterOfGuess.equals(letterOfWord)) {
                // if the letter of the guess doesn't match the letter of the word add 0 (false)
                letterMatch.add(0);
            } else {
                // if the letter of the guess doesn't match the letter of the word add 1 (true)
                letterMatch.add(1);
            }
        }

        for (int index = 0; index < attempt.length(); index++) {
            Character letterOfWord = this.word.getValue().charAt(index);

            if (letterMatch.get(index).equals(0)) {
                // if the match is false add the letter to lettersRemain arrayList
                lettersRemain.add(letterOfWord);
            }
        }

        for (int index = 0; index < attempt.length(); index++) {
            Character letterOfGuess = attempt.charAt(index);

            if (letterMatch.get(index).equals(0)) {
                // check for the letters that were not correct
                if (lettersRemain.contains(letterOfGuess)) {
                    // if the guess has letters that are also in the lettersRemain
                    marks.add(Mark.PRESENT);
                    // remove the present letters of lettersRemain
                    lettersRemain.remove(letterOfGuess);
                } else {
                    // if the guess has no letters that are in lettersRemain
                    marks.add(Mark.ABSENT);
                }
            } else if (letterMatch.get(index).equals(1)) {
                // if the guessedLetter is equal to the letterMatch
                marks.add(Mark.CORRECT);
            }
        }

        Feedback feedback = new Feedback(attempt, marks);
        feedbacks.add(feedback);

        return feedback;
    }
}