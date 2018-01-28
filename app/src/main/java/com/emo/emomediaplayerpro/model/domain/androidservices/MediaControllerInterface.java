package com.emo.emomediaplayerpro.model.domain.androidservices;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;

import java.util.List;


public interface MediaControllerInterface {
    interface MediaControllerCallbacks{
        void onRegisterReceiveCurrentPlaybackTrack(int currentTrackIndex,boolean isPlaying);
        void onTrackChanged(int newIndex);
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
    void setDataSource(List<AudioTrack> trackList,int selectedTrackIndex);
    int  getCurrentTrackIndex();
    void setClientConnected(boolean isConnected);
    //void registerForMediaControllerCallbacks(MediaControllerCallbacks mediaUpdateCallbacks);
    void unregisterMediaUpdateEvents();

}
