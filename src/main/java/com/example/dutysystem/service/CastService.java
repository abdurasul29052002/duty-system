package com.example.dutysystem.service;

import com.example.dutysystem.entity.Complete;
import com.example.dutysystem.entity.Duty;
import com.example.dutysystem.entity.User;
import com.example.dutysystem.payload.CompleteDto;
import com.example.dutysystem.payload.DutyDto;
import com.example.dutysystem.payload.UserDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CastService {
    public DutyDto toDutyDto(Duty duty) {
        List<UserDto> userDtoList = new ArrayList<>();
        if (duty!=null) {
            for (User user : duty.getUsers()) {
                userDtoList.add(toUserDto(user));
            }
            return new DutyDto(duty.getId(), duty.getName(), duty.getDescription(), null, userDtoList, toUserDto(duty.getCurrentDuty()), true);
        }
        return new DutyDto();
    }

    public UserDto toUserDto(User user) {
        List<CompleteDto> completeDtoList = new ArrayList<>();
        if (user != null) {
            for (Complete complete : user.getCompleteList()) {
                completeDtoList.add(toCompleteDto(complete));
            }
            return new UserDto(user.getId(), user.getFullName(), user.getUsername(), null, completeDtoList);
        }
        return new UserDto();
    }

    public CompleteDto toCompleteDto(Complete complete) {
        return new CompleteDto(complete.isCompleted(), complete.getDuty().getId(), complete.getUser().getId());
    }

}
