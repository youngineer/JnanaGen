import { useState, type ChangeEvent, type FormEvent, type FC } from "react"
import { handleLogin, handleSignup } from "../services/authServices";
import AlertMessage from "./AlertMessage";

interface SignupInterface {
  name: string;
  emailId: string;
  password: string;
}

interface LoginInterface {
  emailId: string;
  password: string;
}

const AuthPage: FC = () => {
  const [isLogin, setIsLogin] = useState<boolean>(false);
  const [signupInfo, setSignupInfo] = useState<SignupInterface>({
    name: '',
    emailId: '',
    password: ''
  });
  const [loginInfo, setLoginInfo] = useState<LoginInterface>({
    emailId: 'kartik@gmail.com',
    password: 'Kartik@123'
  });

  const [alertInfo, setAlertInfo] = useState<{
        show: boolean;
        isSuccess: boolean;
        message: string;
    }>({
        show: false,
        isSuccess: false,
        message: ''
    });

  const toggleLogin = (): void => {
    setIsLogin(prev => !prev);
  };

  const handleInputChange = (e: ChangeEvent<HTMLInputElement>): void => {
    const { name, value } = e.target;
    if (isLogin) {
      setLoginInfo(prev => ({
        ...prev,
        [name]: value
      }));
    } else {
      setSignupInfo(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const handleSubmit = async(e: FormEvent<HTMLFormElement>): Promise<void> => {
    e.preventDefault();
    try {
      const message = await (isLogin? handleLogin(loginInfo): handleSignup(signupInfo));
      
      setAlertInfo({
        show: true, 
        isSuccess: true,
        message
      });

      if(!isLogin) {
        setSignupInfo({
          name: '',
          emailId: '',
          password: ''
        });

        setIsLogin(true);
      }
      
    } catch (error) {
      setAlertInfo({
        show: true,
        isSuccess: false,
        message: error as string
      });
    };

    setTimeout(() => {
      setAlertInfo(prev => ({
        ...prev,
        show: false
      }))
    }, 3000);
  };


  return (
    <div className="mt-8 flex items-center justify-center">
      {alertInfo.show && (
        <AlertMessage 
            isSuccess={alertInfo.isSuccess} 
            message={alertInfo.message} 
        />
      )}
      <form onSubmit={handleSubmit} className="w-full max-w-md mx-4">
        <fieldset className="fieldset bg-base-200 border-base-300 rounded-box border p-6 shadow-lg">
          <legend className="fieldset-legend text-xl font-semibold">
            {isLogin ? "Login" : "Sign-up"} form
          </legend>

          {!isLogin && (
            <fieldset className="fieldset mb-4">
              <legend className="fieldset-legend">Name</legend>
              <input
                type="text"
                name="name"
                value={signupInfo.name}
                onChange={handleInputChange}
                className="input input-bordered w-full"
                placeholder="Your name"
                required
              />
            </fieldset>
          )}

          <fieldset className="fieldset mb-4">
            <legend className="fieldset-legend">Email ID</legend>
            <input
              type="email"
              name="emailId"
              value={isLogin ? loginInfo.emailId : signupInfo.emailId}
              onChange={handleInputChange}
              className="input input-bordered w-full"
              placeholder="Email"
              required
            />
          </fieldset>

          <fieldset className="fieldset mb-6">
            <legend className="fieldset-legend">Password</legend>
            <input
              type="password"
              name="password"
              value={isLogin ? loginInfo.password : signupInfo.password}
              onChange={handleInputChange}
              className="input input-bordered w-full"
              placeholder="Password"
              required
            />
          </fieldset>

          <button 
            type="submit"
            className="btn btn-neutral w-full mb-4"
          >
            {isLogin ? "Login" : "Sign up"}
          </button>

          <p 
            className="cursor-pointer underline text-center"
            onClick={toggleLogin}
          >
            {isLogin ? "Not registered? Sign up" : "Already registered? Login"}
          </p>
        </fieldset>
      </form>
    </div>
  );
};

export default AuthPage;