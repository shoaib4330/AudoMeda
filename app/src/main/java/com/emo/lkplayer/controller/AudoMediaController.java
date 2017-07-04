package com.emo.lkplayer.controller;

import android.os.Handler;

import com.emo.lkplayer.model.entities.AudioTrack;

import java.util.List;

/**
 * Created by shoaibanwar on 7/3/17.
 */

public interface AudoMediaController {
    interface MediaControllerCallbacks{
        void onTrackChanged(boolean sourceChanged,int newIndex,List<AudioTrack> newTracksList);
        void onTrackPlay();
        void onTrackPause();
    }
    void play();
    void play(int pos);
    void next();
    void previous();
    void pause();
    void stop();
    void fastForward();
    void fastRewind();
    void setShuffle(boolean shuffle);
    void seekTo(int pos);
    boolean isAudioPlaying();
    void setDataSource(CurrentDataController currentDataController);
    void registerForMediaControllerCallbacks(MediaControllerCallbacks mediaUpdateCallbacks);
    void unregisterMediaUpdateEvents();

}
