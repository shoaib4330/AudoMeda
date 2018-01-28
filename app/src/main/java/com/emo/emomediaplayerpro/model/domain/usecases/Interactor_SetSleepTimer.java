package com.emo.emomediaplayerpro.model.domain.usecases;

import com.emo.emomediaplayerpro.Application;


public class Interactor_SetSleepTimer {

    public Interactor_SetSleepTimer()
    {

    }

    public void setSleepTimer(int minutes)
    {
        Application.setSleepTimer(minutes);
    }

    public void setSleepTimerOff()
    {
        Application.setSleepTimerOff();
    }
}
