package com.example.dutysystem.payload;

import com.example.dutysystem.entity.Complete;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String fullName;

    private String username;

    private String password;

    private List<CompleteDto> completeDtoList = new ArrayList<>();
}
