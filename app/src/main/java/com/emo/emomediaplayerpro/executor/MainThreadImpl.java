package com.emo.emomediaplayerpro.executor;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by shoaibanwar on 12/2/17.
 */

public class MainThreadImpl implements MainThread {

    private Handler handler;

    public MainThreadImpl()
    {
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable)
    {
        handler.post(runnable);
    }
}
