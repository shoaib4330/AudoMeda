package com.emo.lkplayer.innerlayer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.AudioTracksSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.TracksProvider;

import java.util.List;

/**
 * Created by shoaibanwar on 7/24/17.
 */

public class MediaInteractor_Tracks {

    private Context context;
    private TracksProvider tracksProvider;
    private MutableLiveData <List<AudioTrack>> liveAudioTracksList = new MutableLiveData<>();

    public MediaInteractor_Tracks(Context context)
    {
        this.context = context.getApplicationContext();
        tracksProvider = new TracksProvider(context, new TracksProvider.ProviderCallBacks() {
            @Override
            public void onQueryComplete(List<AudioTrack> trackList)
            {
                liveAudioTracksList.setValue(trackList);
            }
        });
    }

    public LiveData<List<AudioTrack>> getAudioTracksAll()
    {
        BaseLoaderSpecification specification = new AudioTracksSpecification();
        tracksProvider.Query(specification);
        return liveAudioTracksList;
    }

    public LiveData<List<AudioTrack>> getAudioTracksRecentlyAdded()
    {
        BaseLoaderSpecification specification = new AudioTracksSpecification.RecentlyAddedAudioTracksSpecification();
        tracksProvider.Query(specification);
        return liveAudioTracksList;
    }

    public LiveData<List<AudioTrack>> getAudioTracksListByAlbum(String albumName)
    {
        if (albumName==null)
            throw new IllegalArgumentException("Album name cannot be null");

        BaseLoaderSpecification specification = new AudioTracksSpecification.AlbumAudioTracksSpecification(albumName);
        tracksProvider.Query(specification);
        return liveAudioTracksList;
    }

    public LiveData<List<AudioTrack>> getAudioTracksListByArtist(String artistName)
    {
        if (artistName==null)
            throw new IllegalArgumentException("Artsit name cannot be null");

        BaseLoaderSpecification specification = new AudioTracksSpecification.AudioTracksByArtistSpecification(artistName);
        tracksProvider.Query(specification);
        return liveAudioTracksList;
    }

    public LiveData<List<AudioTrack>> getAudioTracksListByGenre(long genreID)
    {
        if (genreID<0)
            throw new IllegalArgumentException("Genre id cannot be negative");

        BaseLoaderSpecification specification = new AudioTracksSpecification.AudioTracksByGenreSpecification(genreID);
        tracksProvider.Query(specification);
        return liveAudioTracksList;
    }

    public LiveData<List<AudioTrack>> getAudioTracksListFolder(String folderName)
    {
        if (folderName==null)
            throw new IllegalArgumentException("Folder name cannot be null");

        BaseLoaderSpecification specification = new AudioTracksSpecification.FolderAudioTracksSpecification(folderName);
        tracksProvider.Query(specification);
        return liveAudioTracksList;
    }
}
