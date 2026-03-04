package com.JaniITmer.webapp.service;

import com.JaniITmer.webapp.dto.RegisterRequest;
import com.JaniITmer.webapp.entity.Role;
import com.JaniITmer.webapp.entity.User;
import com.JaniITmer.webapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username already  taken");

        }
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already in use");

        }

        User user=new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(Role.USER));
        userRepository.save(user);
    }
}
