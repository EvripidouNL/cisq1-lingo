package nl.hu.cisq1.lingo.domain;

import lombok.Data;
import java.util.List;

@Data
public class Hint {
    private List<Character> characterList;

    public Hint(List<Character> characterList) {
        this.characterList = characterList;
    }
}
