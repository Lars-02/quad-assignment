package dev.meeuwsen.quadassignment.backend;

import java.util.List;

public class TriviaModels {
    public record OpenTdbResponse(int response_code, List<OpenTdbQuestion> results) {
    }

    public record OpenTdbQuestion(String category, String type, String difficulty,
                                  String question, String correct_answer, List<String> incorrect_answers) {
    }

    public record FrontendQuestion(String question, List<String> all_answers) {
    }

    public record QuestionResponse(String gameId, List<FrontendQuestion> questions) {
    }

    public record AnswerSubmission(String gameId, List<String> user_answers) {
    }

    public record QuestionFeedback(String question, String userAnswer, String correctAnswer, boolean isCorrect) {}
    public record GradingResponse(int score, int totalQuestions, List<QuestionFeedback> feedback) {}
}