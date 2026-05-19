package com.nidhi.finflow.controller;

import com.nidhi.finflow.dto.CreateUserRequest;
import com.nidhi.finflow.dto.LoginRequest;
import com.nidhi.finflow.dto.UpdateUserRequest;
import com.nidhi.finflow.entity.User;
import com.nidhi.finflow.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "User APIs")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Operation(summary = "Register user", description = "Register a new user in the system")
    @PostMapping("/register")
    public User createUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        return userService.createUser(createUserRequest);
    }
    @Operation(summary = "Get all users", description = "Retrieve all registered users. Accessible only by ADMIN")
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    @Operation(summary = "Delete user", description = "Delete a user by ID. Accessible only by ADMIN")
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id){
        return userService.deleteUser(id);
    }
    @Operation(summary = "Update current user", description = "Update profile details of logged-in user")
    @PutMapping("/me")
    public User updateCurrentUser(@RequestBody UpdateUserRequest updateUserRequest){
        return userService.updateCurrentUser(updateUserRequest);
    }
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    @PostMapping("/login")
    public String loginUser(@Valid @RequestBody LoginRequest loginRequest){
        return userService.loginUser(loginRequest);
    }
    @Operation(summary = "Get current user", description = "Fetch details of currently authenticated user")
    @GetMapping("/me")
    public User getCurrentUser(){
        return userService.getCurrentUser();
    }
}
