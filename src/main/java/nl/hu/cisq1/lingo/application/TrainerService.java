package nl.hu.cisq1.lingo.application;

import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Status;
import nl.hu.cisq1.lingo.domain.Word;
import nl.hu.cisq1.lingo.domain.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.presentation.dto.GameMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@Transactional
public class TrainerService {
    private final WordService wordService;
    private final SpringGameRepository gameRepository;
    private final GameMapper gameMapper = Mappers.getMapper(GameMapper.class);

    public TrainerService(WordService wordService, SpringGameRepository gameRepository) {
        this.wordService = wordService;
        this.gameRepository = gameRepository;
    }

    public Game findById(Long id) {
        return gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }

    public GameDTO startNewGame() {
        String randomWord = wordService.provideRandomWord(5);
        Word word = new Word(randomWord);

        Game game = new Game(0, new ArrayList<>(), Status.WAITING_FOR_ROUND);
        game.newRound(word);

        this.gameRepository.save(game);

        return gameMapper.toGameDTO(
                game,
                null,
                game.lastRound().startRound());
    }

    public GameDTO newRound(Long id) {
        Game game = findById(id);

        String randomWord = wordService.provideRandomWord(game.wordLengthBasedOnRounds());
        Word word = new Word(randomWord);

        game.newRound(word);

        this.gameRepository.save(game);

        return gameMapper.toGameDTO(
                game,
                null,
                game.lastRound().startRound());
    }

    public GameDTO makeGuess(Long id, String attempt) {
        Game game = findById(id);

        game.lastRound().guessWord(attempt);
        game.calculateScoreAndGiveStatus();

        this.gameRepository.save(game);
        return gameMapper.toGameDTO(
                game,
                game.lastRound().lastFeedback(),
                game.lastRound().lastFeedback().giveHint());
    }
}