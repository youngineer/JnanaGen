import { useState, useRef, useEffect, type ChangeEvent, type FC, type FormEvent } from "react";
import { generateQuiz } from "../services/quizServices";
import { useNavigate } from "react-router";
import type { QuizSpecification } from "../utils/interfaces";

const loadingMessages = [
    "Please wait; Your inputs are being thoroughly analysed...",
    "Generating thoughtful questions...",
    "Almost there! Crafting your quiz...",
    "Finalizing your personalized quiz..."
];

const QuizParams: FC = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState<QuizSpecification>({
        userNotes: "",
        totalQuestions: 6,
        numberOfOptions: 4,
        additionalNotes: "",
    });

    const [loading, setLoading] = useState<boolean>(false);
    const [loadingMsgIdx, setLoadingMsgIdx] = useState<number>(0);
    const intervalRef = useRef<NodeJS.Timeout | null>(null);

    useEffect(() => {
        if (loading) {
            setLoadingMsgIdx(0);
            intervalRef.current = setInterval(() => {
                setLoadingMsgIdx(prev => (prev + 1) % loadingMessages.length);
            }, 7200);
        } else if (intervalRef.current) {
            clearInterval(intervalRef.current);
        }
        return () => {
            if (intervalRef.current) clearInterval(intervalRef.current);
        };
    }, [loading]);

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
            setLoading(true);
            const quizId = await generateQuiz(formData);
            setLoading(false);
            navigate("/quiz/" + quizId);
        } catch (error) {
            setLoading(false);
            console.error('Failed to fetch quiz:', error);
            navigate("/auth");
        }
    };

    return (
        <div className="relative w-full max-w-3xl mx-auto py-4 min-h-[500px]">
            {/* Loading Overlay */}
            {loading && (
                <div className="absolute inset-0 flex flex-col items-center justify-center z-20 rounded-2xl">
                    <span className="loading loading-spinner loading-xl mb-4">{loadingMessages[loadingMsgIdx]}</span>
                    <span className="text-lg font-semibold text-center">{loadingMessages[loadingMsgIdx]}</span>
                </div>
            )}
            {/* Blurred Form */}
            <form 
                onSubmit={handleSubmit} 
                className={`space-y-6 ${loading ? "blur-sm pointer-events-none select-none" : ""}`}
            >
                <div className="mb-6">
                    <h3 className="font-bold text-xl md:text-2xl mb-4">Help me create your quiz</h3>
                    <fieldset className="fieldset w-full">
                        <legend className="fieldset-legend">Provide your notes and I'll generate a quiz for you</legend>
                        <textarea 
                            name="userNotes"
                            value={formData.userNotes}
                            className="textarea h-28 md:h-36 w-full rounded-2xl" 
                            placeholder="Share the key topics, concepts, or content you want to be included in the quiz. The more specific, the better!"
                            onChange={handleFormDataChange}
                            required
                            disabled={loading}
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
                            disabled={loading}
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
                            disabled={loading}
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
                            disabled={loading}
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
                        disabled={!formData.userNotes.trim() || loading}
                    >
                        {loading ? 'Generating Quiz...' : 'Generate Quiz'}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default QuizParams;
