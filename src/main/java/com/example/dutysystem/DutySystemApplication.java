package com.example.dutysystem;

import com.example.dutysystem.component.DateConfiguration;
import com.example.dutysystem.component.TimerConfiguration;
import com.example.dutysystem.singelton.DateConfigurationSingleton;
import com.example.dutysystem.singelton.TimerConfigurationSingleton;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Timer;

@SpringBootApplication
public class DutySystemApplication {
    private static final long period = 1000 * 60 * 60 * 24;

    public static void main(String[] args) {
        SpringApplication.run(DutySystemApplication.class, args);
        new Timer().scheduleAtFixedRate(TimerConfigurationSingleton.getInstance().getTimerConfiguration(), DateConfigurationSingleton.getInstance(2022,4,15).getDateConfiguration(), period);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
