package com.emo.lkplayer.innerlayer.interactors;

import android.content.Context;
import android.content.pm.LauncherApps;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.repository.TrackRepository;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.AudioTracksSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;

public class Interactor_DeleteTrackFromDevice {

    private Context context;
    private TrackRepository trackRepository;

    public Interactor_DeleteTrackFromDevice(Context context)
    {
        this.context = context.getApplicationContext();
        this.trackRepository = new TrackRepository(context);
    }
    /* method: Delete a track from device */
    public void deleteTrack(AudioTrack audioTrack)
    {
        BaseLoaderSpecification specification = new AudioTracksSpecification.AudioTrackDeletionSpecification(audioTrack.getTrackID());
        this.trackRepository.deleteTrack(specification);
    }
    /* Overloaded method: Delete a track from device */
    public void deleteTrack(long trackID)
    {
        BaseLoaderSpecification specification = new AudioTracksSpecification.AudioTrackDeletionSpecification(trackID);
        this.trackRepository.deleteTrack(specification);
    }
}
