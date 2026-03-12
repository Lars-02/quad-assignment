import useSWR from "swr";
import getTriviaQuestions from "./trivia.data.ts";
import type {QuestionResponse} from "./schemas.ts";

export default function useTrivia() {
    return useSWR<QuestionResponse>(
        ['questions'],
        getTriviaQuestions,
        {revalidateOnFocus: false}
    );
};