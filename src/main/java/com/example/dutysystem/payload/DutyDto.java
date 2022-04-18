package com.example.dutysystem.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DutyDto {
    private Long id;
    @NotNull
    private String name;

    private String description;

    private List<Long> userIdList;

    private List<UserDto> userDtoList;

    private UserDto currentDuty;

    private boolean active=true;
}
