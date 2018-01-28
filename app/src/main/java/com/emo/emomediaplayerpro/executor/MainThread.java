package com.emo.emomediaplayerpro.executor;

/**
 * Created by shoaibanwar on 12/2/17.
 */

/**
 * UI thread abstraction created to change the execution context from any thread to the UI thread.
 *
 * @author shoaibanwar
 */
public interface MainThread {
    void post (final Runnable runnable);
}
