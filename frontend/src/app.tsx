import {Button, Card, CardHeader, Spinner} from '@heroui/react';
import useTrivia from './trivia.hook';
import {useState} from "react";
import {type GradingResponse, GradingResponseSchema} from "./schemas.ts";
import QuestionCard from "./question-card.tsx";
import {getTriviaHandleAnswers} from "./trivia.data.ts";


function App() {
    const {data, error, isLoading, mutate} = useTrivia();
    const [answers, setAnswers] = useState<string[]>([]);
    const [result, setResult] = useState<GradingResponse | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSelectAnswer = (questionIndex: number, answer: string) => {
        const newAnswers = [...answers];
        newAnswers[questionIndex] = answer;
        setAnswers(newAnswers);
    };

    const handleSubmit = async () => {
        if (!data) return;
        setIsSubmitting(true);

        try {
            const response = await getTriviaHandleAnswers(data.gameId, answers)

            if (!response.ok) throw new Error('Error checking answers');

            const responseData = await response.json();
            const validatedResult = GradingResponseSchema.parse(responseData);
            setResult(validatedResult);
        } finally {
            setIsSubmitting(false);
        }
    };

    const handlePlayAgain = async () => {
        setAnswers([]);
        setResult(null);
        await mutate();
    };

    if (isLoading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <Spinner size="lg" label="Fetching questions..." color="primary"/>
            </div>
        );
    }

    if (error) {
        return <div className="min-h-screen flex items-center justify-center text-danger">Error: {error.message}</div>;
    }

    if (!data) return null;

    const answeredCount = answers.filter(a => a !== undefined && a !== "").length;
    const isReadyToSubmit = answeredCount === data.questions.length;

    return (
        <div className="max-w-3xl mx-auto p-6 mt-10">
            <h1 className="text-4xl font-extrabold text-center mb-8 text-foreground">Open Trivia Challenge</h1>

            {result && (
                <Card className="mb-8 border-none bg-primary-50">
                    <CardHeader className="flex-col items-center pb-4 pt-6 px-4">
                        <h2 className="text-3xl font-bold text-primary">Your Score</h2>
                        <p className="text-xl mt-2 text-foreground">
                            You got <span className="font-bold text-success text-2xl">{result.score}</span> out
                            of {result.totalQuestions} questions right!
                        </p>
                    </CardHeader>
                </Card>
            )}

            <div className="space-y-8">
                {data.questions.map((q, qIndex) => (
                    <QuestionCard
                        key={qIndex}
                        index={qIndex}
                        question={q.question}
                        options={q.all_answers}
                        selectedValue={answers[qIndex]}
                        onSelect={(val) => handleSelectAnswer(qIndex, val)}
                        feedback={result ? result.feedback[qIndex] : undefined}
                    />
                ))}
            </div>

            <div className="mt-8 flex justify-center pb-10">
                {!result ? (
                    <Button
                        color="success"
                        size="lg"
                        onPress={handleSubmit}
                        isLoading={isSubmitting}
                        isDisabled={!isReadyToSubmit}
                        className="text-white font-bold"
                    >
                        Check Answers
                    </Button>
                ) : (
                    <Button
                        color="primary"
                        size="lg"
                        onPress={handlePlayAgain}
                        className="font-bold"
                    >
                        Play Again
                    </Button>
                )}
            </div>
        </div>
    );
}

export default App;