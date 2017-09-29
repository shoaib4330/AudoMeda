package com.emo.lkplayer.outerlayer;

import android.content.Context;
import android.content.Intent;

import com.emo.lkplayer.innerlayer.interactors.CurrentSessionInteractor;
import com.emo.lkplayer.innerlayer.model.entities.EQPreset;
import com.emo.lkplayer.outerlayer.androidservices.MediaControllerService;
import com.h6ah4i.android.media.IBasicMediaPlayer;
import com.h6ah4i.android.media.IMediaPlayerFactory;
import com.h6ah4i.android.media.audiofx.IBassBoost;
import com.h6ah4i.android.media.audiofx.IEqualizer;
import com.h6ah4i.android.media.audiofx.IPreAmp;
import com.h6ah4i.android.media.opensl.OpenSLMediaPlayerFactory;

/**
 * Created by shoaibanwar on 7/26/17.
 */

/* A Singleton that handles initialization and holds instances of objects required
for Audio/Media playback */
public class AppMediaManager {

    private static AppMediaManager ourInstance = null;
    private static boolean inited = false;

    public static AppMediaManager getInstance()
    {
        if (!inited)
            throw new IllegalStateException("AppMediaManager: Module not initialized yet");
        return ourInstance;
    }

    static synchronized void init(Context context)
    {

        if (ourInstance == null)
        {
            wContext = context.getApplicationContext();
            iMediaPlayerFactory = new OpenSLMediaPlayerFactory(context);
            ourInstance = new AppMediaManager();
        }
        /* Created an instance of interactor object to interact with model */
        CurrentSessionInteractor currentSessionInteractor = new CurrentSessionInteractor(wContext);
        /* Check if last time equalizer was saved as activated or disabled*/
        iEqualizer.setEnabled(currentSessionInteractor.getEqLastUseState());
        /* Enable the PreAmp*/
        iPreAmp.setEnabled(true);
        /* Everything has been initialized*/
        inited = true;
        /* Get from model the last_selected_saved preset for equalizer*/
        EQPreset preset = currentSessionInteractor.getSavedSelectedEqPreset();
        /* apply the preset to equalizer */
        try
        {
            preset.applyToEQ(iEqualizer);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        /* start MusicPlayBack Service */
        Context mContext = context.getApplicationContext();
        Intent intent = new Intent(mContext, MediaControllerService.class);
        mContext.startService(intent);
    }

    /* ------------------------ Constants -------------------------*/


    /* ------------------------ Fields ------------------------- */
    private static Context wContext;
    private static IPreAmp iPreAmp = null;
    private static IEqualizer iEqualizer = null;
    private static IBassBoost iBassBoost = null;
    private static IBasicMediaPlayer basicMediaPlayer = null;
    private static IMediaPlayerFactory iMediaPlayerFactory = null;

    private AppMediaManager()
    {
        /* Created an instance of interactor object to interact with model */
        CurrentSessionInteractor currentSessionInteractor = new CurrentSessionInteractor(wContext);
        /* Create the instance of mediaplayer to be used in the app*/
        basicMediaPlayer = iMediaPlayerFactory.createMediaPlayer();
        /* Create the instance of Equalizer to be used in the app*/
        iEqualizer = iMediaPlayerFactory.createHQEqualizer();
        /* Create the instance of Bass Boost */
        iBassBoost = iMediaPlayerFactory.createBassBoost(basicMediaPlayer);
        /* Set it enabled */
        iBassBoost.setEnabled(true);
        /* Create the instance of PreAmp to be used in the app*/
        iPreAmp = iMediaPlayerFactory.createPreAmp();
    }

    public IBasicMediaPlayer getMediaBasicPlayer()
    {
        if (ourInstance == null || !inited)
            throw new IllegalStateException("AppMediaManager: Module not initialized yet");
        return basicMediaPlayer;
    }

    public IEqualizer getBasicEqualizer()
    {
        if (ourInstance == null || !inited)
            throw new IllegalStateException("AppMediaManager: Module not initialized yet");
        return iEqualizer;
    }

    public IPreAmp getPreAmp()
    {
        if (ourInstance == null || !inited)
            throw new IllegalStateException("AppMediaManager: Module not initialized yet");
        return iPreAmp;
    }

    public IBassBoost getiBassBoost()
    {
        if (ourInstance == null || !inited)
            throw new IllegalStateException("AppMediaManager: Module not initialized yet");
        return iBassBoost;
    }

    public void End()
    {
        if (basicMediaPlayer!=null)
            basicMediaPlayer.release();
        basicMediaPlayer = null;
        if (iEqualizer!=null)
            iEqualizer.release();
        if (iBassBoost!=null)
            iBassBoost.release();
        if (iPreAmp!=null)
            iPreAmp.release();
        iMediaPlayerFactory=null;
        wContext =null;
        iEqualizer=null;
        iBassBoost=null;
        iPreAmp = null;
    }

}
