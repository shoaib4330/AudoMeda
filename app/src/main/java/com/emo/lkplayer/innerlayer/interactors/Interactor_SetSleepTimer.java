package com.emo.lkplayer.innerlayer.interactors;

import com.emo.lkplayer.outerlayer.Application;

/**
 * Created by shoaibanwar on 8/5/17.
 */

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
