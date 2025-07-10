package com.youngineer.backend.services;

import com.youngineer.backend.dto.requests.UserDto;
import com.youngineer.backend.dto.requests.UserSignUpRequest;

import java.util.List;
import java.util.Optional;

public interface UserServiceImpl {
    List<UserDto> getAllUsers();
    Optional<UserDto> getUserByEmailId(String emailId);
    UserSignUpRequest saveUser(UserSignUpRequest userSignUpRequest);
}
