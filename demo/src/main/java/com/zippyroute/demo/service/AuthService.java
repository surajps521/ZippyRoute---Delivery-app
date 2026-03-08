package com.zippyroute.demo.service;

import com.zippyroute.demo.dto.AuthResponse;
import com.zippyroute.demo.dto.LoginRequest;
import com.zippyroute.demo.dto.RegisterRequest;
import com.zippyroute.demo.entity.User;
import com.zippyroute.demo.repository.UserRepository;
import com.zippyroute.demo.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(null, null, null, null, "Email already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole("USER");
        user.setIsActive(true);

        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser.getEmail(), savedUser.getId());

        return new AuthResponse(token, savedUser.getId(), savedUser.getEmail(), savedUser.getFullName(), "Registration successful");
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(null, null, null, null, "Invalid email or password");
        }

        if (!user.getIsActive()) {
            return new AuthResponse(null, null, null, null, "User account is inactive");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getId());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), "Login successful");
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User updateUserProfile(Long userId, User updatedUser) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setFullName(updatedUser.getFullName());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            user.setAddress(updatedUser.getAddress());
            user.setUpdatedAt(System.currentTimeMillis());
            return userRepository.save(user);
        }
        return null;
    }
}
