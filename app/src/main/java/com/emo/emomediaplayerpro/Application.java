package com.emo.emomediaplayerpro;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.emo.emomediaplayerpro.utilities.Utility;


/* Application class, It starts before anything else in the application
use this class to make all necessary calls and do work required at very
 start of the app
 */
public class Application extends android.app.Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        /* Init all the necessary components (MediaPlayback) for the app via using following class*/
        AppMediaManager.init(getApplicationContext());
        /* Theme storage and reading should be done via a Repo class ideally
        * and there should be a seperate ThemeUtility class to handle theme change events
        * and tasks*/
        Utility.readThemeHistory(getApplicationContext());
    }

    @Override
    public void onTerminate()
    {
        AppMediaManager.getInstance().End();
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /* A Thread that is used to implement Sleep Timer in this application */
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
