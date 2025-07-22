import { BASE_AUTH_URL, MY_HEADER } from "../utils/constants";
import type { AuthCredentials, SignupCredentials } from "../utils/interfaces";

export const handleLogin = async (loginData: AuthCredentials): Promise<string> => {
    const url = BASE_AUTH_URL + "/login";
    const request = new Request(url, {
        method: "POST",
        body: JSON.stringify(loginData),
        headers: MY_HEADER,
        credentials: 'include'
    });

    try {
        const response = await fetch(request);
        const data = await response?.json();

        if(!response.ok) {
            return Promise.reject(data.message || 'Login failed. Please try again.')
        } else {
            return 'Login successful!';
        }
    } catch (error) {
        return Promise.reject('Network error. Please check your connection.');
    }

};


export const handleSignup = async (signupData: SignupCredentials): Promise<string> => {
    const url = BASE_AUTH_URL + "/signup";
    const request = new Request(url, {
        method: "POST",
        body: JSON.stringify(signupData),
        headers: MY_HEADER,
        credentials: 'include'
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