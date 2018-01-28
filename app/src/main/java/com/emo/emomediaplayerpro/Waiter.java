package com.emo.emomediaplayerpro;

import android.util.Log;

public class Waiter extends Thread {

    private static final String TAG = Waiter.class.getName();
    private long waiterInitTime;
    private long periodInMillis;
    private boolean stop;
    private boolean isRunning = false;

    public Waiter(int minutes)
    {
        setPeriod(minutes);
        stop = false;
    }

    @Override
    public synchronized void start()
    {
        super.start();
        this.isRunning = true;
    }

    public void run()
    {
        long idle = 0;
        this.setWaiter();
        do
        {
            idle = System.currentTimeMillis() - waiterInitTime;
            Log.d(TAG, "Application is idle for " + idle + " ms");
            try
            {
                Thread.sleep(5000); //check every 5 seconds
            } catch (InterruptedException e)
            {
                Log.d(TAG, "Waiter interrupted!");
            }
            if (idle > periodInMillis)
            {
                idle = 0;
                /* Sleep timer has completed its time, now kill the process/close the app*/
                this.isRunning = false;
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
        while (!stop);
        this.isRunning = false;
        Log.d(TAG, "Finishing Waiter thread");
    }

    public synchronized void setWaiter()
    {
        waiterInitTime = System.currentTimeMillis();
    }

    public synchronized void forceInterrupt()
    {
        this.interrupt();
    }

    //soft stopping of thread
    public synchronized void stopWaiter()
    {
        stop = true;
    }

    public synchronized boolean isRunning()
    {
        return isRunning;
    }

    public synchronized void setPeriod(int minutes)
    {
        this.periodInMillis = minutes * 60 * 1000;
        setWaiter();
    }


}
