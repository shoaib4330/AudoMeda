package com.emo.lkplayer.middlelayer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.emo.lkplayer.innerlayer.interactors.CurrentSessionInteractor;
import com.emo.lkplayer.innerlayer.interactors.Interactor_DeleteTrackFromDevice;
import com.emo.lkplayer.innerlayer.interactors.Interactor_ProviderTracks;
import com.emo.lkplayer.innerlayer.interactors.Interactor_ModifyDQ;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;

import java.util.List;

/* Controller to control/provide and return all content and requests regarding audio/video tracks */
public final class TrackListingViewModel extends AndroidViewModel {

    public static final String AllTracksSpecification = "give_all_tracks";
    public static final String RecentTracksSpecification = "give_recent_tracks";

    private LiveData<List<AudioTrack>> Live_TracksList = null;
    private Interactor_ProviderTracks interactor_Provider_tracks;

    boolean isAllTracksRequestMade = false;
    boolean isRecentTracksRequestMade = false;

    boolean isReloadDue = false;
    private Interactor_ModifyDQ interactorModifyDQ;

    public TrackListingViewModel(Application application)
    {
        super(application);
        interactorModifyDQ = new Interactor_ModifyDQ(application.getApplicationContext());
        interactor_Provider_tracks = new Interactor_ProviderTracks(application.getApplicationContext());
    }

    public LiveData<List<AudioTrack>> getAudioTracks(String passSpec)
    {
        if (passSpec.equals(AllTracksSpecification))
        {
            isAllTracksRequestMade = true;
            return interactor_Provider_tracks.getAudioTracksAll();
        } else if (passSpec.equals(RecentTracksSpecification))
        {
            isRecentTracksRequestMade = true;
            return interactor_Provider_tracks.getAudioTracksRecentlyAdded();
        } else
            return null;
    }

    public LiveData<List<AudioTrack>> getAudioTracks(String folderName, String albumName, String artistName, String playlistName, long genreID)
    {
        /* At a trac listTrackFragment can request one type of list (by album, by artist...)
        so if it has requested such list once, no need requesting it from interactor again
        just return the list we already have...
         */
        if (Live_TracksList != null && !isReloadDue)
            return Live_TracksList;

        if (genreID != -1)
        {
            Live_TracksList = interactor_Provider_tracks.getAudioTracksListByGenre(genreID);
        }

        if (folderName != null)
        {
            Live_TracksList = interactor_Provider_tracks.getAudioTracksListFolder(folderName);
        } else if (albumName != null)
        {
            Live_TracksList = interactor_Provider_tracks.getAudioTracksListByAlbum(albumName);
        } else if (artistName != null)
        {
            Live_TracksList = interactor_Provider_tracks.getAudioTracksListByAlbum(albumName);
        } else if (playlistName != null)
        {
            Live_TracksList = interactor_Provider_tracks.getAudioTracks_playlist(playlistName);
        } else
        {
            Live_TracksList = interactor_Provider_tracks.getAudioTracksAll();
        }
        isReloadDue = false;
        return Live_TracksList;
    }

    public void updateCurrentSpecification(String folderName, String albumName, String artistName, String playlistName, long genreID, int currentIndex)
    {
        CurrentSessionInteractor currentSessionInteractor = new CurrentSessionInteractor(this.getApplication().getApplicationContext());
        if (isAllTracksRequestMade)
        {
            currentSessionInteractor.updateCurrentProviderQueryPlusIndexAllTracks(currentIndex);
        } else if (isRecentTracksRequestMade)
        {
            currentSessionInteractor.updateCurrentProviderQueryPlusIndexRecentTracks(currentIndex);
        } else
        {
            currentSessionInteractor.updateCurrentProviderQueryPlusIndex(folderName, albumName, artistName, playlistName, genreID, currentIndex);
        }
    }

    public void deleteTrackFromDevice(AudioTrack audioTrack)
    {
        isReloadDue = true;
        new Interactor_DeleteTrackFromDevice(this.getApplication()).deleteTrack(audioTrack.getTrackID());
    }

    public void enqueueTrack(AudioTrack audioTrack)
    {
        interactorModifyDQ.addTrackToDQ(audioTrack);
    }

    public void dequeueTrack(AudioTrack audioTrack)
    {
        interactorModifyDQ.deleteTrackFromDQ(audioTrack);
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();
        this.interactor_Provider_tracks = null;
        this.Live_TracksList = null;
        this.isRecentTracksRequestMade = false;
        this.isAllTracksRequestMade = false;
    }
}
