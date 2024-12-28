package com.example.githubusers.services;

import com.example.githubusers.data.entities.UserEntity;
import com.example.githubusers.data.repository.UserRepository;
import com.example.githubusers.web.errors.NotFoundException;
import com.example.githubusers.web.models.LoginUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserEntity authenticateUser(LoginUser input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.username(),
                        input.password()
                )
        );
        Optional<UserEntity> optionalUserEntity = userRepository.findByName(input.username());
        if (optionalUserEntity.isEmpty()){
            throw new NotFoundException("Check username and password is it correct.");
        }
        return optionalUserEntity.get();
    }

}
