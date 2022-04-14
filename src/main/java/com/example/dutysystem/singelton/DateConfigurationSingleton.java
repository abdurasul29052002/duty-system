package com.example.dutysystem.singelton;

import com.example.dutysystem.component.DateConfiguration;

public class DateConfigurationSingleton {
    private static DateConfiguration dateConfiguration;
    private static final DateConfigurationSingleton dateConfigurationSingleton = new DateConfigurationSingleton();

    private DateConfigurationSingleton() {
    }

    public static DateConfigurationSingleton getInstance(int year, int month, int date) {
        if (dateConfiguration == null) {
            dateConfiguration = DateConfiguration.get(year, month, date);
        }
        return dateConfigurationSingleton;
    }

    public DateConfiguration getDateConfiguration() {
        return dateConfiguration;
    }

}
