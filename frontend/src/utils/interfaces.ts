export interface QuizSpecification {
    userNotes: string;
    additionalNotes: string;
    totalQuestions: number;
    numberOfOptions: number;
}


export interface QuizDetails {
  [id: number]: {
    title: string;
    createdAt: string;
    updatedAt: string;
  }
}

export interface QuizInfoProps {
  quizId: string;
}

export interface AlertState {
    show: boolean;
    isSuccess: boolean;
    message: string;
}

export interface AuthCredentials {
    emailId: string;
    password: string;
}

export interface SignupCredentials extends AuthCredentials {
    name: string;
}

export interface Evaluation {
    [id: number]: {
        question: string,
        userAnswer: string,
        correctAnswer: string,
        isCorrect: boolean,
        explanation: string
    }
}

export interface AttemptedQuizInfo {
    evaluation: Evaluation,
    title: string,
    totalQuestions: number,
    score: number,
    percentage: number
}

export interface Option {
    id: number,
    option: string
}

export interface Question {
    id: number,
    question: string,
    options: Option[]
}

export interface Quiz {
    id: number,
    title: string,
    questions: Question[]
}


export interface ScorePayload {
    quizId: number,
    userAnswerList: string[],
    questionOptionMap: Record<number, number>
}


export interface AlertMessageInterface {
    isSuccess: boolean,
    message: string
}