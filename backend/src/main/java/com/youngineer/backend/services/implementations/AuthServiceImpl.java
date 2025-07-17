package com.youngineer.backend.services.implementations;

import com.youngineer.backend.dto.requests.LoginRequest;
import com.youngineer.backend.dto.requests.SignUpRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.models.User;
import com.youngineer.backend.repository.UserRepository;
import com.youngineer.backend.services.AuthService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;


@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private static final String SUCCESS_MESSAGE = "OK";


    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseDto signup(SignUpRequest signUpRequest) {
        System.out.println("Entered signup service");
        if (signUpRequest == null) {
            return new ResponseDto("Invalid user data", null);
        }

        String userEmailId = signUpRequest.emailId();

        try {
            if (Boolean.FALSE.equals(userRepository.existsByEmailId(userEmailId))) {
                User user = convertToUserEntity(signUpRequest);
                userRepository.save(user);
                return new ResponseDto("OK", null);
            } else {
                throw new Exception(String.format("User with the email address '%s' already exists.", userEmailId));
            }

        } catch (Exception e) {
            return new ResponseDto("Error creating user: " + e, null);
        }
    }

    @Override
    public ResponseDto login(LoginRequest loginRequest) {
        System.out.println("Entered login service");
        String emailId = loginRequest.emailId().trim();
        String userPassword = loginRequest.password();
        try {
            User user = userRepository.findByEmailId(emailId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Email id: %s not registered", emailId)));

            if(!checkPassword(userPassword, user.getPassword())) throw new Exception("incorrect credentials");

            return new ResponseDto(SUCCESS_MESSAGE, "jwt_token");
        } catch (Exception e) {
            return new ResponseDto("Error logging in: " + e, null);
        }
    }

    private User convertToUserEntity(SignUpRequest signUpRequest) {
        User user = new User();
        String hashedPassword = encodePassword(signUpRequest.password());

        user.setName(signUpRequest.name());
        user.setEmailId(signUpRequest.emailId());
        user.setPassword(hashedPassword);

        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        user.setCreatedAt(currentTimeStamp);
        user.setUpdatedAt(currentTimeStamp);

        return user;
    }

    private String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    private Boolean checkPassword(String userPassword, String hashedPassword) {
        return BCrypt.checkpw(userPassword, hashedPassword);
    }
}
