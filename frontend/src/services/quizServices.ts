import { BASE_QUIZ_URL, MY_HEADER } from "../utils/constants"


interface QuizSpecificationState{
    userNotes: string,
    totalQuestions: number,
    numberOfOptions: number,
    additionalNotes: string
}


export const fetchQuiz = async(userInput: QuizSpecificationState): Promise<void> => {
    const url = BASE_QUIZ_URL + "/generateQuiz";
    console.log(userInput);
    const request = new Request(url, {
        method: "POST",
        body: JSON.stringify(userInput),
        headers: MY_HEADER
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
            console.log(data?.content)
            return data?.content;
        }
    } catch (error) {
        console.error('Network error. Please check your connection.');
        return Promise.reject('Network error. Please check your connection.');
    }
}