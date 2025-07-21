import { useEffect, useState, type FC, type JSX } from "react";

const questions = [
  {
    question: "What is the capital of France?",
    options: ["Berlin", "Madrid", "Paris", "Rome"],
    correctAnswer: "Paris"
  },
  {
    question: "Which planet is known as the Red Planet?",
    options: ["Earth", "Mars", "Jupiter", "Saturn"],
    correctAnswer: "Mars"
  },
  {
    question: "Who wrote 'To Kill a Mockingbird'?",
    options: ["Harper Lee", "J.K. Rowling", "Ernest Hemingway", "George Orwell"],
    correctAnswer: "Harper Lee"
  },
  {
    question: "What is the largest ocean on Earth?",
    options: ["Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean"],
    correctAnswer: "Pacific Ocean"
  },
  {
    question: "Which element has the chemical symbol 'O'?",
    options: ["Oxygen", "Osmium", "Ozone", "Opium"],
    correctAnswer: "Oxygen"
  }
];

const totalQuestions: number = questions.length;


interface QuestionInterface{
  question: string,
  options: string[]
}

const QuizPage: FC = (): JSX.Element => {
    const [currentQuestionIndex, setCurrentQuestionIndex] = useState<number>(0);
    const [selectedAnswers, setSelectedAnswers] = useState<string[]>(new Array(totalQuestions));

    useEffect(() => {
      
    }, []);
    
    const handleNext = (): void => {
      if(currentQuestionIndex < totalQuestions - 1) {
        setCurrentQuestionIndex(prev => prev + 1);
      }
    }

    const handlePrev = (): void => {
      if(currentQuestionIndex > 0) {
        setCurrentQuestionIndex(prev => prev - 1);
      }
    }

    const handleOptionSelect = (option: string):void => {
      const newAnswers = [...selectedAnswers];
      newAnswers[currentQuestionIndex] = option;
      setSelectedAnswers(newAnswers);
    };

    const handleSubmit = (): void => {
      console.log(selectedAnswers);
    }

  return (
        <div className='flex flex-col justify-center items-center p-4'>
            <h3 className="text-2xl font-bold mb-4">Quiz title</h3>
            
            <div className="mb-6">
                <progress 
                    className="progress w-56 mx-1" 
                    value={currentQuestionIndex} 
                    max={questions.length - 1}>
                </progress>
                <span className="ml-2">
                    {currentQuestionIndex + 1} out of {questions.length}
                </span>
            </div>

            <div className="card bg-base-100 w-96 shadow-lg">
                <div className="card-body">
                    <h2 className="card-title">
                        Q. {questions[currentQuestionIndex].question}
                    </h2>
                    
                    <div className="flex flex-col gap-2 my-4">
                        {questions[currentQuestionIndex].options.map((option, index) => (
                            <label 
                                key={index} 
                                className={`btn ${selectedAnswers[currentQuestionIndex] === option ? 'btn-primary' : 'btn-ghost'}`}
                            >
                                <input 
                                    type="radio"
                                    name={`question-${currentQuestionIndex}`}
                                    value={option}
                                    checked={selectedAnswers[currentQuestionIndex] === option}
                                    onChange={() => handleOptionSelect(option)}
                                    className="hidden"
                                />
                                {option}
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
                            disabled={currentQuestionIndex === questions.length - 1}
                        >
                            Next
                        </button>
                    </div>
                </div>
            </div>

            <button 
                className="btn btn-primary mt-4"
                onClick={handleSubmit}
                // disabled={selectedAnswers.includes('')}
            >
                Submit Quiz
            </button>
        </div>
    );
};

export default QuizPage;