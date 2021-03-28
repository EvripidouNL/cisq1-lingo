package nl.hu.cisq1.lingo.domain;

import lombok.*;
import org.hibernate.annotations.Cascade;
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

    @OneToMany
    @JoinColumn
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Round> rounds;

    public Game(int score, List<Round> rounds) {
        this.score = score;
        this.rounds = rounds;
    }

    public int calculateScore() {
        if(lastRound().lastFeedback().isWordGuessed()) {
            this.score += 5 * (5 - lastRound().getFeedbacks().size()) + 5;
        }

        return score;
    }

    public Round newRound(Word word) {
        Round round = new Round(word, new ArrayList<>());

        round.startRound();

        this.rounds.add(round);

        return round;
    }

    public Round lastRound() {
        return this.rounds.get(this.rounds.size() -1);
    }

    public int attemptsLeft() {
        return 5 - this.lastRound().getFeedbacks().size();
    }

    public int totalRounds() {
        return this.getRounds().size() %3 +5;
    }
}