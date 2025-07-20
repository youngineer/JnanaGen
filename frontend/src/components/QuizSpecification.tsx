import { useState, type ChangeEvent, type FC, type FormEvent, type JSX } from "react";

interface QuizSpecificationState {
    userNotes: string,
    additionalNotes: string,
    totalQuestions: number,
    numberOfOptions: number,
}


const QuizSpecification: FC = (): JSX.Element => {
    const [formData, setFormData] = useState<QuizSpecificationState>({
        userNotes: "",
        additionalNotes: "",
        totalQuestions: 6,
        numberOfOptions: 4
    });


    const handleFormDataChange = (e: ChangeEvent<HTMLTextAreaElement | HTMLInputElement | 
        HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'totalQuestions' ? parseInt(value) : value
        }));
    };


    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        console.log(formData);
        //TODO submit;
    }
    


  return (
    <form onSubmit={handleSubmit} className="pt-4 md:pt-8 px-4 md:px-16 min-w-4xl mx-auto">
    <div className="pt-4 md:pt-8 px-4 md:px-16 min-w-4xl mx-auto">
        <div className="mb-6">
            <h3 className="font-bold text-xl md:text-2xl">Create your quiz</h3>
            <fieldset className="fieldset w-full">
                <legend className="fieldset-legend">Enter your notes and we will generate the quiz for you</legend>
                <textarea 
                    className="textarea h-28 md:h-36 w-full" 
                    placeholder="Your notes here"
                    onChange={handleFormDataChange}
                ></textarea>
            </fieldset>
        </div>

        <div className="mb-6" >
            <fieldset className="fieldset w-full">
                <legend className="fieldset-legend">Additional Notes</legend>
                <textarea 
                    className="textarea h-20 md:h-24 w-full" 
                    placeholder="Additional notes here"
                    onChange={handleFormDataChange}
                ></textarea>
            </fieldset>
        </div>

        <div className="flex flex-col md:flex-row gap-4">
            <fieldset className="w-full md:w-1/2">
                <legend className="fieldset-legend">Number of Questions: {formData.totalQuestions}</legend>
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
                <select defaultValue="4 Options" className="select w-full" onChange={handleFormDataChange}>
                    <option>2 Options</option>
                    <option>3 Options</option>
                    <option>4 Options</option>
                    <option>5 Options</option>
                    <option>6 Options</option>
                </select>
            </fieldset>
        </div>

        <div className="flex justify-center p-8">
            <button type="submit" className="btn btn-xs sm:btn-sm md:btn-md lg:btn-lg xl:btn-xl">Generate Quiz</button>
        </div>
    </div>
    </form>
  )
}

export default QuizSpecification;