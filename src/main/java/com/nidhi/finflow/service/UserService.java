package com.nidhi.finflow.service;

import com.nidhi.finflow.dto.CreateUserRequest;
import com.nidhi.finflow.entity.User;
import com.nidhi.finflow.exception.ResourceNotFoundException;
import com.nidhi.finflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public User createUser(CreateUserRequest createUserRequest){
        User user = User.builder()
                .name(createUserRequest.getName())
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword())
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
        return "User with " + id + " deleted";
    }
    public User updateUser(Long id, CreateUserRequest updateUserRequest){
        Optional<User> existingUser = userRepository.findById(id);
        if(existingUser.isEmpty()){
            throw new ResourceNotFoundException("User not found");
        }
        User user = existingUser.get();
        if(updateUserRequest.getName() != null){
            user.setName(updateUserRequest.getName());
        }
        if(updateUserRequest.getEmail() != null){
            user.setEmail(updateUserRequest.getEmail());
        }
        if(updateUserRequest.getPassword() != null){
            user.setPassword(updateUserRequest.getPassword());
        }
        if(updateUserRequest.getRole() != null){
            user.setRole(updateUserRequest.getRole());
        }
        return userRepository.save(user);
    }
}