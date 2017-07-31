package com.emo.lkplayer.middlelayer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.emo.lkplayer.innerlayer.CurrentSessionInteractor;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;

import java.util.List;

/**
 * Created by shoaibanwar on 7/14/17.
 */

public final class PlayBackViewModel extends AndroidViewModel {

    private CurrentSessionInteractor currentSessionInteractor;
    private LiveData<List<AudioTrack>> livedata_trackList;
    private LiveData<Integer> livedata_trackIndex;

    public PlayBackViewModel(Application application)
    {
        super(application);
        currentSessionInteractor = new CurrentSessionInteractor(application.getApplicationContext());
    }

    public LiveData<List<AudioTrack>> getTracksList()
    {
        if (livedata_trackList==null)
            livedata_trackList = currentSessionInteractor.getCurrentAudioTracksList();
        return livedata_trackList;
    }

    public LiveData<Integer> getCurrentTrackIndex()
    {
        if(livedata_trackIndex==null)
            livedata_trackIndex = currentSessionInteractor.getCurrentTrackIndex();
        return livedata_trackIndex;
    }

    public String getTrackArtUriByID(long albumID,Context context)
    {
        //return this.trackRepository.getTrackArtUriByID(albumID,context);
        return "";
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();
        this.livedata_trackList = null;
        this.livedata_trackIndex = null;
        this.currentSessionInteractor = null;
    }
}
