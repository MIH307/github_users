package com.example.githubusers.web.models;

import jakarta.validation.constraints.NotBlank;

public record LoginUser(
        @NotBlank(message = "User Name is mandatory") String username,
        @NotBlank(message = "Password is mandatory") String password
) { }
