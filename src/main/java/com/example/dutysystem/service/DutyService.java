package com.example.dutysystem.service;

import com.example.dutysystem.entity.Duty;
import com.example.dutysystem.entity.User;
import com.example.dutysystem.payload.ApiResponse;
import com.example.dutysystem.payload.DutyDto;
import com.example.dutysystem.repository.DutyRepository;
import com.example.dutysystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DutyService {

    @Autowired
    DutyRepository dutyRepository;
    @Autowired
    UserRepository userRepository;

    public ApiResponse addDuty(DutyDto dutyDto) {
        Duty duty = new Duty();
        duty.setName(dutyDto.getName());
        duty.setDescription(dutyDto.getDescription());
        duty.setUsers(new TreeSet<>(Comparator.comparing(User::getFullName)));
        if (addUsersToDutyAndSave(dutyDto, duty))
            return new ApiResponse("Duty saved",true);
        return new ApiResponse("User not found", false);
    }

    private boolean addUsersToDutyAndSave(DutyDto dutyDto, Duty duty) {
        for (Long id : dutyDto.getUserIdList()) {
            Optional<User> optionalUser = userRepository.findById(id);
            if (!optionalUser.isPresent())
                return false;
            duty.getUsers().add(optionalUser.get());
        }
        dutyRepository.save(duty);
        return true;
    }

    public ApiResponse addUserToDuty(DutyDto dutyDto, Long id) {
        Optional<Duty> optionalDuty = dutyRepository.findById(id);
        if (!optionalDuty.isPresent())
            return new ApiResponse("Duty not found",false);
        Duty duty = optionalDuty.get();
        if (addUsersToDutyAndSave(dutyDto, duty)) return new ApiResponse("User not found", false);
        return new ApiResponse("All users added to Duty",true);
    }

    public ApiResponse deleteDuty(Long id) {
        Optional<Duty> optionalDuty = dutyRepository.findById(id);
        if (!optionalDuty.isPresent())
            return new ApiResponse("Duty not found",false);
        Duty duty = optionalDuty.get();
        duty.setActive(false);
        dutyRepository.save(duty);
        return new ApiResponse("Duty deleted",false);
    }

    public ApiResponse deleteUserFromDuty(DutyDto dutyDto, Long id) {
        Optional<Duty> optionalDuty = dutyRepository.findById(id);
        if(!optionalDuty.isPresent())
            return new ApiResponse("Duty not found",false);
        Duty duty = optionalDuty.get();
        for (Long userId : dutyDto.getUserIdList()) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent())
                return new ApiResponse("User not found",false);
            duty.getUsers().remove(optionalUser.get());
        }
        dutyRepository.save(duty);
        return new ApiResponse("All users deleted from Duty",true);
    }

    public HttpEntity<?> getAllDuties() {
        return ResponseEntity.ok(dutyRepository.findAll());
    }

    public HttpEntity<?> getDutyById(Long id) {
        Optional<Duty> optionalDuty = dutyRepository.findById(id);
        return ResponseEntity.status(optionalDuty.isPresent()?200:409).body(optionalDuty.orElse(null));
    }

    public HttpEntity<?> getDutyByUserId(Long userId) {
        List<Duty> byUser = dutyRepository.findByUser(userId);
        return ResponseEntity.ok(byUser);
    }
}
