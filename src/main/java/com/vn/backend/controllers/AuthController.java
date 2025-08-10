package com.vn.backend.controllers;

import com.vn.backend.configs.jwt.JwtTokenPovider;
import com.vn.backend.dto.request.LoginRequest;
import com.vn.backend.dto.request.SignUpRequest;
import com.vn.backend.dto.response.JwtResponse;
import com.vn.backend.dto.response.MessageResponse;
import com.vn.backend.dto.response.ResponseData;
import com.vn.backend.dto.response.UserResponse;
import com.vn.backend.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenPovider tokenPovider;
    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/register")
    public ResponseData<MessageResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
        logger.info("[IN] POST /api/auth/signup");
        var response = ResponseData.created(authService.registerUser(signUpRequest));
        logger.info("[OUT] POST /api/auth/signup");
        return response;
    }

    @PostMapping("/login")
    public ResponseData<JwtResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        logger.info("[IN] POST /api/auth/login");
        var response = ResponseData.success(authService.login(loginRequest));
        logger.info("[OUT] POST /api/auth/login");
        return response;
    }

    @GetMapping("/me")
    public ResponseData<UserResponse> getCurrentUser() {
        logger.info("[IN] GET /api/auth/me");
        var response = ResponseData.success(authService.getCurrentUserInfo());
        logger.info("[OUT] GET /api/auth/me");
        return response;
    }
}
