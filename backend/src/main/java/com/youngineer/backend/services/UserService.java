package com.youngineer.backend.services;

import com.youngineer.backend.dto.requests.UserDto;
import com.youngineer.backend.dto.requests.UserSignUpRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ResponseDto getAllUsers();
    ResponseDto getUserByEmailId(String emailId);
    ResponseDto saveUser(UserSignUpRequest userSignUpRequest);
}
