package com.emo.lkplayer.innerlayer.interactors;

import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.repository.CurrentSessionRepo;
import com.emo.lkplayer.innerlayer.repository.TrackRepo;

public class Interactor_DeleteTrackFromDevice {

    private Context context;
    private TrackRepo trackRepo;
    private CurrentSessionRepo currentSessionRepo;

    public Interactor_DeleteTrackFromDevice(Context context)
    {
        this.context = context.getApplicationContext();
        this.trackRepo = new TrackRepo(context);
        this.currentSessionRepo = new CurrentSessionRepo(context);
    }
    /* method: Delete a track from device */
    public void deleteTrack(AudioTrack audioTrack)
    {
        //BaseLoaderSpecification specification = new AudioTracksSpecification.AudioTrackDeletionSpecification(audioTrack.getTrackID());
        //currentSessionRepo.removeTrackFromList(audioTrack);
        //this.trackRepo.deleteTrack(specification);
    }
}
