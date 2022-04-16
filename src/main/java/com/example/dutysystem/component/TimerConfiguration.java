package com.example.dutysystem.component;

import com.example.dutysystem.entity.Complete;
import com.example.dutysystem.entity.Duty;
import com.example.dutysystem.entity.User;
import com.example.dutysystem.repository.CompleteRepository;
import com.example.dutysystem.repository.DutyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.TreeSet;

@Component
public class TimerConfiguration extends TimerTask {

    @Autowired
    DutyRepository dutyRepository;
    @Autowired
    CompleteRepository completeRepository;

    @Override
    public void run() {
        System.out.println("Timer ishladi");
        List<Duty> allDuties = dutyRepository.findAll();
        for (int i=0;i<allDuties.size();i++) {
            Duty duty = allDuties.get(i);
            List<User> users = duty.getUsers();
            int index = 0;
            a:for (User user : users) {
                for (Complete complete : user.getCompleteList()) {
                    if (complete.getDuty().getId()==duty.getId()) {
                        if (!complete.isCompleted()) {
                            duty.setCurrentDuty(user);
                            dutyRepository.save(duty);
                            System.out.println("Navbatchi almashdi "+duty.getName()+" ga "+duty.getCurrentDuty().getFullName());
                            break a;
                        } else {
                            break;
                        }
                    }
                }
                index++;
                if (index == users.size()) {
                    for (User user1 : users) {
                        for (Complete complete : user1.getCompleteList()) {
                            if (complete.getDuty().getId()==duty.getId()){
                                complete.setCompleted(false);
                                completeRepository.save(complete);
                            }
                        }
                    }
                    i--;
                }
            }
        }
    }
}
