package dev.meeuwsen.quadassignment.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static dev.meeuwsen.quadassignment.backend.TriviaModels.*;

@RestController
@CrossOrigin(origins = "*")
public class TriviaController {

    private final TriviaService triviaService;

    public TriviaController(TriviaService triviaService) {
        this.triviaService = triviaService;
    }

    @GetMapping("/questions")
    public QuestionResponse getQuestions() {
        return triviaService.fetchQuestions();
    }

    @PostMapping("/checkanswers")
    public GradingResponse checkAnswers(@RequestBody AnswerSubmission submission) {
        return triviaService.checkAnswers(submission);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleInvalidGameId(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
}