package com.emo.lkplayer.innerlayer.interactors;

import android.arch.lifecycle.LiveData;
import android.content.ContentUris;
import android.content.Context;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.repository.CurrentSessionRepo;

import java.util.List;

/**
 * Created by shoaibanwar on 8/4/17.
 */

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
