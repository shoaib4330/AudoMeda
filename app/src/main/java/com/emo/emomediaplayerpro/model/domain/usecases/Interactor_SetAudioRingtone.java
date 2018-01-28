package com.emo.emomediaplayerpro.model.domain.usecases;

import android.content.ContentUris;
import android.content.Context;
import android.media.RingtoneManager;

import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.data_layer.content_providers.Specification.AudioTracksSpecification;

public class Interactor_SetAudioRingtone {

    private Context context;

    public Interactor_SetAudioRingtone (Context context)
    {
        this.context = context.getApplicationContext();
    }

    public void setAsRingTone (AudioTrack audioTrack)
    {
        RingtoneManager.setActualDefaultRingtoneUri(context,RingtoneManager.TYPE_RINGTONE, ContentUris.withAppendedId(AudioTracksSpecification.AUDIO_CONTENT_URI,audioTrack.getTrackID()));
    }
}
