package com.youngineer.backend.services.implementations;

import com.youngineer.backend.dto.requests.LoginRequest;
import com.youngineer.backend.dto.requests.SignUpRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.models.User;
import com.youngineer.backend.repository.UserRepository;
import com.youngineer.backend.services.AuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
    public ResponseDto userSignup(SignUpRequest signUpRequest) {
        if (signUpRequest == null) {
            return new ResponseDto("Invalid user data", null);
        }

        String requestEmailId = signUpRequest.emailId();

        try {
            if (Boolean.FALSE.equals(userRepository.existsByEmailId(requestEmailId))) {
                User user = convertToUserEntity(signUpRequest);
                userRepository.save(user);
                return new ResponseDto("OK", null);
            } else {
                return new ResponseDto("Email already registered", null);
            }

        } catch (Exception e) {
            return new ResponseDto("Error creating user " + e, null);
        }
    }

    @Override
    public ResponseDto userLogin(LoginRequest loginRequest) {
        String emailId = loginRequest.emailId().trim();
        String userPassword = loginRequest.password();
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

    private User convertToUserEntity(SignUpRequest signUpRequest) {
        User user = new User();

        user.setName(signUpRequest.name());
        user.setEmailId(signUpRequest.emailId());
        user.setPassword(signUpRequest.password());

        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        user.setCreatedAt(currentTimeStamp);
        user.setUpdatedAt(currentTimeStamp);

        return user;
    }
}
