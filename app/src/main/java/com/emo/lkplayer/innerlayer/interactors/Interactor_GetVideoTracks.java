package com.emo.lkplayer.innerlayer.interactors;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.repository.TrackRepo;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.AudioTracksSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.VideoTracksSpecification;

import java.util.List;

public class Interactor_GetVideoTracks {

    private Context context;
    private TrackRepo trackRepo;

    public Interactor_GetVideoTracks(Context context)
    {
        this.context = context;
        this.trackRepo = new TrackRepo(this.context);
    }

    public LiveData<List<AudioTrack>> getVideoTracksList()
    {
        BaseLoaderSpecification specification = new VideoTracksSpecification();
        return trackRepo.QueryTracks(specification);
    }
}
