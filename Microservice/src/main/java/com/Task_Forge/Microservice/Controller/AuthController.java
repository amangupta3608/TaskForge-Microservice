package com.Task_Forge.Microservice.Controller;

import com.Task_Forge.Microservice.DTO.LoginRequest;
import com.Task_Forge.Microservice.DTO.SignupRequest;
import com.Task_Forge.Microservice.Security.JwtTokenProvider;
import com.Task_Forge.Microservice.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequest signupRequest){
        String jwt = userService.registerUser(signupRequest);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticationUser(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }
}
