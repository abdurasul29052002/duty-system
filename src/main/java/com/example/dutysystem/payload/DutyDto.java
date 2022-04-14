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
    @NotNull
    private String name;

    private String description;

    private List<Long> userIdList;

    private boolean active=true;
}
