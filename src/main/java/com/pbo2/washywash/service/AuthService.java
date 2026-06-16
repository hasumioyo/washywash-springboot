package com.pbo2.washywash.service;

import org.springframework.stereotype.Service;

import com.pbo2.washywash.model.User;
import com.pbo2.washywash.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }
    
}
