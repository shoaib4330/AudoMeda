package com.emo.lkplayer.controller;

import com.emo.lkplayer.model.CurrentDataMaintainer;
import com.emo.lkplayer.model.entities.AudioTrack;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by shoaibanwar on 7/3/17.
 */

public class CurrentDataController implements Observer {

    public interface DataEventsCallBacks {
        void onNewTrackSelected(List<AudioTrack> newTracksList, int newAudioIndex);
    }

    private DataEventsCallBacks dataEventsListener;

    public CurrentDataController()
    {
    }

    public void isObservant(boolean beObservant){
        if (beObservant)
            CurrentDataMaintainer.getInstance().addObserver(this);
        else
            CurrentDataMaintainer.getInstance().deleteObserver(this);
    }

    public void register(DataEventsCallBacks dataEventsListener)
    {
        this.dataEventsListener = dataEventsListener;
        this.dataEventsListener.onNewTrackSelected(CurrentDataMaintainer.getInstance().getAudioTrackList(), CurrentDataMaintainer.getInstance().getCurrentTrackIndex());
    }

    public void unregister()
    {
        this.dataEventsListener = null;
    }

    public void setNewTrackListPlusIndex(List<AudioTrack> audioTracks, int index)
    {
        CurrentDataMaintainer.getInstance().setAudioTrackList(audioTracks);
        CurrentDataMaintainer.getInstance().setCurrentTrackIndex(index);
        if (dataEventsListener != null)
            dataEventsListener.onNewTrackSelected(audioTracks, index);
    }

    public List<AudioTrack> getCurrentListPlayed()
    {
        return CurrentDataMaintainer.getInstance().getAudioTrackList();
    }

    public int getCurrentPlayedTrackIndex()
    {
        return CurrentDataMaintainer.getInstance().getCurrentTrackIndex();
    }

    public AudioTrack getCurrentPlayedTrack(){
        return CurrentDataMaintainer.getInstance().getAudioTrackList().get(this.getCurrentPlayedTrackIndex());
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (this.dataEventsListener != null)
        {
            this.dataEventsListener.onNewTrackSelected(CurrentDataMaintainer.getInstance().getAudioTrackList(), CurrentDataMaintainer.getInstance().getCurrentTrackIndex());
        }
    }
}
