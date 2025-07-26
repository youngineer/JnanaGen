import { useEffect, useState, type FC } from 'react'
import { fetchUserQuiz } from '../services/quizServices';
import type { Evaluation } from '../utils/interfaces';
import { useNavigate } from 'react-router';


const QuizInfo: FC = () => {
  const [quiz, setQuiz] = useState<Evaluation>([]);
  const [title, setTitle] = useState<string>("");
  const [score, setScore] = useState<number>(-1);
  const [percentage, setPercentage] = useState<number>(-1);
  const navigate = useNavigate()

  const path: string = location.pathname;

  useEffect(() => {
    const getQuiz = async () => {
      try {
        const {evaluation, title, score, percentage } = await fetchUserQuiz(path);
        
        setQuiz(evaluation);
        setTitle(title);
        setScore(score);
        setPercentage(percentage);

      } catch (err) {
        setQuiz([]);
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
    <div className="max-w-3xl mx-auto space-y-6 mb-auto">
      <h1 className="text-2xl font-bold flex justify-center items-center">{title}</h1>
        <div className="text-xl flex justify-center items-center gap-4 mb-4">
        <h3>Score:</h3>
        <div
          className="radial-progress p-0.5"
          style={{ "--value": percentage } as React.CSSProperties}
          aria-valuenow={percentage}
          role="progressbar"
        >
          {percentage}%
        </div>
      </div>
      
      {
        Object.entries(quiz).map(([id, data]) => (
          <div
            key={id}
            className={`card w-auto ${
              data.isCorrect
                ? 'bg-success text-success-content'
                : 'bg-error text-error-content'
            }`}
          >
            <div className="card-body">
              <h2 className="card-title">Q. {data.question}</h2>
              <h4>Your answer: {data.userAnswer}</h4>
              <h4>Correct answer: {data.correctAnswer}</h4>
              <h3 className='text-accent-content'>Explanation: {data.explanation}</h3>
            </div>
          </div>
        ))
      }
    </div>
  )
}

export default QuizInfo