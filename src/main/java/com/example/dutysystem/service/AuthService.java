package com.example.dutysystem.service;

import com.example.dutysystem.entity.User;
import com.example.dutysystem.payload.ApiResponse;
import com.example.dutysystem.payload.LoginDto;
import com.example.dutysystem.payload.RegisterDto;
import com.example.dutysystem.repository.UserRepository;
import com.example.dutysystem.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse registerUser(RegisterDto registerDto) {
        User user = new User();
        user.setFullName(registerDto.getFullName());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userRepository.save(user);
        return new ApiResponse("User saved",true);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElseThrow(()->new RuntimeException("User not found"));
    }

    public ApiResponse loginUser(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()
            ));
            String token = jwtProvider.generateToken(loginDto.getUsername());
            return new ApiResponse("Login successfully",true,token,userRepository.findByUsername(loginDto.getUsername()).orElse(null).getId());
        }catch (Exception e){
            return new ApiResponse("Username or password error",false);
        }
    }
}