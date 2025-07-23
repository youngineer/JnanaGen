import { useEffect, useState, type FC } from 'react'
import { fetchUserQuiz } from '../services/quizServices';
import type { Evaluation } from '../utils/interfaces';
import { useNavigate, useOutletContext } from 'react-router';



const QuizInfo: FC = () => {
  const [quiz, setQuiz] = useState<Evaluation>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [title, setTitle] = useState<string>("");
  const [score, setScore] = useState<number>(-1);
  const [percentage, setPercentage] = useState<number>(-1);
  const [totalQuestions, setTotalQuestions] = useState<number>(-1);
  const navigate = useNavigate()

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

  
  if(score === undefined) {
    navigate('/quiz');
  }


  return (
    <div className="max-w-3xl mx-auto space-y-6">
      <h2 className="flex text-2xl font-bold my-6 justify-center text-secondary">{title}</h2>
      {/* <div className="radial-progress" style={{ "--value": {percentage} } as React.CSSProperties } 
        aria-valuenow={70} role="progressbar">{percentage}%</div> */}

      <h3 className="flex text-2xl font-bold my-6 justify-center text-secondary">Score: {score}/{totalQuestions}</h3>
      <div className="space-y-4">
        {Object.entries(quiz).map(([id, question]) => (
          <div 
            key={id}
            role="alert" 
            className={`card ${
              question.isCorrect ? 'bg-success/60' : 'bg-error/20'
            } shadow-lg`}
          >
            <div className="card-body">
              <h3 className="card-title text-primary">Q. {question.question}</h3>
              <div className="space-y-2 text-sm">
                <p className={`${
                  question.isCorrect ? 'text-secondary' : 'text-error'
                } font-medium`}>
                  Your Answer: {question.userAnswer}
                </p>
                <p className="text-secondary font-medium">
                  Correct Answer: {question.correctAnswer}
                </p>
                <div className="divider my-2"></div>
                <p className="text-primary font-bold">
                  <span className="">Explanation:</span> {question.explanation}
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