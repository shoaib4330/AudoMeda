package com.emo.emomediaplayerpro.middlelayer.actioners;

import com.h6ah4i.android.media.audiofx.IBassBoost;

public interface ToneVolStereoActions {
    void setLeftRightBalance(float left,float right);
    void setVolumeOverAll(float volume);
    void switchMonoStereo(boolean ifStereo);
    void setStereoX(float value);
    boolean getStereoState();
    float getVolume();
    IBassBoost getBassBoost();
}
