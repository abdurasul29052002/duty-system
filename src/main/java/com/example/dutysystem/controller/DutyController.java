package com.example.dutysystem.controller;

import com.example.dutysystem.payload.ApiResponse;
import com.example.dutysystem.payload.DutyDto;
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
        return dutyService.getAllDuties();
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getDutyById(@PathVariable Long id){
        return dutyService.getDutyById(id);
    }

    @GetMapping("/byUser/{userId}")
    public HttpEntity<?> getDutyByUserId(@PathVariable Long userId) {
        return dutyService.getDutyByUserId(userId);
    }

    @PutMapping("/addUser/{id}")
    public HttpEntity<?> addUserToDuty(@RequestBody DutyDto dutyDto,@PathVariable Long id){
        ApiResponse apiResponse = dutyService.addUserToDuty(dutyDto,id);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }

    @PutMapping("/deleteUser/{id}")
    public HttpEntity<?> deleteUserFromDuty(@RequestBody DutyDto dutyDto, @PathVariable Long id){
        ApiResponse apiResponse = dutyService.deleteUserFromDuty(dutyDto,id);
        return ResponseEntity.status(apiResponse.isSuccess()?204:409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteDuty(@PathVariable Long id){
        ApiResponse apiResponse = dutyService.deleteDuty(id);
        return ResponseEntity.status(apiResponse.isSuccess()?204:409).body(apiResponse);
    }
}
