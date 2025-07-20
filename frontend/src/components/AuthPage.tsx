import { useState, type FC, type JSX } from "react"

const AuthPage: FC = (): JSX.Element => {
  const [isLogin, setIsLogin] = useState<boolean>(false);
  const [signupInfo, setSignupInfo] = useState<string[]>(new Array(3));
  const [loginInfo, setLoginInfo] = useState<string[]>(new Array(3));

  const toggleLogin = (): void => {
    setIsLogin(prev => !prev);
  };

  const handleSubmit = (): void => {
    
  }


  return (
    <div className="mt-8 flex items-center justify-center">
      <fieldset className="fieldset bg-base-200 border-base-300 rounded-box w-full max-w-md mx-4 border p-6 shadow-lg">
        <legend className="fieldset-legend text-xl font-semibold">{isLogin ? "Login" : "Sign-up"} form</legend>

        {!isLogin && (
          <fieldset className="fieldset mb-4">
            <legend className="fieldset-legend">Name</legend>
            <input type="text" className="input input-bordered w-full" placeholder="Your name" />
          </fieldset>
        )}

        <fieldset className="fieldset mb-4">
          <legend className="fieldset-legend">Email ID</legend>
          <input type="email" className="input input-bordered w-full" placeholder="Email" />
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Password</legend>
          <input type="password" className="input input-bordered w-full" placeholder="Password" />
        </fieldset>

        <p className="cursor-pointer underline text-center" onClick={toggleLogin}>
          {isLogin ? "Not registered? Sign up" : "Already registered? Login"}
        </p>

        <button className="btn btn-neutral w-full mt-4">{isLogin ? "Login" : "Sign up"}</button>
      </fieldset>
    </div>
  )
}

export default AuthPage;