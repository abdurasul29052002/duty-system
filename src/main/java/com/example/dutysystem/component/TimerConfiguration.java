package com.example.dutysystem.component;

import com.example.dutysystem.entity.Duty;
import com.example.dutysystem.entity.User;
import com.example.dutysystem.repository.DutyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.TimerTask;

@Component
public class TimerConfiguration extends TimerTask {

    @Autowired
    DutyRepository dutyRepository;

    @Override
    public void run() {
        for (Duty duty : dutyRepository.findAll()) {

        }

    }
}
