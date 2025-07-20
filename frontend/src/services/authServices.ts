import { BASE_AUTH_URL, MY_HEADER } from "../utils/constants";




interface SignupInterface {
  name: string;
  emailId: string;
  password: string;
}

interface LoginInterface {
  emailId: string;
  password: string;
}


export const handleLogin = async (loginData: LoginInterface): Promise<string> => {
    const url = BASE_AUTH_URL + "/login";
    const request = new Request(url, {
        method: "POST",
        body: JSON.stringify(loginData),
        headers: MY_HEADER
    });

    try {
        const response = await fetch(request);
        const data = await response?.json();
        console.log(data);

        if(!response.ok) {
            return Promise.reject(data.message || 'Login failed. Please try again.')
        } else {
            return 'Login successful!';
        }
    } catch (error) {
        return Promise.reject('Network error. Please check your connection.');
    }

};


export const handleSignup = async (signupData: SignupInterface): Promise<string> => {
    const url = BASE_AUTH_URL + "/signup";
    const request = new Request(url, {
        method: "POST",
        body: JSON.stringify(signupData),
        headers: MY_HEADER
    });

    try {
        const response = await fetch(request);
        const data = await response?.json();

        if(!response.ok) {
            return Promise.reject(data?.message || 
                'Registration failed. Please try again.'
            );
        } else {
            return "Registration successful! Please login";
        }
    } catch (error) {
        return Promise.reject('Network error. Please check your connection.');
    }
};