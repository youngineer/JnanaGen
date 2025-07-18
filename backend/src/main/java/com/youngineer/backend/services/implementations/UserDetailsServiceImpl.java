package com.youngineer.backend.services.implementations;

import com.youngineer.backend.models.User;
import com.youngineer.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user from database
        User user = userRepository.findByEmailId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Make sure to return authorities - even if it's just a default role
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmailId())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}

