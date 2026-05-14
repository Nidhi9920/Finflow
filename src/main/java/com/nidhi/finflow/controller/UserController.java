package com.nidhi.finflow.controller;

import com.nidhi.finflow.dto.CreateUserRequest;
import com.nidhi.finflow.dto.LoginRequest;
import com.nidhi.finflow.entity.User;
import com.nidhi.finflow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping
    public User createUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        return userService.createUser(createUserRequest);
    }
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id){
        return userService.deleteUser(id);
    }
    @PutMapping("/{id}")
    public User updateUserById(@PathVariable Long id, @Valid @RequestBody CreateUserRequest updateUserRequest){
        return userService.updateUser(id, updateUserRequest);
    }
    @PostMapping("/login")
    public String loginUser(@Valid @RequestBody LoginRequest loginRequest){
        return userService.loginUser(loginRequest);
    }
}
