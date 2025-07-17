package com.youngineer.backend.services.implementations;

import com.youngineer.backend.models.User;
import com.youngineer.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public UserDetails loadUserByUsername(String emailId) {

        User user = repository.findByEmailId(emailId).orElseThrow(() ->
                new EntityNotFoundException(String.format("User does not exist, email: %s", emailId)));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmailId())
                .password(user.getPassword())
                .build();
    }
}

