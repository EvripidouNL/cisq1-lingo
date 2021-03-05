package nl.hu.cisq1.lingo.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
public class Hint {
    private List<Character> characterList;

    public Hint(List<Character> characterList) {
        this.characterList = characterList;
    }
}
