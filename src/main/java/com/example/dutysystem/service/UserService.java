package com.example.dutysystem.service;

import com.example.dutysystem.entity.User;
import com.example.dutysystem.payload.ApiResponse;
import com.example.dutysystem.payload.UserDto;
import com.example.dutysystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

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
        return ResponseEntity.status(optionalUser.isPresent()?200:409).body(optionalUser.orElse(null));
    }

    public HttpEntity<?> getAllUsers() {
        List<User> allByEnabled = userRepository.findAllByEnabled(true);
        TreeSet<User> treeSet = new TreeSet<>(Comparator.comparing(User::getFullName));
        treeSet.addAll(allByEnabled);
        return ResponseEntity.ok(treeSet);
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
}
