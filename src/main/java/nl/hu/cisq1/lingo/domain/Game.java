package nl.hu.cisq1.lingo.domain;

import lombok.*;
import nl.hu.cisq1.lingo.domain.exception.CannotStartNewRoundException;
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

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Status status;

    public Game(int score, List<Round> rounds, Status status) {
        this.score = score;
        this.rounds = rounds;
        this.status = status;
    }

    public void calculateScoreAndGiveStatus() {
        boolean calculateScore = false;

        if (lastRound().lastFeedback().isWordGuessed()) {
            calculateScore = true;
            this.status = Status.WAITING_FOR_ROUND;
        } else if (lastRound().getAttempts() >= 5) {
            calculateScore = true;
            this.status = Status.GAME_ENDED;
        }

        if (calculateScore) {
            this.score += 5 * (5 - lastRound().getAttempts()) + 5;
        }
    }

    public Round newRound(Word word) {
        if (!status.equals(Status.WAITING_FOR_ROUND)) {
            throw new CannotStartNewRoundException(this.status);
        }

        Round round = new Round(word, new ArrayList<>());
        round.startRound();

        this.status = Status.PLAYING;
        this.rounds.add(round);

        return round;
    }

    public Round lastRound() {
        return this.rounds.get(this.rounds.size() -1);
    }

    public int attemptsLeft() {
        return 5 - this.lastRound().getAttempts();
    }

    public int wordLengthBasedOnRounds() {
        return this.getRounds().size() %3 +5;
    }
}