package nl.hu.cisq1.lingo.domain;

import lombok.*;
import nl.hu.cisq1.lingo.domain.exception.RoundDoesNotBelongToGameException;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "lingogames")
public class Game {
    @Id
    @Column(name = "game_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @Column(name = "score")
    private int score;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Round> rounds;

    public Game(Long gameId, int score, List<Round> rounds) {
        this.gameId = gameId;
        this.score = score;
        this.rounds = rounds;
    }

    public void calculateScore(Round round, int attempts) {
        if (!this.rounds.contains(round)) {
            throw new RoundDoesNotBelongToGameException();
        }

        this.score += 5 * (5 - attempts) + 5;
    }

    public Round newRound(Word word) {
        int totalRounds = this.rounds.size();

        Round round = new Round(totalRounds +1, word, new ArrayList<>());

        this.rounds.add(round);

        return round;
    }

    public Round lastRound() {
        return this.rounds.get(this.rounds.size() -1);
    }
}