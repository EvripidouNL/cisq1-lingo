package nl.hu.cisq1.lingo;

import nl.hu.cisq1.lingo.application.WordService;
import nl.hu.cisq1.lingo.data.SpringGameRepository;
import nl.hu.cisq1.lingo.data.SpringWordRepository;
import nl.hu.cisq1.lingo.domain.Word;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Profile("ci")
@TestConfiguration
public class CiTestConfiguration {
    @Bean
    CommandLineRunner importWords(SpringWordRepository wordRepository) {
        return new WordTestDataFixtures(wordRepository);
    }

    @Bean
    CommandLineRunner importGame(SpringGameRepository gameRepository, WordService wordService) {
        return new GameTestDataFixtures(gameRepository, wordService);
    }
}