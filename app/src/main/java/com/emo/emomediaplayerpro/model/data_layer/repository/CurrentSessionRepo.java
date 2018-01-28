package com.emo.emomediaplayerpro.model.data_layer.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.usecases.Interactor_GetPlaylistDetail;
import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.domain.entities.Playlist;
import com.emo.emomediaplayerpro.model.data_layer.SessionStorage;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.AudioTracksSpecification;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.BaseLoaderSpecification;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Provider_Tracks;

import java.util.List;


public class CurrentSessionRepo {

    private static MutableLiveData<List<AudioTrack>> currentLiveListAudioTracks=null;
    private static MutableLiveData<Integer> currentLiveTrackIndex=null;

    private static SessionStorage sessionStorage;

    private Provider_Tracks providerTracks;
    private Context mContext;

    private boolean toSetIndex = false;
    private int indexToUpdate = -1;

    public CurrentSessionRepo(Context context)
    {
        this.mContext = context.getApplicationContext();
        sessionStorage = new SessionStorage(mContext);
        providerTracks = new Provider_Tracks(mContext, new Provider_Tracks.ProviderCallBacks() {
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
            providerTracks.Query(specification);
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

    public void updateCurrentProviderQueryPlusIndex(String folderName, String albumName, String artistName,String playlistName ,long genreID, int newIndex)
    {

        BaseLoaderSpecification specification = null;
        if (folderName != null)
            specification = new AudioTracksSpecification.FolderAudioTracksSpecification(folderName);
        else if (albumName != null)
            specification = new AudioTracksSpecification.AlbumAudioTracksSpecification(albumName);
        else if (artistName != null)
            specification = new AudioTracksSpecification.AudioTracksByArtistSpecification(artistName);
        else if (playlistName !=null)
        {
            Playlist.UserDefinedPlaylist playlist = new Interactor_GetPlaylistDetail(mContext).detailsForPlaylist(playlistName);
            specification = new AudioTracksSpecification.AudioTracksByPlaylistSpecification(playlist);
        }
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
        providerTracks.Query(specification);
        sessionStorage.writeSpecification(specification);

    }

    public void updateCurrentProviderQueryPlusIndexAllTracks(int newIndex)
    {

        BaseLoaderSpecification specification = new AudioTracksSpecification() ;
        toSetIndex = true;
        indexToUpdate = newIndex;
        /* when following query completes, trackIndex is updated aswell */
        providerTracks.Query(specification);
        sessionStorage.writeSpecification(specification);
    }

    public void updateCurrentProviderQueryPlusIndexRecentTracks(int newIndex)
    {

        BaseLoaderSpecification specification = new AudioTracksSpecification.RecentlyAddedAudioTracksSpecification() ;
        toSetIndex = true;
        indexToUpdate = newIndex;
        /* when following query completes, trackIndex is updated aswell */
        providerTracks.Query(specification);
        sessionStorage.writeSpecification(specification);
    }

    public void removeTrackFromList(AudioTrack audioTrack)
    {
        if (currentLiveListAudioTracks!=null)
        {
            if (currentLiveListAudioTracks.getValue()!=null)
            {
                if (currentLiveListAudioTracks.getValue().contains(audioTrack))
                {
                    List<AudioTrack> list = currentLiveListAudioTracks.getValue();
                    list.remove(audioTrack);
                    currentLiveTrackIndex.setValue(list.size()-1);
                    currentLiveListAudioTracks.setValue(list);
                }
            }
        }
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

//    public float getVolumeOverAll()
//    {
//        return sessionStorage.getVolumeOverAll();
//    }

//    public void saveVolumeOverAll(float volume)
//    {
//        sessionStorage.saveVolumeOverAll(volume);
//    }

    public void setStereo(boolean stereo)
    {
        sessionStorage.saveStereoUseState(stereo);
    }

    public boolean getStereo()
    {
        return sessionStorage.getSetereoUseState();
    }

}
