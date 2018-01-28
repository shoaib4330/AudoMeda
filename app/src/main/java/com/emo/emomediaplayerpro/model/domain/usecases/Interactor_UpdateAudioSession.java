package com.emo.emomediaplayerpro.model.domain.usecases;

import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;

import java.util.List;


public class Interactor_UpdateAudioSession {

    private Context context;

    public Interactor_UpdateAudioSession(Context context)
    {
        this.context = context.getApplicationContext();
    }

    public void updateAudioSession (List<AudioTrack> selectedList, AudioTrack selectedTrack)
    {
        //new CurrentSessionRepo(context).updateCurrentProviderQueryPlusIndex();
    }
}
