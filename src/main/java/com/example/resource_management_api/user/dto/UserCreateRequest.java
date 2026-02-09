package com.example.resource_management_api.user.dto;

import com.example.resource_management_api.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotNull
    private Role role;
}
