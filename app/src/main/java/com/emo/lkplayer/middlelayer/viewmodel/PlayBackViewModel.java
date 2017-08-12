package com.emo.lkplayer.middlelayer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.emo.lkplayer.innerlayer.interactors.CurrentSessionInteractor;
import com.emo.lkplayer.innerlayer.interactors.Interactor_GetAlbumDetails;
import com.emo.lkplayer.innerlayer.interactors.Interactor_GetLyrics;
import com.emo.lkplayer.innerlayer.interactors.Interactor_SetAudioRingtone;
import com.emo.lkplayer.innerlayer.interactors.Interactor_SetSleepTimer;
import com.emo.lkplayer.innerlayer.model.entities.Album;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.Lyrics;

import java.util.List;


public final class PlayBackViewModel extends AndroidViewModel {

    private CurrentSessionInteractor currentSessionInteractor;
    private Interactor_GetAlbumDetails interactor_getAlbumDetails;
    private Interactor_GetLyrics interactor_getLyrics;
    private LiveData<List<AudioTrack>> livedata_trackList;
    private LiveData<Integer> livedata_trackIndex;

    public PlayBackViewModel(Application application)
    {
        super(application);
        currentSessionInteractor = new CurrentSessionInteractor(application.getApplicationContext());
        interactor_getAlbumDetails = new Interactor_GetAlbumDetails(application);
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

    public LiveData<List<Album>> getTrackAlbum(long albumID)
    {
        return interactor_getAlbumDetails.albumByID(albumID);
    }

    public void setRingtone(AudioTrack audioTrack)
    {
        new Interactor_SetAudioRingtone(this.getApplication()).setAsRingTone(audioTrack);
    }

    public void setSleepTimer (int minutes)
    {
        new Interactor_SetSleepTimer().setSleepTimer(minutes);
    }

    public void setSleepTimeOff ()
    {
        new Interactor_SetSleepTimer().setSleepTimerOff();
    }

    public LiveData<String> getLyrics(AudioTrack track)
    {
        interactor_getLyrics = new Interactor_GetLyrics();
        return interactor_getLyrics.getLyrics(track);
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
