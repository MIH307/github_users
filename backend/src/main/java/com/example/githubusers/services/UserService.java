package com.example.githubusers.services;

import com.example.githubusers.data.entities.UserEntity;
import com.example.githubusers.data.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${database.user1.pass}")
    private String user1Pass;

    @Value("${database.user2.pass}")
    private String user2Pass;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct
    private void init(){
        UserEntity user1 = new UserEntity();
        user1.setName("TestUser");
        user1.setPassword(passwordEncoder.encode(user1Pass));
        saveUserIfNone(user1);

        UserEntity user2 = new UserEntity();
        user2.setName("DemoUser");
        user2.setPassword(passwordEncoder.encode(user2Pass));
        saveUserIfNone(user2);

    }

    private void saveUserIfNone(UserEntity user) {
        Optional<UserEntity> optionalUser = userRepository.findByName(user.getName());
        if (optionalUser.isEmpty()){
            userRepository.save(user);
            System.out.println("User: " + user.getName() + " saved to DB");
        }
    }

}
