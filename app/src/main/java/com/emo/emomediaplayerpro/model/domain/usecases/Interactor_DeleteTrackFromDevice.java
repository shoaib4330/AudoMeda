package com.emo.emomediaplayerpro.model.domain.usecases;

import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.data_layer.repository.CurrentSessionRepo;
import com.emo.emomediaplayerpro.model.data_layer.daos.LocalAudioTracksDao;

public class Interactor_DeleteTrackFromDevice {

    private Context context;
    private LocalAudioTracksDao localAudioDao;
    private CurrentSessionRepo currentSessionRepo;

    public Interactor_DeleteTrackFromDevice(Context context)
    {
        this.context = context.getApplicationContext();
        this.localAudioDao = new LocalAudioTracksDao(context);
        this.currentSessionRepo = new CurrentSessionRepo(context);
    }
    /* method: Delete a track from device */
    public void deleteTrack(AudioTrack audioTrack)
    {
        //BaseLoaderSpecification specification = new AudioTracksSpecification.AudioTrackDeletionSpecification(audioTrack.getTrackID());
        //currentSessionRepo.removeTrackFromList(audioTrack);
        //this.localAudioDao.deleteTrack(specification);
    }
}
