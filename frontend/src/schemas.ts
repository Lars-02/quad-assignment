import { z } from 'zod';

export const FrontendQuestionSchema = z.object({
    question: z.string(),
    all_answers: z.array(z.string()),
});

export const QuestionResponseSchema = z.object({
    gameId: z.string(),
    questions: z.array(FrontendQuestionSchema),
});

export const QuestionFeedbackSchema = z.object({
    question: z.string(),
    userAnswer: z.string(),
    correctAnswer: z.string(),
    isCorrect: z.boolean(),
});

export const GradingResponseSchema = z.object({
    score: z.number(),
    totalQuestions: z.number(),
    feedback: z.array(QuestionFeedbackSchema),
});

export type QuestionResponse = z.infer<typeof QuestionResponseSchema>;
export type GradingResponse = z.infer<typeof GradingResponseSchema>;
export type QuestionFeedback = GradingResponse['feedback'][0];
