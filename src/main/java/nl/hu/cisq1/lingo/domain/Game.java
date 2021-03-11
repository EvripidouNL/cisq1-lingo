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
    private Long id;

    @Column(name = "score")
    private double score;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "round_id")
    private List<Round> rounds;

    public Game(Long id, double score, List<Round> rounds) {
        this.id = id;
        this.score = score;
        this.rounds = rounds;
    }

    public void calculateScore(Round round, int attempts) {
        if (!this.rounds.contains(round)) {
            throw new RoundDoesNotBelongToGameException();
        }

        this.score += 5 * (5 - attempts) + 5;
    }

    public void newRound(Word word) {
        Round round = new Round(this.rounds.size() +1L, word, new ArrayList<>());

        round.startRound();

        this.rounds.add(round);
    }
}