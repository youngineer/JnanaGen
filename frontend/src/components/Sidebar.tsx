import { useEffect, useState, type FC, type JSX } from 'react'
import { fetchUserQuizList } from '../services/quizServices'
import { Link } from 'react-router-dom';
import type { AlertState, QuizDetails  } from '../utils/interfaces';


const Sidebar: FC = (): JSX.Element => {

  const [quizList, setQuizList] = useState<QuizDetails>({});

  const [alert, setAlert] = useState<AlertState>({
    show: false,
    isSuccess: false,
    message: ''
  });

  useEffect(() => {
    const fetchQuizzes = async() => {
      try {
        const quizzes = await fetchUserQuizList();
        setQuizList(quizzes);
      } catch (error) {
        setAlert({
          show: true,
          isSuccess: false,
          message: error as string
        })
      }
    };

    fetchQuizzes();
  }, []);

  return (
    <>
      <div className='mt-6'>
        <ul className="menu bg-base-200 rounded-box w-56">
          <li>
            <details open>
              <summary>Your Quizzes</summary>
              <ul>
                {
                  Object.entries(quizList).map(([id, quiz]) => (
                    <div className="tooltip" data-tip={quiz.title}>
                      <li key={id}>
                          <Link to={`/quizInfo/${id}`}>{quiz.title}</Link>
                      </li>
                    </div>
                  ))
                }
              </ul>
            </details>
          </li>
        </ul>
      </div>
    </>
  )
}

export default Sidebar;