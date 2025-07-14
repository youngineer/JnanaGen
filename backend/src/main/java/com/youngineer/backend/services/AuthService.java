package com.youngineer.backend.services;

import com.youngineer.backend.dto.requests.UserLoginRequest;
import com.youngineer.backend.dto.requests.UserSignUpRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {
    ResponseDto userSignup(UserSignUpRequest userSignUpRequest);
    ResponseDto userLogin(UserLoginRequest userLoginRequest);
}
