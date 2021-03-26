package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.domain.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.presentation.dto.GameMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Service
@Transactional
public class TrainerService {
    private final SpringGameRepository gameRepository;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private WordService wordService;

    public TrainerService(SpringGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game findById(Long id) {
        return gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }

    public GameDTO startNewGame() {
        String randomWord = wordService.provideRandomWord(5);
        Word word = new Word(randomWord);

        Game game = new Game(0, new ArrayList<>());
        game.newRound(word);
        game.lastRound().startRound();

        this.gameRepository.save(game);

        return gameMapper.toGameDTO(
                game,
                game.lastRound().startRound());
    }

    public GameDTO newRound(Long id) {
        Game game = findById(id);

        String randomWord = wordService.provideRandomWord(game.getRounds().size() %3 +5);
        Word word = new Word(randomWord);

        game.newRound(word);
        game.lastRound().startRound();

        this.gameRepository.save(game);

        return gameMapper.toGameDTO(
                game,
                game.lastRound().startRound());
    }

    public GameDTO makeGuess(Long id, String attempt) {
        Game game = findById(id);

        game.lastRound().guessWord(attempt);

        if (game.lastRound().lastFeedback().isWordGuessed()) {
            game.calculateScore(game.lastRound(), game.lastRound().getFeedbacks().size());
        }

        this.gameRepository.save(game);
        return gameMapper.toGameDTO(
                game,
                game.lastRound().lastFeedback().giveHint());
    }
}