package com.example.dutysystem.singelton;

import com.example.dutysystem.component.TimerConfiguration;

public class TimerConfigurationSingleton {
    private static final TimerConfigurationSingleton timerConfigurationSingleton = new TimerConfigurationSingleton();
    private static TimerConfiguration timerConfiguration;

    private TimerConfigurationSingleton(){

    }
    public static TimerConfigurationSingleton getInstance(){
        if (timerConfiguration == null) {
            timerConfiguration = new TimerConfiguration();
        }
        return timerConfigurationSingleton;
    }

    public TimerConfiguration getTimerConfiguration(){
        return timerConfiguration;
    }
}
