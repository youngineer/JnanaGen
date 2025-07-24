import { useEffect, useState, type FC } from "react";
import { useLocation, useNavigate } from "react-router";
import { calculateScore, loadQuiz } from "../services/quizServices";
import type { Option, Question, Quiz } from "../utils/interfaces";

const QuizPage: FC = () => {
  const navigate = useNavigate();
    const [quiz, setQuiz] = useState<Quiz>();
    const [currentQuestionIndex, setCurrentQuestionIndex] = useState<number>(0);
    const [selectedAnswers, setSelectedAnswers] = useState<string[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    const location = useLocation().pathname;

    const quizId = location.slice(location.lastIndexOf('/') + 1);

    useEffect(() => {
        const fetchQuiz = async () => {
            setLoading(true);
            setError(null);
            try {
                const loadedQuiz = await loadQuiz(quizId);
                setQuiz(loadedQuiz);
                setSelectedAnswers(new Array(loadedQuiz.questions.length).fill(""));
            } catch (err) {
                setError("Failed to load quiz.");
            } finally {
                setLoading(false);
            }
        };
        fetchQuiz();
    }, [quizId]);

    if (loading) {
        return (
            <div className="flex justify-center items-center h-64">
                <span className="loading loading-spinner loading-lg"></span>
            </div>
        );
    }

    if (error || !quiz) {
        return (
            <div className="flex justify-center items-center h-64 text-error">
                {error || "Quiz not found."}
            </div>
        );
    }

    const questions: Question[] = quiz.questions;
    const totalQuestions = questions.length;
    const title = quiz.title;

    const handleNext = (): void => {
        if (currentQuestionIndex < totalQuestions - 1) {
            setCurrentQuestionIndex(prev => prev + 1);
        }
    };

    const handlePrev = (): void => {
        if (currentQuestionIndex > 0) {
            setCurrentQuestionIndex(prev => prev - 1);
        }
    };

    const handleOptionSelect = (option: string): void => {
        const newAnswers = [...selectedAnswers];
        newAnswers[currentQuestionIndex] = option;
        setSelectedAnswers(newAnswers);
    };

    const handleSubmit = async(): Promise<void> => {
        // Build questionOptionMap and userAnswerList
        const userAnswerList: string[] = [];
        const questionOptionMap: Record<number, number> = {};

        for(let i = 0; i < totalQuestions; i++) {
          const question = questions[i];
          const answer = selectedAnswers[i];
          
          question.options.forEach(opt => {
            if(opt.option === answer) {
              questionOptionMap[question.id] = opt.id
              console.log(opt.id)
            };
          })
          userAnswerList.push(answer);
        }

        const payload = {
            quizId: Number(quizId),
            userAnswerList,
            questionOptionMap
        };

        console.log(payload);
        try {
          const path = await calculateScore(payload);
          console.log(path);
          navigate(path);
        } catch (err) {
            setError("Failed to load quiz.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className='flex flex-col justify-center items-center p-4'>
            <h3 className="text-2xl font-bold mb-4">{title}</h3>
            
            <div className="mb-6">
                <progress 
                    className="progress w-56 mx-1" 
                    value={currentQuestionIndex + 1} 
                    max={totalQuestions}>
                </progress>
                <span className="ml-2">
                    {currentQuestionIndex + 1} out of {totalQuestions}
                </span>
            </div>

            <div className="card bg-base-100 w-2xl shadow-lg">
                <div className="card-body">
                    <h2 className="card-title">
                        Q. {questions[currentQuestionIndex].question}
                    </h2>
                    
                    <div className="flex flex-col gap-2 my-4">
                        {questions[currentQuestionIndex].options.map((optionObj: Option) => (
                            <label 
                                key={optionObj.id} 
                                className={`btn ${selectedAnswers[currentQuestionIndex] === optionObj.option ? 'btn-primary' : 'btn-ghost'}`}
                            >
                                <input 
                                    type="radio"
                                    name={`question-${currentQuestionIndex}`}
                                    value={optionObj.option}
                                    checked={selectedAnswers[currentQuestionIndex] === optionObj.option}
                                    onChange={() => handleOptionSelect(optionObj.option)}
                                    className="hidden"
                                />
                                {optionObj.option}
                            </label>
                        ))}
                    </div>

                    <div className="flex justify-between mt-4">
                        <button 
                            className="btn btn-outline"
                            onClick={handlePrev}
                            disabled={currentQuestionIndex === 0}
                        >
                            Previous
                        </button>
                        <button 
                            className="btn btn-outline"
                            onClick={handleNext}
                            disabled={currentQuestionIndex === totalQuestions - 1}
                        >
                            Next
                        </button>
                    </div>
                </div>
            </div>

            <button 
                className="btn btn-primary mt-4"
                onClick={handleSubmit}
                disabled={selectedAnswers.includes("")}
            >
                Submit Quiz
            </button>
        </div>
    );
};

export default QuizPage;