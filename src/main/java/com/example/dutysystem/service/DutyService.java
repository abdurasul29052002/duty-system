package com.example.dutysystem.service;

import com.example.dutysystem.entity.Complete;
import com.example.dutysystem.entity.Duty;
import com.example.dutysystem.entity.User;
import com.example.dutysystem.payload.ApiResponse;
import com.example.dutysystem.payload.DutyDto;
import com.example.dutysystem.payload.VoteDto;
import com.example.dutysystem.repository.CompleteRepository;
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
    @Autowired
    CompleteRepository completeRepository;

    public ApiResponse addDuty(DutyDto dutyDto) {
        Duty duty = new Duty();
        duty.setName(dutyDto.getName());
        duty.setDescription(dutyDto.getDescription());
        duty.setUsers(new ArrayList<>());
        dutyRepository.save(duty);
        return new ApiResponse("Duty saved", true);
    }

    public ApiResponse addUserToDuty(DutyDto dutyDto, Long id) {
        Optional<Duty> optionalDuty = dutyRepository.findById(id);
        if (!optionalDuty.isPresent())
            return new ApiResponse("Duty not found", false);
        Duty duty = optionalDuty.get();
        for (Long userId : dutyDto.getUserIdList()) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent())
                return new ApiResponse("User not found", false);
            User user = optionalUser.get();
            duty.getUsers().add(user);
            Complete complete = new Complete();
            complete.setDuty(duty);
            complete.setUser(user);
            completeRepository.save(complete);
        }
        dutyRepository.save(duty);
        return new ApiResponse("All users added to Duty", true);
    }

    public ApiResponse deleteDuty(Long id) {
        Optional<Duty> optionalDuty = dutyRepository.findById(id);
        if (!optionalDuty.isPresent())
            return new ApiResponse("Duty not found", false);
        Duty duty = optionalDuty.get();
        duty.setActive(false);
        dutyRepository.save(duty);
        return new ApiResponse("Duty deleted", false);
    }

    public ApiResponse deleteUserFromDuty(DutyDto dutyDto, Long id) {
        Optional<Duty> optionalDuty = dutyRepository.findById(id);
        if (!optionalDuty.isPresent())
            return new ApiResponse("Duty not found", false);
        Duty duty = optionalDuty.get();
        for (Long userId : dutyDto.getUserIdList()) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent())
                return new ApiResponse("User not found", false);
            duty.getUsers().remove(optionalUser.get());
        }
        dutyRepository.save(duty);
        return new ApiResponse("All users deleted from Duty", true);
    }

    public List<Duty> getAllDuties() {
        return dutyRepository.findAll();
    }

    public HttpEntity<?> getDutyById(Long id) {
        Optional<Duty> optionalDuty = dutyRepository.findById(id);
        return ResponseEntity.status(optionalDuty.isPresent() ? 200 : 409).body(optionalDuty.orElse(null));
    }

    public HttpEntity<?> getDutyByUserId(Long userId) {
        List<Duty> byUser = dutyRepository.findByUser(userId);
        return ResponseEntity.ok(byUser);
    }

    public HttpEntity<?> getCurrentDutyByDutyId(Long dutyId) {
        Optional<Duty> optionalDuty = dutyRepository.findById(dutyId);
        return ResponseEntity.status(optionalDuty.isPresent() ? 200 : 409).body(optionalDuty.get().getCurrentDuty());
    }

    public ApiResponse completeDuty(VoteDto voteDto, Long id) {
        Optional<Duty> optionalDuty = dutyRepository.findById(id);
        if (!optionalDuty.isPresent())
            return new ApiResponse("Duty not found", false);
        Duty duty = optionalDuty.get();
        Optional<User> optionalUser = userRepository.findById(voteDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("User not found", false);
        User user = optionalUser.get();
        for (Complete complete : user.getCompleteList()) {
            if (complete.getDuty().getId() == duty.getId()) {
                complete.setCompleted(true);
                completeRepository.save(complete);
                return new ApiResponse("Completed", true);
            }
        }
        return new ApiResponse("Error", false);
    }

    public ApiResponse setCurrentDuty(Long dutyId, Long userId) {
        Optional<Duty> optionalDuty = dutyRepository.findById(dutyId);
        if (!optionalDuty.isPresent())
            return new ApiResponse("Duty not found", false);
        Duty duty = optionalDuty.get();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent())
            return new ApiResponse("User not found", false);
        User user = optionalUser.get();
        for (Complete complete : user.getCompleteList()) {
            if (complete.getDuty().equals(duty) && complete.isCompleted())
                return new ApiResponse("This user already completed this duty", false);
        }
        duty.setCurrentDuty(user);
        dutyRepository.save(duty);
        return new ApiResponse("This user is current duty",true);
    }
}
