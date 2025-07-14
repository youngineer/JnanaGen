package com.youngineer.backend.services.implementations;

import com.youngineer.backend.dto.responses.UserDto;
import com.youngineer.backend.dto.requests.UserLoginRequest;
import com.youngineer.backend.dto.requests.UserSignUpRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.models.User;
import com.youngineer.backend.repository.UserRepository;
import com.youngineer.backend.services.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;


@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private static final String SUCCESS_MESSAGE = "OK";


    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseDto userSignup(UserSignUpRequest userSignUpRequest) {
        if (userSignUpRequest == null) {
            return new ResponseDto("Invalid user data", null);
        }

        String requestEmailId = userSignUpRequest.emailId();

        try {
            if (Boolean.FALSE.equals(userRepository.existsByEmailId(requestEmailId))) {
                User user = convertToUserEntity(userSignUpRequest);
                User savedUser = userRepository.save(user);
                UserDto savedUserDto = convertToUserDto(savedUser);
                return new ResponseDto("OK", savedUserDto);
            } else {
                return new ResponseDto("Email already registered", null);
            }

        } catch (Exception e) {
            return new ResponseDto("Error creating user " + e, null);
        }
    }

    @Override
    public ResponseDto userLogin(UserLoginRequest userLoginRequest) {
        String emailId = userLoginRequest.emailId().trim();
        String userPassword = userLoginRequest.password();
        try {
            Optional<User> userOptional = userRepository.findByEmailId(emailId);
            if(userOptional.isEmpty()) throw new Exception("email id not registered");

            User dbUser = userOptional.get();
            if(!userPassword.equals(dbUser.getPassword())) throw new Exception("incorrect credentials");

            return new ResponseDto(SUCCESS_MESSAGE, "jwt_token");
        } catch (Exception e) {
            return new ResponseDto("Error logging in: " + e, null);
        }
    }

    private UserDto convertToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmailId(), user.getQuizzes(), user.getQuizResults());
    }

    private User convertToUserEntity(UserSignUpRequest userSignUpRequest) {
        User user = new User();

        user.setName(userSignUpRequest.name());
        user.setEmailId(userSignUpRequest.emailId());
        user.setPassword(userSignUpRequest.password());

        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        user.setCreatedAt(currentTimeStamp);
        user.setUpdatedAt(currentTimeStamp);

//        user.setQuizzes(new ArrayList<>());
//        user.setQuizResults(new ArrayList<>());

        return user;
    }
}
