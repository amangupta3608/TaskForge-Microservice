package com.Task_Forge.Microservice.Service;

import com.Task_Forge.Microservice.DTO.LoginRequest;
import com.Task_Forge.Microservice.DTO.SignupRequest;
import com.Task_Forge.Microservice.ENUM.RoleType;
import com.Task_Forge.Microservice.Entity.User;
import com.Task_Forge.Microservice.Entity.Role;
import com.Task_Forge.Microservice.Exception.ResourceNotFoundException;
import com.Task_Forge.Microservice.Repository.UserRepository;
import com.Task_Forge.Microservice.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Transactional
    public String registerUser(SignupRequest signupRequest){
        if(userRepository.findByEmail(signupRequest.getEmail()) != null){
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRole(signupRequest.getRole() != null ? signupRequest.getRole() : RoleType.DEVELOPER);
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signupRequest.getEmail(), signupRequest.getPassword())
        );

        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional
    public String authenticateUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        return jwtTokenProvider.generateToken(authentication);
    }

    public User getUserById(UUID userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }
}
