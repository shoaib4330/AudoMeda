package com.emo.emomediaplayerpro.executor;

/**
 * Created by shoaibanwar on 12/2/17.
 */

public final class ExecutorModule {

    private static ThreadExecutor threadExecutor = null;
    private static MainThread mainThread = null;

    public Executor provideExecutor()
    {
        if (threadExecutor==null)
            threadExecutor = new ThreadExecutor();
        return threadExecutor;
    }

    public MainThread provideMainThread()
    {
        if (mainThread==null)
            mainThread = new MainThreadImpl();
        return mainThread;
    }
}
