package com.example.dutysystem.component;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class DateConfiguration extends Date {
    private DateConfiguration() {
    }

    public static Date get(int year, int month, int date){
        LocalDate localDate = LocalDate.of(year, month, date);
        LocalDate currentLocalDate = LocalDate.now();
        Period period = Period.between(currentLocalDate, localDate);
        long l = (long) period.getDays() * 24 * 60 * 60 * 1000;
        return new Date(System.currentTimeMillis()+l);
    }
}
