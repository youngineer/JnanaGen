import { useState, type ChangeEvent, type FC, type FormEvent } from "react";
import { fetchQuiz } from "../services/quizServices";

export interface QuizSpecificationState {
    userNotes: string;
    totalQuestions: number;
    numberOfOptions: number;
    additionalNotes: string;
}

const QuizSpecification: FC = () => {
    const [formData, setFormData] = useState<QuizSpecificationState>({
        userNotes: "",
        totalQuestions: 6,
        numberOfOptions: 4,
        additionalNotes: "",
    });

    const handleFormDataChange = (e: ChangeEvent<HTMLTextAreaElement | HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'totalQuestions' || name === 'numberOfOptions' 
                ? parseInt(value) 
                : value
        }));
    };

    const handleSubmit = async(e: FormEvent) => {
        e.preventDefault();
        try {
            await fetchQuiz(formData);
            // Navigate to quiz page after successful fetch
        } catch (error) {
            console.error('Failed to fetch quiz:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="w-full max-w-3xl mx-auto py-8">
            <div className="space-y-6">
                <div className="mb-6">
                    <h3 className="font-bold text-xl md:text-2xl mb-4">Create your quiz</h3>
                    <fieldset className="fieldset w-full">
                        <legend className="fieldset-legend">Enter your notes and we will generate the quiz for you</legend>
                        <textarea 
                            name="userNotes"
                            value={formData.userNotes}
                            className="textarea h-28 md:h-36 w-full" 
                            placeholder="Your notes here"
                            onChange={handleFormDataChange}
                            required
                        />
                    </fieldset>
                </div>

                <div className="mb-6">
                    <fieldset className="fieldset w-full">
                        <legend className="fieldset-legend">Additional Notes</legend>
                        <textarea 
                            name="additionalNotes"
                            value={formData.additionalNotes}
                            className="textarea h-20 md:h-24 w-full" 
                            placeholder="Additional notes here"
                            onChange={handleFormDataChange}
                        />
                    </fieldset>
                </div>

                <div className="flex flex-col md:flex-row gap-4">
                    <fieldset className="w-full md:w-1/2">
                        <legend className="fieldset-legend">
                            Number of Questions: {formData.totalQuestions}
                        </legend>
                        <input 
                            name="totalQuestions"
                            type="range" 
                            min={6} 
                            max={30} 
                            value={formData.totalQuestions}
                            className="range range-sm w-full"
                            onChange={handleFormDataChange}
                        />
                        <div className="w-full flex justify-between text-xs px-2">
                            <span>Min: 6</span>
                            <span>Max: 30</span>
                        </div>
                    </fieldset>

                    <fieldset className="fieldset w-full md:w-1/2">
                        <legend className="fieldset-legend">Options per question</legend>
                        <select 
                            name="numberOfOptions"
                            value={formData.numberOfOptions}
                            className="select w-full" 
                            onChange={handleFormDataChange}
                        >
                            <option value={2}>2 Options</option>
                            <option value={3}>3 Options</option>
                            <option value={4}>4 Options</option>
                            <option value={5}>5 Options</option>
                            <option value={6}>6 Options</option>
                        </select>
                    </fieldset>
                </div>

                <div className="flex justify-center">
                    <button 
                        type="submit" 
                        className="btn btn-primary btn-wide"
                        disabled={!formData.userNotes.trim()}
                    >
                        Generate Quiz
                    </button>
                </div>
            </div>
        </form>
    );
};

export default QuizSpecification;
