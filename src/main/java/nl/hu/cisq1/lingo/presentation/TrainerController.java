package nl.hu.cisq1.lingo.presentation;

import nl.hu.cisq1.lingo.application.TrainerService;
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

    @PostMapping("/games")
    // For creating a new Lingo gameId
    public GameDTO createGame() {
        return this.trainerService.startNewGame();
    }

    @PostMapping("/game/{id}/round")
    // For creating a new round for lingo game
    public GameDTO newRound(@PathVariable Long id) {
        return this.trainerService.newRound(id);
    }

    @PostMapping("/game/{id}")
    // For making a guess
    public GameDTO makeGuess(@PathVariable Long id, @RequestBody GuessDTO guessDTO) {
        return trainerService.makeGuess(id, guessDTO.getAttempt());
    }
}