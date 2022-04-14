package com.example.dutysystem.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotNull
    private String fullName;

    @NotNull
    private String username;

    @NotNull
    private String password;
}
