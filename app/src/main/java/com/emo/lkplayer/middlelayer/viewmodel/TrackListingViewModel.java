package com.emo.lkplayer.middlelayer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.emo.lkplayer.innerlayer.CurrentSessionInteractor;
import com.emo.lkplayer.innerlayer.MediaInteractor_Tracks;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;

import java.util.List;

/**
 * Created by shoaibanwar on 7/12/17.
 */
/* Controller to control/provide and return all content and requests regarding audio/video tracks */
public final class TrackListingViewModel extends AndroidViewModel {

    public  static final String AllTracksSpecification      = "give_all_tracks";
    public  static final String RecentTracksSpecification   = "give_recent_tracks";

    private LiveData<List<AudioTrack>> Live_TracksList=null;
    private MediaInteractor_Tracks mediaInteractor_tracks;

    boolean isAllTracksRequestMade = false;
    boolean isRecentTracksRequestMade = false;

    public TrackListingViewModel(Application application)
    {
        super(application);
        mediaInteractor_tracks = new MediaInteractor_Tracks(application.getApplicationContext());
    }

    public LiveData<List<AudioTrack>> getAudioTracks(String passSpec)
    {
        if (passSpec.equals(AllTracksSpecification))
        {
            isAllTracksRequestMade = true;
            return mediaInteractor_tracks.getAudioTracksAll();
        }
        else if (passSpec.equals(RecentTracksSpecification))
        {
            isRecentTracksRequestMade = true;
            return mediaInteractor_tracks.getAudioTracksRecentlyAdded();
        }
        else
            return null;
    }

    public LiveData<List<AudioTrack>> getAudioTracks(String folderName, String albumName, String artistName, long genreID)
    {
        /* At a trac listTrackFragment can request one type of list (by album, by artist...)
        so if it has requested such list once, no need requesting it from interactor again
        just return the list we already have...
         */
        if (Live_TracksList!=null)
            return Live_TracksList;

        if (genreID != -1)
        {
            Live_TracksList = mediaInteractor_tracks.getAudioTracksListByGenre(genreID);
        }

        if (folderName != null)
        {
            Live_TracksList = mediaInteractor_tracks.getAudioTracksListFolder(folderName);
        }
        else if (albumName != null)
        {
            Live_TracksList= mediaInteractor_tracks.getAudioTracksListByAlbum(albumName);
        }
        else if (artistName != null)
        {
            Live_TracksList = mediaInteractor_tracks.getAudioTracksListByAlbum(albumName);
        }
        else {
            Live_TracksList = mediaInteractor_tracks.getAudioTracksAll();
        }

        return Live_TracksList;
    }

    public LiveData<List<AudioTrack>> retrieveDynamicQueueTracks()
    {
        return null;
    }

    public void updateCurrentSpecification(String folderName, String albumName, String artistName, long genreID,int currentIndex)
    {
        CurrentSessionInteractor currentSessionInteractor = new CurrentSessionInteractor(this.getApplication().getApplicationContext());
        if (isAllTracksRequestMade)
        {
            currentSessionInteractor.updateCurrentProviderQueryPlusIndexAllTracks(currentIndex);
        }
        else if (isRecentTracksRequestMade)
        {
            currentSessionInteractor.updateCurrentProviderQueryPlusIndexRecentTracks(currentIndex);
        }
        else{
            currentSessionInteractor.updateCurrentProviderQueryPlusIndex(folderName,albumName,artistName,genreID,currentIndex);
        }
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();
        this.mediaInteractor_tracks=null;
        this.Live_TracksList=null;
        this.isRecentTracksRequestMade = false;
        this.isAllTracksRequestMade = false;
    }
}
