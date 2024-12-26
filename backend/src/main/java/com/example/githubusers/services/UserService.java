package com.example.githubusers.services;

import com.example.githubusers.data.entities.UserEntity;
import com.example.githubusers.data.repository.UserRepository;
import com.example.githubusers.web.errors.NotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }







/*    @PostConstruct
    private void init(){
        UserEntity user1 = new UserEntity();
        user1.setName("TestUser");
        user1.setPassword(passwordEncoder.encode("00_Ntcn>ptH_99"));
        userRepository.save(user1);

        UserEntity user2 = new UserEntity();
        user2.setName("DemoUser");
        user2.setPassword(passwordEncoder.encode("33_Ltvj>ptH_77"));
        userRepository.save(user2);
        System.out.println("Users saved!!!");

    }*/

/*    public UserEntity getUserByName(String userName){
        Optional<UserEntity> optionalUser = userRepository.findByName(userName);
        if (optionalUser.isEmpty()){
            throw new NotFoundException("User not found with this name: " + userName);
        }
        return optionalUser.get();
    }

    public Optional<UserEntity> findByUsername(String userName){
        return userRepository.findByName(userName);
    }

    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }*/

}
