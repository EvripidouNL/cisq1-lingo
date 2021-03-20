package nl.hu.cisq1.lingo;

import nl.hu.cisq1.lingo.application.WordService;
import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.data.SpringWordRepository;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.domain.Word;
import org.springframework.boot.CommandLineRunner;

import java.util.ArrayList;

public class GameTestDataFixtures implements CommandLineRunner {
    private final SpringGameRepository gameRepository;
    private final WordService wordService;

    public GameTestDataFixtures(SpringGameRepository gameRepository, WordService wordService) {
        this.gameRepository = gameRepository;
        this.wordService = wordService;
    }

    @Override
    public void run(String... args) throws Exception {
        String randomWord = wordService.provideRandomWord(5);
        Word word = new Word(randomWord);

        Game game = new Game(0, new ArrayList<>());
        game.newRound(word);
        game.lastRound().startRound();

        this.gameRepository.save(game);
    }
}