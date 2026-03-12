import {QuestionResponseSchema} from "./schemas.ts";


export function getTriviaHandleAnswers(gameId: string, answers: string[]) {
    return fetch('http://localhost:8080/checkanswers', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({gameId, user_answers: answers}),
    });
}

export default async function getTriviaQuestions() {
    const res = await fetch('http://localhost:8080/questions');
    if (!res.ok) throw new Error('An error occurred while getting trivia');
    const data = await res.json();
    return QuestionResponseSchema.parse(data);
}
