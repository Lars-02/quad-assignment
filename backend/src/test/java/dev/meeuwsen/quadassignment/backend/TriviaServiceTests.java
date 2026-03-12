package dev.meeuwsen.quadassignment.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static dev.meeuwsen.quadassignment.backend.TriviaModels.*;
import static org.junit.jupiter.api.Assertions.*;

class TriviaServiceTest {

    private TriviaService triviaService;

    @BeforeEach
    void setUp() {
        triviaService = new TriviaService();
    }

    private void injectFakeGame(String gameId) {
        OpenTdbQuestion q1 = new OpenTdbQuestion(
                "Science", "multiple", "easy", "Q1", "A", List.of("B", "C", "D"));
        OpenTdbQuestion q2 = new OpenTdbQuestion(
                "Geography", "multiple", "easy", "Q2", "E", List.of("F", "G", "H"));

        Map<String, List<OpenTdbQuestion>> fakeMemory = new ConcurrentHashMap<>();
        fakeMemory.put(gameId, List.of(q1, q2));
        ReflectionTestUtils.setField(triviaService, "activeGames", fakeMemory);
    }

    @Test
    void checkAnswersMixedResults() {
        String testGameId = UUID.randomUUID().toString();
        injectFakeGame(testGameId);
        AnswerSubmission submission = new AnswerSubmission(testGameId, List.of("A", "F"));

        GradingResponse response = triviaService.checkAnswers(submission);

        assertNotNull(response);
        assertEquals(1, response.score());
        assertEquals(2, response.totalQuestions());
        assertTrue(response.feedback().get(0).isCorrect());
        assertFalse(response.feedback().get(1).isCorrect());
    }

    @Test
    void checkAnswersAllCorrect() {
        String testGameId = UUID.randomUUID().toString();
        injectFakeGame(testGameId);
        AnswerSubmission submission = new AnswerSubmission(testGameId, List.of("A", "E"));

        GradingResponse response = triviaService.checkAnswers(submission);

        assertEquals(2, response.score());
        assertTrue(response.feedback().get(0).isCorrect());
        assertTrue(response.feedback().get(1).isCorrect());
    }

    @Test
    void checkAnswersAllIncorrect() {
        String testGameId = UUID.randomUUID().toString();
        injectFakeGame(testGameId);
        AnswerSubmission submission = new AnswerSubmission(testGameId, List.of("B", "F"));

        GradingResponse response = triviaService.checkAnswers(submission);

        assertEquals(0, response.score());
        assertFalse(response.feedback().get(0).isCorrect());
        assertFalse(response.feedback().get(1).isCorrect());
    }

    @Test
    void checkAnswersFewerAnswersThanQuestions() {
        String testGameId = UUID.randomUUID().toString();
        injectFakeGame(testGameId);
        AnswerSubmission submission = new AnswerSubmission(testGameId, List.of("A"));

        GradingResponse response = triviaService.checkAnswers(submission);

        assertEquals(1, response.score());
        assertTrue(response.feedback().get(0).isCorrect());
        assertFalse(response.feedback().get(1).isCorrect());
        assertEquals("No answer provided", response.feedback().get(1).userAnswer());
    }

    @Test
    void checkAnswersMoreAnswersThanQuestions() {
        String testGameId = UUID.randomUUID().toString();
        injectFakeGame(testGameId);
        AnswerSubmission submission = new AnswerSubmission(testGameId, List.of("A", "E", "Extra"));

        GradingResponse response = triviaService.checkAnswers(submission);

        assertEquals(2, response.score());
        assertEquals(2, response.feedback().size());
    }

    @Test
    void checkAnswersThrowsExceptionWhenGameIdIsInvalid() {
        String fakeGameId = UUID.randomUUID().toString();
        AnswerSubmission badSubmission = new AnswerSubmission(fakeGameId, List.of("A", "B"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            triviaService.checkAnswers(badSubmission);
        });

        assertEquals("Game session not found or already completed!", exception.getMessage());
    }
}