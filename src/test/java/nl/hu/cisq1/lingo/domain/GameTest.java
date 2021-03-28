package nl.hu.cisq1.lingo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    @DisplayName("score based on attempts in round")
    void calculateScore() {
        Game game = new Game(0, new ArrayList<>());
        Word word = new Word("woord");

        game.newRound(word);
        game.lastRound().guessWord("moord");
        game.lastRound().guessWord("woord");

        game.calculateScore();

        assertEquals(20, game.getScore());
    }


    @Test
    @DisplayName("add one new round to game")
    void newRound() {
        Game game = new Game(0, new ArrayList<>());

        game.newRound(new Word("woord"));

        assertEquals(1, game.getRounds().size());
    }

    @Test
    @DisplayName("last Feedback of round")
    void lastRound() {
        Game game = new Game(0, new ArrayList<>());
        String roundWord = "woord";
        Word word = new Word(roundWord);

        Round round = game.newRound(word);

        round.guessWord("moord");

        assertEquals(round, game.lastRound());
    }
}