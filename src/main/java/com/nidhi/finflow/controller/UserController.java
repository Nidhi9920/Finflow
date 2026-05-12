package com.nidhi.finflow.controller;

import com.nidhi.finflow.dto.CreateUserRequest;
import com.nidhi.finflow.entity.User;
import com.nidhi.finflow.service.UserService;
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
    public User createUser(@RequestBody CreateUserRequest createUserRequest){
        return userService.createUser(createUserRequest);
    }
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id){
        return userService.deleteUser(id);
    }
    @PutMapping("/{id}")
    public User updateUserById(@PathVariable Long id, @RequestBody CreateUserRequest updateUserRequest){
        return userService.updateUser(id, updateUserRequest);
    }
}
