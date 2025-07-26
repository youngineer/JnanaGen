
export const BASE_AUTH_URL:string = "https://jnanagen.onrender.com/auth";
export const BASE_QUIZ_URL:string = "https://jnanagen.onrender.com/user";

// export const BASE_AUTH_URL:string = "http://localhost:8080/auth";
// export const BASE_QUIZ_URL:string = "http://localhost:8080/user";

// export const BASE_AUTH_URL:string = `${process.env.REACT_APP_BACKEND_URL}/auth`;
// export const BASE_QUIZ_URL:string = `${process.env.REACT_APP_BACKEND_URL}/user`;

export const MY_HEADER: Headers = new Headers({
    'Content-Type': 'application/json',
    'Accept': 'application/json'
});