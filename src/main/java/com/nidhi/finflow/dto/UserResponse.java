package com.nidhi.finflow.dto;

import com.nidhi.finflow.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
}
