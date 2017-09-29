package com.emo.lkplayer.middlelayer.actioners;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.emo.lkplayer.outerlayer.androidservices.MediaControllerService;

public interface AudioActioner {
    void pauseAudio ();
    void playAudio();

    class AudioActionerImpl implements AudioActioner
    {
        private Context context;
        private LocalBroadcastManager broadcastManager;

        public AudioActionerImpl(Context context)
        {
            this.context = context;
            this.broadcastManager = LocalBroadcastManager.getInstance(context);
        }

        @Override
        public void pauseAudio()
        {
            broadcastManager.sendBroadcast(new Intent(MediaControllerService.Constants.PAUSE_ACTION));
        }

        @Override
        public void playAudio()
        {
            broadcastManager.sendBroadcast(new Intent(MediaControllerService.Constants.PLAY_ACTION));
        }
    }
}
