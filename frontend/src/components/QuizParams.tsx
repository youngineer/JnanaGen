import { useState, type ChangeEvent, type FC, type FormEvent } from "react";
import { generateQuiz } from "../services/quizServices";
import { useNavigate } from "react-router";
import type { QuizSpecification } from "../utils/interfaces";

const QuizParams: FC = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState<QuizSpecification>({
        userNotes: "",
        totalQuestions: 6,
        numberOfOptions: 4,
        additionalNotes: "",
    });

    const [loading, setLoading] = useState<boolean>(false);

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
            navigate("/quiz")
            setLoading(true);
            await generateQuiz(formData);
            setLoading(false);
            // Navigate to quiz page after successful fetch
        } catch (error) {
            console.error('Failed to fetch quiz:', error);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="w-full max-w-3xl mx-auto py-4">
            <div className="space-y-6">
                <div className="mb-6">
                    <h3 className="font-bold text-xl md:text-2xl mb-4">Help us create your quiz</h3>
                    <fieldset className="fieldset w-full">
                        <legend className="fieldset-legend">Provide your notes and we'll generate the quiz for you</legend>
                        <textarea 
                            name="userNotes"
                            value={formData.userNotes}
                            className="textarea h-28 md:h-36 w-full rounded-2xl" 
                            placeholder="Share the key topics, concepts, or content you want to be included in the quiz. The more specific, the better!"
                            onChange={handleFormDataChange}
                            required
                        />
                    </fieldset>
                </div>

                <div className="mb-6">
                    <fieldset className="fieldset w-full">
                        <legend className="fieldset-legend">Additional Notes (Optional)</legend>
                        <textarea 
                            name="additionalNotes"
                            value={formData.additionalNotes}
                            className="textarea h-20 md:h-24 w-full rounded" 
                            placeholder="Any extra instructions or preferences for your quiz (e.g., difficulty level, types of questions)?"
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
                        <legend className="fieldset-legend">Number of Options per Question</legend>
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
                        {loading ? 'Generating Quiz...' : 'Generate Quiz'}
                    </button>
                </div>
            </div>
        </form>
    );
};

export default QuizParams;
