import { useEffect, useState, type FC, type JSX } from 'react'
import { fetchUserQuizList, getRedirectionPath } from '../services/quizServices'
import { useNavigate } from 'react-router-dom';
import type { AlertState, QuizDetails  } from '../utils/interfaces';


const Sidebar: FC = (): JSX.Element => {
  const navigate = useNavigate();

  const [quizList, setQuizList] = useState<QuizDetails>({});

  const [alert, setAlert] = useState<AlertState>({
    show: false,
    isSuccess: false,
    message: ''
  });

  useEffect(() => {
    const fetchQuizzes = async():Promise<void> => {
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

  const handleQuizRedirect = async (id: string) => {
    try {
      const path = await getRedirectionPath(id);
      if (path) {
        navigate(path, { replace: true });
      } else {
        setAlert({
          show: true,
          isSuccess: false,
          message: "Redirection path not found."
        });
      }
    } catch (error) {
      setAlert({
        show: true,
        isSuccess: false,
        message: (error as string) || "Failed to load the quiz."
      });
    }
  };

  return (
  <>
    <details open={false} className='mx-4'>
      <summary className='font-bold cursor-pointer border-1 p-2 rounded-lg shadow-2xs'>Your Quizzes</summary>
      <ul className='flex flex-col gap-1 max-w-96'>
        {
          Object.entries(quizList).map(([id, quiz]) => (
            <li key={id} className="">
              <div className="tooltip tooltip-right w-full tooltip-warning" data-tip={quiz.title}>
                <button
                  className="btn btn-soft-content px-2 py-1 rounded hover:btn-soft transition w-full text-left"
                  onClick={() => handleQuizRedirect(id)}
                >
                  <span className="block truncate max-w-full">
                    {quiz.title}
                  </span>
                </button>
              </div>
            </li>
          ))
        }
      </ul>
    </details>
    {alert.show && (
      <div className={`alert ${alert.isSuccess ? 'alert-success' : 'alert-error'} mt-4`}>
        {alert.message}
      </div>
    )}
  </>
)
}

export default Sidebar;