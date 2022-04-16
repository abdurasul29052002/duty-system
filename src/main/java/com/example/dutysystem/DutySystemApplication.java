package com.example.dutysystem;

import com.example.dutysystem.component.DateConfiguration;
import com.example.dutysystem.component.TimerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Timer;

@SpringBootApplication
public class DutySystemApplication {

    @Autowired
    TimerConfiguration timerConfiguration;

    private static final long period = 1000 * 60*10;
    private static TimerConfiguration staticTimer;

    public static void main(String[] args) {
        SpringApplication.run(DutySystemApplication.class, args);
        new Timer().scheduleAtFixedRate(staticTimer,1000,period);
    }

    @PostConstruct
    public void  init(){
        staticTimer=timerConfiguration;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
