package nl.hu.cisq1.lingo.domain;

import lombok.Getter;
import nl.hu.cisq1.lingo.domain.exception.WordLengthNotSupportedException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity(name = "words")
public class Word {
    @Id
    @Column(name = "word")
    private String value;
    private Integer length;

    public Word() {}
    public Word(String word) {
        if (word.length() < 5 || word.length() > 7) {
            throw new WordLengthNotSupportedException(length);
        }

        this.value = word;
        this.length = word.length();
    }
}