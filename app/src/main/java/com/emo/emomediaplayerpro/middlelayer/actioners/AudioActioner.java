package com.emo.emomediaplayerpro.middlelayer.actioners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.emo.emomediaplayerpro.model.domain.androidservices.MediaControllerService;

public interface AudioActioner {
    void pauseAudio ();
    void playAudio();
    boolean switchRepeat();
    boolean switchShuffle();

    interface AudioEvents
    {
        void onPlay();
        void onPause();
        void onTrackChange(int trackIndex);
        void onTrackProgressUpdate(int progress);
    }

    class AudioActionerImpl implements AudioActioner
    {
        private Context context;
        private AudioEvents audioEvents;
        private LocalBroadcastManager broadcastManager;
        private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if(audioEvents==null)
                    return;
                if (intent.getAction().equals(MediaControllerService.Constants.ServiceSentActionConstants.ACTION_SERVICE_PLAYS_TRACK))
                {
                    audioEvents.onPlay();
                } else if (intent.getAction().equals(MediaControllerService.Constants.ServiceSentActionConstants.ACTION_SERVICE_STOPS_TRACK))
                {
                    audioEvents.onPause();
                } else if (intent.getAction().equals(MediaControllerService.Constants.ServiceSentActionConstants.ACTION_SERVICE_CHANGES_TRACK))
                {
                    int trackIndex = intent.getIntExtra(MediaControllerService.Constants.ServiceSentActionConstants.INTENT_EXTRA_TRACKINDEX_INT, 0);
                    audioEvents.onTrackChange(trackIndex);
                } else if (intent.getAction().equals(MediaControllerService.Constants.ServiceSentActionConstants.ACTION_SEEKBAR_UPDATE))
                {
                    int currentProgress = intent.getIntExtra(MediaControllerService.Constants.ServiceSentActionConstants.TAG_INTENT_PROGRESS_INTEGER, 0);
                    audioEvents.onTrackProgressUpdate(currentProgress);
                }
            }
        };

        public AudioActionerImpl(Context context,AudioEvents audioEvents)
        {
            this.context = context;
            this.audioEvents = audioEvents;
            this.broadcastManager = LocalBroadcastManager.getInstance(context);
            //this.context.getApplicationContext().registerReceiver(receiver,new IntentFilter())
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

        @Override
        public boolean switchRepeat()
        {
            broadcastManager.sendBroadcast(new Intent(MediaControllerService.Constants.PAUSE_ACTION));
            return false;
        }

        @Override
        public boolean switchShuffle()
        {
            return false;
        }
    }
}
