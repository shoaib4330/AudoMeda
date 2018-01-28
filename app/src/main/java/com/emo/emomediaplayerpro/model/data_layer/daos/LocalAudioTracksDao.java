package com.emo.emomediaplayerpro.model.data_layer.daos;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.BaseLoaderSpecification;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Provider_Tracks;

import java.util.ArrayList;
import java.util.List;

public class LocalAudioTracksDao {
    /*ToDo: This is a db based datastore, it must implement an interface (ideally, logically) that
    is consistent for different kinds of datastores. Since currently we have one datastore,
    so we skip this interface at the moment
      */

    private Provider_Tracks providerTracks;
    private MutableLiveData <List<AudioTrack>> liveAudioTracksList = new MutableLiveData<>();

    public LocalAudioTracksDao(Context context)
    {
        if (context==null)
            throw new IllegalArgumentException("context parameter cannot be null");
        this.providerTracks = new Provider_Tracks(context, new Provider_Tracks.ProviderCallBacks() {
            @Override
            public void onQueryComplete(List<AudioTrack> trackList)
            {
                if (trackList==null)
                    trackList = new ArrayList<>();
                liveAudioTracksList.setValue(trackList);
            }
        });
    }

    public LiveData<List<AudioTrack>> QueryTracks(BaseLoaderSpecification specification)
    {
        providerTracks.Query(specification);
        return liveAudioTracksList;
    }

    public void deleteTrack (BaseLoaderSpecification specification)
    {
        providerTracks.remove(specification);
    }
}
