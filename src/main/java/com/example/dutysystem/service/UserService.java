package com.example.dutysystem.service;

import com.example.dutysystem.entity.User;
import com.example.dutysystem.payload.ApiResponse;
import com.example.dutysystem.payload.UserDto;
import com.example.dutysystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CastService castService;

    public ApiResponse updateUser(UserDto userDto,Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())
            return new ApiResponse("User not found",false);
        User user = optionalUser.get();
        user.setFullName(userDto.getFullName());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        return new ApiResponse("User updated",true);
    }

    public HttpEntity<?> getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())
            return ResponseEntity.status(404).body(new ApiResponse("User not found",false));
        return ResponseEntity.ok(castService.toUserDto(optionalUser.get()));
    }

    public HttpEntity<?> getAllUsers() {
        List<User> allByEnabled = userRepository.findAllByEnabled(true);
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : allByEnabled) {
            userDtoList.add(castService.toUserDto(user));
        }
        return ResponseEntity.ok(userDtoList);
    }

    public ApiResponse deleteUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent())
            return new ApiResponse("User not found",false);
        User user = optionalUser.get();
        user.setEnabled(false);
        userRepository.save(user);
        return new ApiResponse("User deleted",true);
    }

    public HttpEntity<?> getUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsernameAndEnabled(username,true);
        return ResponseEntity.status(optionalUser.isPresent()?200:409).body(optionalUser.orElse(null));
    }

    public HttpEntity<?> searchUser(String username) {
        List<User> search = userRepository.search(username);
        return ResponseEntity.ok(search);
    }
}
