package dev.meeuwsen.quadassignment.backend;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static dev.meeuwsen.quadassignment.backend.TriviaModels.*;

@Service
public class TriviaService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, List<OpenTdbQuestion>> activeGames = new ConcurrentHashMap<>();

    public QuestionResponse fetchQuestions() {
        String url = "https://opentdb.com/api.php?amount=10&type=multiple";
        OpenTdbResponse response = restTemplate.getForObject(url, OpenTdbResponse.class);

        if (response == null || response.results() == null) {
            throw new RuntimeException("Could not fetch trivia questions!");
        }

        String gameId = UUID.randomUUID().toString();
        activeGames.put(gameId, response.results());

        List<FrontendQuestion> safeQuestions = response.results().stream().map(q -> {
            List<String> mixedAnswers = new ArrayList<>(q.incorrect_answers());
            mixedAnswers.add(q.correct_answer());
            Collections.shuffle(mixedAnswers);
            return new FrontendQuestion(q.question(), mixedAnswers);
        }).toList();

        return new QuestionResponse(gameId, safeQuestions);
    }

    public GradingResponse checkAnswers(AnswerSubmission submission) {
        List<OpenTdbQuestion> originalQuestions = activeGames.get(submission.gameId());

        if (originalQuestions == null) {
            throw new IllegalArgumentException("Game session not found or already completed!");
        }

        int score = 0;
        List<String> userAnswers = submission.user_answers();
        List<QuestionFeedback> feedbackList = new ArrayList<>();

        for (int i = 0; i < originalQuestions.size(); i++) {
            OpenTdbQuestion q = originalQuestions.get(i);
            String correct = q.correct_answer();

            String userAnswer = (i < userAnswers.size()) ? userAnswers.get(i) : "No answer provided";

            boolean isCorrect = correct.equals(userAnswer);

            if (isCorrect) {
                score++;
            }

            feedbackList.add(new QuestionFeedback(q.question(), userAnswer, correct, isCorrect));
        }

        activeGames.remove(submission.gameId());

        return new GradingResponse(score, originalQuestions.size(), feedbackList);
    }
}