package com.example.dutysystem.controller;

import com.example.dutysystem.payload.ApiResponse;
import com.example.dutysystem.payload.DutyDto;
import com.example.dutysystem.payload.VoteDto;
import com.example.dutysystem.service.DutyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/duty")
public class DutyController {

    @Autowired
    DutyService dutyService;

    @PostMapping
    public HttpEntity<?> addDuty(@RequestBody DutyDto dutyDto){
        ApiResponse apiResponse = dutyService.addDuty(dutyDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getAllDuties(){
        return ResponseEntity.ok(dutyService.getAllDuties());
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getDutyById(@PathVariable Long id){
        return dutyService.getDutyById(id);
    }

    @GetMapping("/byUser/{userId}")
    public HttpEntity<?> getDutyByUserId(@PathVariable Long userId) {
        return dutyService.getDutyByUserId(userId);
    }

    @GetMapping("/byAdmin/{adminId}")
    public HttpEntity<?> getDutyByAdminId(@PathVariable Long adminId){
        return dutyService.getDutyByAdminId(adminId);
    }

    @GetMapping("/currentDuty/{dutyId}")
    public HttpEntity<?> getCurrentDutyByDutyId(@PathVariable Long dutyId){
        return dutyService.getCurrentDutyByDutyId(dutyId);
    }

    @PutMapping("/addUser/{id}")
    public HttpEntity<?> addUserToDuty(@RequestBody DutyDto dutyDto,@PathVariable Long id){
        ApiResponse apiResponse = dutyService.addUserToDuty(dutyDto,id);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }

    @PutMapping("/deleteUser")
    public HttpEntity<?> deleteUserFromDuty(@RequestParam Long userId, @RequestParam Long dutyId){
        ApiResponse apiResponse = dutyService.deleteUserFromDuty(userId,dutyId);
        return ResponseEntity.status(apiResponse.isSuccess()?204:409).body(apiResponse);
    }

    @PutMapping("/completeDuty/{id}")
    public HttpEntity<?> completeDuty(@RequestBody VoteDto voteDto,@PathVariable Long id) {
        ApiResponse apiResponse = dutyService.completeDuty(voteDto, id);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }

    @PutMapping("/setCurrentDuty")
    public HttpEntity<?> setCurrentDuty(@RequestParam Long dutyId,@RequestParam Long userId){
        ApiResponse apiResponse = dutyService.setCurrentDuty(dutyId,userId);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteDuty(@PathVariable Long id){
        ApiResponse apiResponse = dutyService.deleteDuty(id);
        return ResponseEntity.status(apiResponse.isSuccess()?204:409).body(apiResponse);
    }
}
