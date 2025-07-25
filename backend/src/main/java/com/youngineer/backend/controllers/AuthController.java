package com.youngineer.backend.controllers;

import com.youngineer.backend.dto.requests.LoginRequest;
import com.youngineer.backend.dto.requests.SignUpRequest;
import com.youngineer.backend.dto.responses.ResponseDto;
import com.youngineer.backend.services.AuthService;
import com.youngineer.backend.utils.JwtHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@CrossOrigin(origins = "${frontend.url}")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }


    @GetMapping("/checkAuth")
    public ResponseEntity<ResponseDto> checkAuth(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        System.out.println(token);

        if (token != null) {
            String emailId = JwtHelper.extractEmailId(token);
            if(JwtHelper.validateToken(token, emailId))
                return ResponseEntity.ok(new ResponseDto("authenticated", true));
            }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ResponseDto("Invalid login", false));
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signup(@Valid @RequestBody SignUpRequest request) {
        try {
            ResponseDto responseDto = authService.signup(request);

            if ("OK".equals(responseDto.message())) {
                return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDto(responseDto.message(), null));
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage(), null));
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("An unexpected error occurred: " + e.getMessage(), null));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseDto> userLogin(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.emailId(), request.password()));

            if (authentication.isAuthenticated()) {
                String accessToken = JwtHelper.generateToken(request.emailId());

                ResponseCookie cookie = ResponseCookie.from("token", accessToken)
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
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDto("Authentication failed", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("An unexpected error occurred: " + e.getMessage(), null));
        }
    }


    @GetMapping("/logout")
    public ResponseEntity<ResponseDto> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofSeconds(0))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(new ResponseDto("Successfully logged out", "Logout successful!"));
    }

}
