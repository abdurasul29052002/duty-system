package com.example.dutysystem.service;

import com.example.dutysystem.entity.Complete;
import com.example.dutysystem.entity.Duty;
import com.example.dutysystem.entity.User;
import com.example.dutysystem.payload.ApiResponse;
import com.example.dutysystem.payload.DutyDto;
import com.example.dutysystem.payload.UserDto;
import com.example.dutysystem.payload.VoteDto;
import com.example.dutysystem.repository.CompleteRepository;
import com.example.dutysystem.repository.DutyRepository;
import com.example.dutysystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DutyService {

    @Autowired
    DutyRepository dutyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompleteRepository completeRepository;
    @Autowired
    CastService castService;

    public ApiResponse addDuty(DutyDto dutyDto) {
        Duty duty = new Duty();
        duty.setName(dutyDto.getName());
        duty.setDescription(dutyDto.getDescription());
        duty.setUsers(new ArrayList<>());
        Duty savedDuty = dutyRepository.save(duty);
        return new ApiResponse("Duty saved", true, savedDuty.getId());
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
            if (!completeRepository.existsByUserIdAndDutyId(user.getId(), duty.getId())) {
                Complete complete = new Complete();
                complete.setDuty(duty);
                complete.setUser(user);
                completeRepository.save(complete);
            }
            if (!duty.getUsers().contains(user)) {
                duty.getUsers().add(user);
            }
        }
        if (duty.getCurrentDuty() == null) {
            Optional<User> firstDuty = userRepository.findById(dutyDto.getUserIdList().get(0));
            duty.setCurrentDuty(firstDuty.orElse(null));
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
        return new ApiResponse("Duty deleted", true);
    }

    public ApiResponse deleteUserFromDuty(Long userId, Long id) {
        Optional<Duty> optionalDuty = dutyRepository.findById(id);
        if (!optionalDuty.isPresent())
            return new ApiResponse("Duty not found", false);
        Duty duty = optionalDuty.get();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent())
            return new ApiResponse("User not found", false);
        duty.getUsers().remove(optionalUser.get());
        dutyRepository.save(duty);
        return new ApiResponse("All users deleted from Duty", true);
    }

    public List<DutyDto> getAllDuties() {
        List<Duty> dutyList = dutyRepository.findAllByActive(true);
        List<DutyDto> dutyDtoList = new ArrayList<>();
        for (Duty duty : dutyList) {
            dutyDtoList.add(castService.toDutyDto(duty));
        }
        return dutyDtoList;
    }

    public HttpEntity<?> getDutyById(Long id) {
        Optional<Duty> optionalDuty = dutyRepository.findByIdAndActive(id, true);
        if (!optionalDuty.isPresent())
            return ResponseEntity.status(409).body(new ApiResponse("Duty not found", false));
        Duty duty = optionalDuty.get();
        return ResponseEntity.ok(castService.toDutyDto(duty));
    }

    public HttpEntity<?> getDutyByUserId(Long userId) {
        List<Duty> byUser = dutyRepository.findByUser(userId);
        List<DutyDto> dutyDtoList = new ArrayList<>();
        for (Duty duty : byUser) {
            dutyDtoList.add(castService.toDutyDto(duty));
        }
        return ResponseEntity.ok(dutyDtoList);
    }

    public HttpEntity<?> getCurrentDutyByDutyId(Long dutyId) {
        try {
            Optional<Duty> optionalDuty = dutyRepository.findById(dutyId);
            Duty duty = optionalDuty.orElse(new Duty());
            UserDto userDto = castService.toUserDto(duty.getCurrentDuty());
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(409).body(new ApiResponse("Something error", false));
        }
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
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long dutyAdminId = duty.getCreatedBy();
        if (!Objects.equals(principal.getId(), dutyAdminId)) {
            return new ApiResponse("You is not owner this duty", false);
        }
        for (Complete complete : user.getCompleteList()) {
            if (Objects.equals(complete.getDuty().getId(), duty.getId())) {
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
        return new ApiResponse("This user is current duty", true);
    }

    public HttpEntity<?> getDutyByAdminId(Long adminId) {
        List<DutyDto> dutyDtoList = new ArrayList<>();
        for (Duty duty : dutyRepository.findAllByCreatedByAndActive(adminId, true)) {
            dutyDtoList.add(castService.toDutyDto(duty));
        }
        return ResponseEntity.ok(dutyDtoList);
    }
}
