import { useEffect, useState, type FC } from 'react'
import { fetchUserQuiz } from '../services/quizServices';
import type { Evaluation } from '../utils/interfaces';



const QuizInfo: FC = () => {
  const [quiz, setQuiz] = useState<Evaluation>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [title, setTitle] = useState<string>("");
  const [score, setScore] = useState<number>(0);
  const [percentage, setPercentage] = useState<number>(0);
  const [totalQuestions, setTotalQuestions] = useState<number>(0);

  const path = location.pathname;

  useEffect(() => {
    const getQuiz = async () => {
      try {
        setIsLoading(true);
        const {evaluation, title, score, percentage, totalQuestions} = await fetchUserQuiz(path);
        
        setQuiz(evaluation);
        setTitle(title);
        setScore(score);
        setPercentage(percentage);
        setTotalQuestions(totalQuestions);
        setError(null);

      } catch (err) {
        setError(err as string);
        setQuiz([]);

      } finally {
        setIsLoading(false);
      }
    }

    if (path) {
      getQuiz()
    }
  }, [path])

  return (
    <div className="max-w-3xl mx-auto space-y-6">
      <h2 className="text-2xl font-bold mb-6">Quiz Results</h2>
      <div className="space-y-4">
        {Object.entries(quiz).map(([id, question]) => (
          <div 
            key={id}
            role="alert" 
            className={`card ${
              question.isCorrect ? 'bg-success/10' : 'bg-error/10'
            } shadow-lg`}
          >
            <div className="card-body">
              <h3 className="card-title">Q. {question.question}</h3>
              <div className="space-y-2 text-sm">
                <p className={`${
                  question.isCorrect ? 'text-success' : 'text-error'
                } font-medium`}>
                  Your Answer: {question.userAnswer}
                </p>
                <p className="text-success font-medium">
                  Correct Answer: {question.correctAnswer}
                </p>
                <div className="divider my-2"></div>
                <p className="text-base-content/80">
                  <span className="font-medium">Explanation:</span> {question.explanation}
                </p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

export default QuizInfo