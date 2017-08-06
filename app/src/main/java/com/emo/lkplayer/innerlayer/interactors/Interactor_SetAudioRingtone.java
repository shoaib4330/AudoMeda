package com.emo.lkplayer.innerlayer.interactors;

import android.content.ContentUris;
import android.content.Context;
import android.media.RingtoneManager;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.AudioTracksSpecification;

/**
 * Created by shoaibanwar on 8/4/17.
 */

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
