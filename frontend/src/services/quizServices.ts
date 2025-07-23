
import { BASE_QUIZ_URL, MY_HEADER } from "../utils/constants"
import type { AttemptedQuizInfo, Quiz, QuizDetails, QuizInfoProps, QuizSpecification, ScorePayload } from "../utils/interfaces";


export const generateQuiz = async(userInput: QuizSpecification): Promise<void> => {
    const url = BASE_QUIZ_URL + "/generateQuiz";
    const request = new Request(url, {
        method: "POST",
        body: JSON.stringify(userInput),
        headers: MY_HEADER,
        credentials: 'include'
    });

    try {
        const response = await fetch(request);
        const data = await response.json();

        if(!response.ok) {
            console.error(data?.message || "Error generating the quiz")
            return Promise.reject(() => {
                data?.message || "Error generating the quiz"
            });
        } else {
            return data?.content;
        }
    } catch (error) {
        console.error('Network error. Please check your connection.');
        return Promise.reject('Network error. Please check your connection.');
    }
}


export const loadQuiz = async(id: string): Promise<Quiz> => {
    const url = BASE_QUIZ_URL + "/loadQuiz/" + id;
    console.log(url)
    const request = new Request(url, {
        method: "GET",
        headers: MY_HEADER,
        credentials: "include"
    });

    try {
        const response = await fetch(request);
        const data = await response?.json();

        if(!response.ok) {
            console.error(data?.message || "Error generating the quiz")
            return Promise.reject(() => {
                data?.message || "Error generating the quiz"
            });
        } else {
            console.log(data?.content)
            return data?.content;
        }
    } catch (error) {
        console.error('Network error. Please check your connection.');
        return Promise.reject('Network error. Please check your connection.');
    }
}


export const fetchUserQuizList = async(): Promise<QuizDetails> => {
    const url = BASE_QUIZ_URL + "/getUserQuizzes";
    const request = new Request(url, {
        method: 'GET',
        headers: MY_HEADER,
        credentials: 'include'
    });

    try {
        const response = await fetch(request);
        const data = await response.json();

        if(!response.ok) {
            console.error(data?.message || "Could not fetch user quizzes");
            return Promise.reject(() => {
                data?.message || "Could not fetch user quizzes";
            });
        } else {
            console.log(data?.content);
            return data?.content?.quizList;
        }
    } catch (error) {
        console.error('Network error. Please check your connection.' , error);
        return Promise.reject('Network error. Please check your connection.');
    }
}


export const fetchUserQuiz = async(path: string): Promise<AttemptedQuizInfo> => {
    const url = BASE_QUIZ_URL + "/getQuizInfo";
    const start = path.lastIndexOf('/') + 1;
    const end = path.length;
    const id = path.slice(start, end);
    const jsonRequest: QuizInfoProps = {"quizId": id};

    const request = new Request(url, {
        method: "POST",
        body: JSON.stringify(jsonRequest),
        headers: MY_HEADER,
        credentials: "include"
    });
    
    try {
        const response = await fetch(request);
        const data = await response?.json();

        if(!response.ok) {
            return Promise.reject(() => {
                data?.message || "Error fetching the selected quiz";
            });
        } else {
            return data?.content;
        }
    } catch (error) {
        console.error('Network error. Please check your connection:' , error);
        return Promise.reject('Network error. Please check your connection:' + error);
    }
}


export const getRedirectionPath = async(quizId: string) => {
    const url = BASE_QUIZ_URL + "/getRedirectionPath/" + quizId;
    const request = new Request(url, {
        method: "GET",
        headers: MY_HEADER,
        credentials: "include"
    });

    try {
        const response = await fetch(request);
        const data = await response?.json();

        if(!response.ok) {
            return Promise.reject(() => {
                data?.message || "Error making the api call";
            });
        } else {
            return data?.content;
        }
    } catch (error) {
        console.error('Network error. Please check your connection:' , error);
        return Promise.reject('Network error. Please check your connection:' + error);
    }
}


export const calculateScore = async(payload: ScorePayload): Promise<string> => {
    const url = BASE_QUIZ_URL + "/calculateScore";
    const request = new Request(url, {
        method: "POST",
        body: JSON.stringify(payload),
        headers: MY_HEADER,
        credentials: "include"
    });

    try {
        const response = await fetch(request);
        const data = await response?.json();

        if(!response.ok) {
            return Promise.reject(() => {
                data?.message || "Error calculating the results";
            });
        } else {
            return data?.content;
        }
    } catch (error) {
        console.error('Network error. Please check your connection: ' , error);
        return Promise.reject('Network error. Please check your connection: ' + error);
    }
}
