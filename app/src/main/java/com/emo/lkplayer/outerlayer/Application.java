package com.emo.lkplayer.outerlayer;

import android.content.Intent;

import com.emo.lkplayer.Waiter;
import com.emo.lkplayer.outerlayer.androidservices.MediaControllerService;
import com.emo.lkplayer.outerlayer.androidservices.MediaControllerService.Constants.ServiceClientIntentExtras;
/**
 * Created by shoaibanwar on 7/23/17.
 */

public class Application extends android.app.Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        /* Init all the neccessary components for the app via using following class*/
        AppMediaManager.init(getApplicationContext());
    }

    private static Waiter sleepTimer = null;

    public static void setSleepTimer(int minutes)
    {
        if(sleepTimer==null)
            sleepTimer = new Waiter(minutes);
        /* Stop if already running */
        if (sleepTimer.isRunning())
        {
            sleepTimer.setPeriod(minutes);
        }
        else
        {
            sleepTimer.start();
        }

    }

    public static void setSleepTimerOff()
    {
        if (sleepTimer!=null)
            sleepTimer.stopWaiter();
    }

}
