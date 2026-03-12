import type {GradingResponse} from "./schemas.ts";
import {Card, CardBody, Radio, RadioGroup} from "@heroui/react";

type QuestionFeedback = GradingResponse['feedback'][0];

interface QuestionCardProps {
    question: string;
    options: string[];
    index: number;
    selectedValue: string;
    onSelect: (val: string) => void;
    feedback?: QuestionFeedback;
}

export default function QuestionCard({question, options, index, selectedValue, onSelect, feedback}: QuestionCardProps) {
    let cardBorder = "";
    if (feedback) {
        cardBorder = feedback.isCorrect ? "border-2 border-success bg-success-50" : "border-2 border-danger bg-danger-50";
    }

    return (
        <Card className={`w-full ${cardBorder}`}>
            <CardBody className="p-6">
                <h2 className="text-xl font-semibold mb-4"
                    dangerouslySetInnerHTML={{__html: `${index + 1}. ${question}`}}/>

                <RadioGroup
                    value={selectedValue || ""}
                    onValueChange={onSelect}
                    isDisabled={!!feedback}
                >
                    {options.map((option, aIndex) => {
                        let textClass = "text-foreground";

                        if (feedback) {
                            if (option === feedback.correctAnswer) {
                                textClass = "text-success-700 font-bold";
                            } else if (option === feedback.userAnswer && !feedback.isCorrect) {
                                textClass = "text-danger-700 line-through";
                            }
                        }

                        return (
                            <Radio key={aIndex} value={option}>
                                <span className={textClass} dangerouslySetInnerHTML={{__html: option}}/>
                            </Radio>
                        );
                    })}
                </RadioGroup>

                {feedback && !feedback.isCorrect && (
                    <div className="mt-4 p-3 bg-white rounded-md border-2 border-neutral-200">
                        <p className="text-sm text-success-600 font-semibold">
                            Correct answer: <span dangerouslySetInnerHTML={{__html: feedback.correctAnswer}}/>
                        </p>
                    </div>
                )}
            </CardBody>
        </Card>
    );
}
