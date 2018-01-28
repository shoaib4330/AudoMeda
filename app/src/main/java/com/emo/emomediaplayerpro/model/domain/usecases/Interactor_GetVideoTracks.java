package com.emo.emomediaplayerpro.model.domain.usecases;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.data_layer.daos.LocalAudioTracksDao;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.BaseLoaderSpecification;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.VideoTracksSpecification;

import java.util.List;

public class Interactor_GetVideoTracks {

    private Context context;
    private LocalAudioTracksDao localAudioDao;

    public Interactor_GetVideoTracks(Context context)
    {
        this.context = context;
        this.localAudioDao = new LocalAudioTracksDao(this.context);
    }

    public LiveData<List<AudioTrack>> getVideoTracksList()
    {
        BaseLoaderSpecification specification = new VideoTracksSpecification();
        return localAudioDao.QueryTracks(specification);
    }
}
