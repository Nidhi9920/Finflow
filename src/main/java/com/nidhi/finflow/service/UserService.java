package com.nidhi.finflow.service;

import com.nidhi.finflow.dto.CreateUserRequest;
import com.nidhi.finflow.dto.LoginRequest;
import com.nidhi.finflow.dto.UpdateUserRequest;
import com.nidhi.finflow.entity.User;
import com.nidhi.finflow.exception.EmailAlreadyExistsException;
import com.nidhi.finflow.exception.InvalidCredentialsException;
import com.nidhi.finflow.exception.ResourceNotFoundException;
import com.nidhi.finflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public User createUser(CreateUserRequest createUserRequest){
        if(userRepository.existsByEmail(createUserRequest.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        User user = User.builder()
                .name(createUserRequest.getName())
                .email(createUserRequest.getEmail())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .role(createUserRequest.getRole())
                .build();
        return userRepository.save(user);
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }
    public String deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
        return "User with id=" + id + " deleted";
    }
    public User updateCurrentUser(UpdateUserRequest updateUserRequest){
        User user = getCurrentUser();
        if(updateUserRequest.getName() != null){
            user.setName(updateUserRequest.getName());
        }
        if(updateUserRequest.getEmail() != null){
            boolean emailExists = userRepository.existsByEmail(updateUserRequest.getEmail());
            boolean sameEmail = user.getEmail().matches(updateUserRequest.getEmail());
            //Email already exist and is it NOT the current user's own email
            if(emailExists && !sameEmail){
                throw new EmailAlreadyExistsException("Email already exists");
            }
        }
        user.setEmail(updateUserRequest.getEmail());
        if(updateUserRequest.getPassword() != null){
            user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        }
        return userRepository.save(user);
    }
    public String loginUser(LoginRequest loginRequest){
       User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        if(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            return jwtService.generateToken(user.getEmail(), user.getRole().name());
        }else{
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }
    public User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }
}