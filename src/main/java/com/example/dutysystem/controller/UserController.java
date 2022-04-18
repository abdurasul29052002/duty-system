package com.example.dutysystem.controller;

import com.example.dutysystem.payload.ApiResponse;
import com.example.dutysystem.payload.UserDto;
import com.example.dutysystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping("/{id}")
    public HttpEntity<?> updateUser(@RequestBody UserDto userDto,@PathVariable Long id){
        ApiResponse apiResponse = userService.updateUser(userDto,id);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/byUsername/{username}")
    public HttpEntity<?> getUserByUsername(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    @GetMapping("/search")
    public HttpEntity<?> searchUser(@RequestParam String username) {
        return userService.searchUser(username);
    }

    @GetMapping
    public HttpEntity<?> getAllUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteUserById(@PathVariable Long id){
        ApiResponse apiResponse = userService.deleteUserById(id);
        return ResponseEntity.status(apiResponse.isSuccess()?204:409).body(apiResponse);
    }
}
