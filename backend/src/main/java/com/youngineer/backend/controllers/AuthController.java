package com.youngineer.backend.controllers;

import com.youngineer.backend.dto.requests.LoginRequest;
import com.youngineer.backend.dto.requests.SignUpRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.services.AuthService;
import com.youngineer.backend.utils.JwtHelper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signup(@Valid @RequestBody SignUpRequest request) {
        try {
            ResponseDto responseDto = authService.signup(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage(), null));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> userLogin(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.emailId(), request.password()));
            if (authentication.isAuthenticated()) {
                String accessToken = JwtHelper.generateToken(request.emailId());

                ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(Duration.ofHours(1))
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

                ResponseDto responseDto = new ResponseDto("OK", null);
                return ResponseEntity.ok(responseDto);
            } else {
                throw new BadCredentialsException("Error authenticating the user");
            }
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(new ResponseDto("Invalid credentials", null));
        }  catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("Authentication failed", null));
        }
    }

}
