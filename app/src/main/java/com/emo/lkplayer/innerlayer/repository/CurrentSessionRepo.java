package com.emo.lkplayer.innerlayer.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.outerlayer.storage.SessionStorage;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.AudioTracksSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.TracksProvider;

import java.util.List;

/**
 * Created by shoaibanwar on 7/24/17.
 */

public class CurrentSessionRepo {

    private static MutableLiveData<List<AudioTrack>> currentLiveListAudioTracks;
    private static MutableLiveData<Integer> currentLiveTrackIndex;

    private static SessionStorage sessionStorage;

    private TracksProvider tracksProvider;
    private Context mContext;

    private boolean toSetIndex = false;
    private int indexToUpdate = -1;

    public CurrentSessionRepo(Context context)
    {
        this.mContext = context.getApplicationContext();
        sessionStorage = new SessionStorage(mContext);
        tracksProvider = new TracksProvider(mContext, new TracksProvider.ProviderCallBacks() {
            @Override
            public void onQueryComplete(List<AudioTrack> trackList)
            {
                if (toSetIndex)
                {
                    updateCurrentIndex(indexToUpdate);
                    toSetIndex = false;
                    indexToUpdate = -1;
                }
                currentLiveListAudioTracks.setValue(trackList);
            }
        });
    }

    public LiveData<List<AudioTrack>> getAudioTracksList()
    {
        if (currentLiveListAudioTracks == null)
        {
            currentLiveListAudioTracks = new MutableLiveData<>();
            BaseLoaderSpecification specification = sessionStorage.getSpecification();
            if (specification == null)
            {
                specification = new AudioTracksSpecification();
                sessionStorage.writeSpecification(specification);
            }
            tracksProvider.Query(specification);
        }
        return currentLiveListAudioTracks;

    }

    public LiveData<Integer> getCurrentTrackIndex()
    {
        if (currentLiveTrackIndex == null)
        {
            currentLiveTrackIndex = new MutableLiveData<>();
            int readIndex = sessionStorage.getCurrentTrackIndex();
            currentLiveTrackIndex.setValue(readIndex);
        }
        return currentLiveTrackIndex;
    }

    public void updateCurrentProviderQueryPlusIndex(String folderName, String albumName, String artistName, long genreID, int newIndex)
    {

        BaseLoaderSpecification specification = null;
        if (folderName != null)
            specification = new AudioTracksSpecification.FolderAudioTracksSpecification(folderName);
        else if (albumName != null)
            specification = new AudioTracksSpecification.AlbumAudioTracksSpecification(albumName);
        else if (artistName != null)
            specification = new AudioTracksSpecification.AudioTracksByArtistSpecification(artistName);
        else if (genreID != -1)
            specification = new AudioTracksSpecification.AudioTracksByGenreSpecification(genreID);
        else
        {
            updateCurrentIndex(newIndex);
            return;
        }
        toSetIndex = true;
        indexToUpdate = newIndex;
        /* when following query completes, trackIndex is updated aswell */
        tracksProvider.Query(specification);
        sessionStorage.writeSpecification(specification);

    }

    public void updateCurrentProviderQueryPlusIndexAllTracks(int newIndex)
    {

        BaseLoaderSpecification specification = new AudioTracksSpecification() ;
        toSetIndex = true;
        indexToUpdate = newIndex;
        /* when following query completes, trackIndex is updated aswell */
        tracksProvider.Query(specification);
        sessionStorage.writeSpecification(specification);
    }

    public void updateCurrentProviderQueryPlusIndexRecentTracks(int newIndex)
    {

        BaseLoaderSpecification specification = new AudioTracksSpecification.RecentlyAddedAudioTracksSpecification() ;
        toSetIndex = true;
        indexToUpdate = newIndex;
        /* when following query completes, trackIndex is updated aswell */
        tracksProvider.Query(specification);
        sessionStorage.writeSpecification(specification);
    }

    private void updateCurrentIndex(int mIndex)
    {
        currentLiveTrackIndex.setValue(mIndex);
        sessionStorage.storeCurrentTrackIndex(mIndex);
    }

    /*----------------- Methods used to handle storage-reading of Equalizer data ---------------- */
    public void saveUsedPreset(String presetName)
    {
        sessionStorage.saveCurrentUsedPreset(presetName);

    }

    public String getSavedUsedPresetName()
    {
        return sessionStorage.getCurrentUsedPresetName();
    }

    public void saveEqualizerUseState(boolean isBeingUsed)
    {
        sessionStorage.saveEqaulizerUseState(isBeingUsed);
    }

    public boolean getEqualizerUseState()
    {
        return sessionStorage.getEqaulizerUseState();
    }

    public void saveToneUseState(boolean isBeingUsed)
    {
        sessionStorage.saveToneUseState(isBeingUsed);
    }

    public boolean getToneUseState()
    {
        return sessionStorage.getToneUseState();
    }

    public void saveLimitUseState(boolean isBeingUsed)
    {
        sessionStorage.saveToneUseState(isBeingUsed);
    }

    public boolean getLimitUseState()
    {
        return sessionStorage.getToneUseState();
    }

}
