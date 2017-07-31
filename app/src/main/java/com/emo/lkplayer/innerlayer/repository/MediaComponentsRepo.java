package com.emo.lkplayer.innerlayer.repository;

import com.emo.lkplayer.outerlayer.AppMediaManager;
import com.h6ah4i.android.media.IBasicMediaPlayer;
import com.h6ah4i.android.media.audiofx.IEqualizer;
import com.h6ah4i.android.media.audiofx.IPreAmp;

/**
 * Created by shoaibanwar on 7/26/17.
 */

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

}
