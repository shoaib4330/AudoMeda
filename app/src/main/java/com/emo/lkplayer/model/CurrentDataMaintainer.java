package com.emo.lkplayer.model;

import com.emo.lkplayer.model.entities.AudioTrack;

import java.util.List;
import java.util.Observable;

/**
 * Created by shoaibanwar on 7/3/17.
 */

public class CurrentDataMaintainer extends Observable {
    private static final CurrentDataMaintainer ourInstance = new CurrentDataMaintainer();

    private List<AudioTrack> audioTrackList;
    private int currentTrackPosition = 0;

    public void setAudioTrackList(List<AudioTrack> audioTrackList)
    {
        this.audioTrackList = audioTrackList;
        notifyObservers();
    }

    public void setCurrentTrackIndex(int index){
        if (index<0){
            throw new NumberFormatException("Track Index cannot be negative/less than 0");
        }
        this.currentTrackPosition = index;
        notifyObservers();
    }

    public List<AudioTrack> getAudioTrackList()
    {
        return this.audioTrackList;
    }

    public int getCurrentTrackIndex(){
        return this.currentTrackPosition;
    }


    public static CurrentDataMaintainer getInstance()
    {
        return ourInstance;
    }

    private CurrentDataMaintainer()
    {
    }

}
