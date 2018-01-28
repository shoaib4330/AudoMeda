package com.emo.emomediaplayerpro.model.data_layer.repository;

import com.emo.emomediaplayerpro.AppMediaManager;
import com.h6ah4i.android.media.IBasicMediaPlayer;
import com.h6ah4i.android.media.audiofx.IBassBoost;
import com.h6ah4i.android.media.audiofx.IEqualizer;
import com.h6ah4i.android.media.audiofx.IPreAmp;


public class MediaComponentsRepo {

    public IEqualizer getEqualizer ()
    {
        return AppMediaManager.getInstance().getBasicEqualizer();
    }

    public IBasicMediaPlayer getMediaPlayer()
    {
        return AppMediaManager.getInstance().getMediaBasicPlayer();
    }

    public IPreAmp getPreAmp()
    {
        return AppMediaManager.getInstance().getPreAmp();
    }

    public IBassBoost getBassBoost ()
    {
        return AppMediaManager.getInstance().getiBassBoost();
    }
}
