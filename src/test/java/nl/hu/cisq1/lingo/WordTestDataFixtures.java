package nl.hu.cisq1.lingo;

import nl.hu.cisq1.lingo.data.SpringWordRepository;
import nl.hu.cisq1.lingo.domain.Word;
import org.springframework.boot.CommandLineRunner;

public class WordTestDataFixtures implements CommandLineRunner {
    private final SpringWordRepository repository;

    public WordTestDataFixtures(SpringWordRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.repository.save(new Word("pizza"));
        this.repository.save(new Word("oranje"));
        this.repository.save(new Word("wanorde"));
    }
}