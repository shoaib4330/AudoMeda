package com.emo.lkplayer.innerlayer.interactors;

import android.content.Context;
import android.media.AudioManager;

import com.emo.lkplayer.innerlayer.repository.CurrentSessionRepo;
import com.emo.lkplayer.innerlayer.repository.MediaComponentsRepo;

public class Interactor_VolumeAndStereos {

    private Context context;
    private MediaComponentsRepo mediaComponentsRepo;
    private CurrentSessionRepo currentSessionRepo;

    public Interactor_VolumeAndStereos(Context context)
    {
        this.context = context;
        mediaComponentsRepo = new MediaComponentsRepo();
        currentSessionRepo = new CurrentSessionRepo(context);
    }

    public void setVolume(float volume)
    {
        mediaComponentsRepo.getMediaPlayer().setVolume(volume/100,volume/100);
        //currentSessionRepo.saveVolumeOverAll(volume);
    }

    public void setLeftRightBalance(float left,float right)
    {
        mediaComponentsRepo.getMediaPlayer().setVolume((100-left)/100,right/100);
    }

    public void setStereoX(float value)
    {
        mediaComponentsRepo.getMediaPlayer().setVolume((100-value)/100,value/100);
    }

    public float getVolume()
    {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return 100*audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //return currentSessionRepo.getVolumeOverAll();
    }

    public void setStereoUseState(boolean set)
    {
        currentSessionRepo.setStereo(set);
    }

    public boolean getStereoUseState ()
    {
        return currentSessionRepo.getStereo();
    }

}
