package com.vn.backend.services;

import com.vn.backend.configs.jwt.JwtTokenPovider;
import com.vn.backend.dto.request.LoginRequest;
import com.vn.backend.dto.request.SignUpRequest;
import com.vn.backend.dto.response.JwtResponse;
import com.vn.backend.dto.response.MessageResponse;
import com.vn.backend.entities.User;
import com.vn.backend.enums.Role;
import com.vn.backend.exceptions.InvalidDataException;
import com.vn.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService{

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenPovider tokenPovider;

    // (Spring Security sẽ gọi)
    @Override
    public UserDetails loadUserByUsername(String emails) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(emails)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    // Lấy User hiện tại từ context
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return null;
        String emails = authentication.getName();
        return userRepository.findByEmail(emails).orElse(null);
    }


    public MessageResponse registerUser(SignUpRequest signUpRequest){
        logger.info("[IN] Register User {}", signUpRequest.getEmail());
            if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
                throw new InvalidDataException("Mật khẩu không khớp với xác nhận mật khẩu");
            }
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                throw new InvalidDataException("Đã tồn tại Email: " + signUpRequest.getEmail());
            }
            User user = new User();
            user.setPassword(encoder.encode(signUpRequest.getPassword()));
            user.setEmail(signUpRequest.getEmail());
            user.setIsActive(true);
            // Set role mặc định là USER
            user.setRole(Role.USER);
        userRepository.save(user);
        logger.info("[OUT] Register User {}", signUpRequest.getEmail());
        return new MessageResponse("Đăng ký thành công");
    }

    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String jwt = tokenPovider.generateToken(customUserDetails);
        List<String> listRoles = customUserDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());
        return new JwtResponse(jwt, customUserDetails.getUsername(),
                customUserDetails.getPhone(), listRoles);
    }
}
