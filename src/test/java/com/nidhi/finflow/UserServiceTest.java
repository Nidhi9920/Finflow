package com.nidhi.finflow;

import com.nidhi.finflow.dto.CreateUserRequest;
import com.nidhi.finflow.dto.LoginRequest;
import com.nidhi.finflow.dto.UserResponse;
import com.nidhi.finflow.entity.Role;
import com.nidhi.finflow.entity.User;
import com.nidhi.finflow.exception.EmailAlreadyExistsException;
import com.nidhi.finflow.exception.InvalidCredentialsException;
import com.nidhi.finflow.exception.ResourceNotFoundException;
import com.nidhi.finflow.repository.UserRepository;
import com.nidhi.finflow.service.JwtService;
import com.nidhi.finflow.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private UserService userService;
    @Test
    void shouldCreateUserSuccessfully(){
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Nidhi");
        request.setEmail("nidhi@gmail.com");
        request.setPassword("password123");
        request.setRole(Role.USER);
        User savedUser =
                User.builder()
                .id(1L)
                .name("Nidhi")
                .email("nidhi@gmail.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
        when(passwordEncoder
                .encode("password123")
        ).thenReturn("encodedPassword");
        when(userRepository
                .save(any(User.class))
        ).thenReturn(savedUser);
        UserResponse response = userService.createUser(request);
        assertNotNull(response);
        assertEquals("Nidhi", response.getName());
        assertEquals("nidhi@gmail.com", request.getEmail());
        verify(userRepository).save(any(User.class));
    }
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists(){
        CreateUserRequest request = new CreateUserRequest();
        request.setName("Nidhi");
        request.setEmail("nidhi@gmail.com");
        request.setPassword("password123");
        request.setRole(Role.USER);
        when(userRepository.existsByEmail("nidhi@gmail.com")).thenReturn(true);
        assertThrows(EmailAlreadyExistsException.class, ()->userService.createUser(request));
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    void shouldLoginSuccessfully(){
        LoginRequest request = new LoginRequest();
        request.setEmail("nidhi@gmail.com");
        request.setPassword("password123");
        User user = User.builder()
                .email("nidhi@gmail.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
        when(userRepository.findByEmail("nidhi@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken("nidhi@gmail.com", "USER")).thenReturn("fake-jwt-token");
        String token = userService.loginUser(request);
        assertEquals("fake-jwt-token", token);
        verify(jwtService).generateToken("nidhi@gmail.com", "USER");
    }
    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid(){
        LoginRequest request = new LoginRequest();
        request.setEmail("nidhi@gmail.com");
        request.setPassword("wrongPassword");
        User user = User.builder()
                .email("nidhi@gmail.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
        when(userRepository.findByEmail("nidhi@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);
        assertThrows(InvalidCredentialsException.class, ()-> userService.loginUser(request));
        verify(jwtService, never()).generateToken(anyString(), anyString());
    }
}
