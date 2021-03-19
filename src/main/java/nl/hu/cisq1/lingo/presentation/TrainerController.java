package nl.hu.cisq1.lingo.presentation;

import nl.hu.cisq1.lingo.application.TrainerService;
import nl.hu.cisq1.lingo.domain.Game;
import nl.hu.cisq1.lingo.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.presentation.dto.GuessDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lingo")
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping("/create")
    // For creating a new Lingo gameId
    public GameDTO createGame() {
        return this.trainerService.startNewGame();
    }

    @PostMapping("/game/{gameById}/round")
    // For creating a new round for lingo game
    public GameDTO newRound(@PathVariable Game gameById) {
        return this.trainerService.newRound(gameById);
    }

    @PostMapping("/game/{gameById}")
    // For making a guess
    public GameDTO makeGuess(@PathVariable Game gameById, @Validated @RequestBody GuessDTO guessDTO) {
        return trainerService.makeGuess(gameById, guessDTO.getAttempt());
    }
}