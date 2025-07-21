import React, { type FC } from 'react'


interface QuestionInterface {
    id: number,
    question: string,
    userAnswer: string,
    correctAnswer: string,
    isCorrect: boolean,
    explanation: string
}

const QuizInfo: FC = (props: QuestionInterface[]) => {
    
  return (
    <div>
        
    </div>
  )
}

export default QuizInfo;