package com.example.recyclingAppBackend.service;

import com.example.recyclingAppBackend.dto.LoginRequest;
import com.example.recyclingAppBackend.dto.RegisterRequest;
import com.example.recyclingAppBackend.exception.ResourceNotFoundException;
import com.example.recyclingAppBackend.model.User;
import com.example.recyclingAppBackend.repository.UserRepository;
import com.example.recyclingAppBackend.security.jwt.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private static final Set<String> ALLOWED_ROLES = Set.of("STUDENT", "TEACHER", "PARENT");

    public UserService(UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<?> registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username is already taken"));
        }
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is already in use"));
        }
        if (!ALLOWED_ROLES.contains(request.role())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid user role specified"));
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setEmail(request.email());
        user.setRole(request.role());

        if (request.role().equals("STUDENT")) {
            if (request.parentId() == null || request.parentId().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "A parent or teacher ID is required for student registration."));
            }

            User parent = findUserById(request.parentId());
            if (!parent.getRole().equals("PARENT") && !parent.getRole().equals("TEACHER")) {
                return ResponseEntity.badRequest().body(Map.of("error", "The specified parent ID does not belong to a valid PARENT or TEACHER account."));
            }
            user.setParentId(request.parentId());
            user.setActive(false);
        } else {
            user.setActive(true);
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully. Student accounts require parent approval."));
    }

    public ResponseEntity<?> authenticateUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        return ResponseEntity.ok(response);
    }

    public String getUserIdFromPrincipal(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found from principal."));
    }

    public User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }
}