package com.youngineer.backend.services.implementations;

import com.youngineer.backend.dto.requests.UserDto;
import com.youngineer.backend.dto.requests.UserSignUpRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.models.Quiz;
import com.youngineer.backend.models.QuizResult;
import com.youngineer.backend.models.User;
import com.youngineer.backend.repository.UserRepository;
import com.youngineer.backend.services.UserService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private static String message = "OK";
    private static Object content = null;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseDto getAllUsers() {
        try {
            List<User> allUsers = userRepository.findAll();
            List<UserDto> allUserDtos = allUsers.stream()
                    .map(this::convertToUserDto)
                    .toList();

            return new ResponseDto(message, allUserDtos);
        } catch(Exception e) {
            logger.info("Error occurred: " + e);
            return new ResponseDto("Error occurred: " + e, content);
        }
    }

    @Override
    public ResponseDto getUserByEmailId(String emailId) {
        if (emailId == null || emailId.trim().isEmpty()) {
            return new ResponseDto("Email ID cannot be empty", null);
        }

        try {
            Optional<User> userOptional = userRepository.findByEmailId(emailId);
            if (userOptional.isPresent()) {
                UserDto userDto = convertToUserDto(userOptional.get());
                return new ResponseDto("User found", userDto);
            } else {
                return new ResponseDto("User not found", null);
            }
        } catch (Exception e) {
            logger.info("Error retrieving user by email: {}" + emailId + e);
            return new ResponseDto("Error retrieving user", null);
        }
    }

    @Override
    public ResponseDto saveUser(UserSignUpRequest userSignUpRequest) {
        if (userSignUpRequest == null) {
            return new ResponseDto("Invalid user data", null);
        }

        String requestEmailId = userSignUpRequest.emailId();

        try {
            if (userRepository.existsByEmailId(requestEmailId)) {
                return new ResponseDto("Email already registered", null);
            }

            User user = convertToUserEntity(userSignUpRequest);
            User savedUser = userRepository.save(user);
            UserDto savedUserDto = convertToUserDto(savedUser);

            logger.info("User successfully created with email: {}" + requestEmailId);
            return new ResponseDto("User created successfully", savedUserDto);

        } catch (Exception e) {
            logger.info("Error saving user with email: {}" + requestEmailId + e);
            return new ResponseDto("Error creating user", null);
        }
    }

    private UserDto convertToUserDto(User user) {
        return new UserDto(user.getName(), user.getEmailId(), user.getQuizzes(), user.getQuizResults());
    }

    private User convertToUserEntity(UserSignUpRequest userSignUpRequest) {
        User user = new User();

        user.setName(userSignUpRequest.name());
        user.setEmailId(userSignUpRequest.emailId());
        user.setPassword(userSignUpRequest.password());

        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        user.setCreatedAt(currentTimeStamp);
        user.setUpdatedAt(currentTimeStamp);

        user.setQuizzes(new ArrayList<Quiz>());
        user.setQuizResults(new ArrayList<QuizResult>());


        return user;
    }

    private ResponseDto generateResponseDto(String message, Object content) {
        return new ResponseDto(message, content);
    }
}
