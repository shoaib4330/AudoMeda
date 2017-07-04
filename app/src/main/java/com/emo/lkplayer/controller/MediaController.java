package com.emo.lkplayer.controller;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.emo.lkplayer.model.entities.AudioTrack;

import java.io.IOException;
import java.util.List;

/**
 * Created by shoaibanwar on 7/3/17.
 */

public class MediaController extends Service implements AudoMediaController, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    public class MusicBinder extends Binder {
        public AudoMediaController getServiceInstance()
        {
            return MediaController.this;
        }
    }

    public static final String ACTION_SEEKBAR_UPDATE = "com.emo.audomeda_local_CURRENTSEEKPOSITION_update";
    public static final String TAG_INTENT_PROGRESS_INTEGER = "currentProgress";

    private static final int ONGOING_NOTIFICATION_ID = 1131;
    private static final int HANDLER_DELAY_REPEATING_SEEKBAR = 1000;

    private boolean serviceInstanceStarted = false;
    private AudoMediaController.MediaControllerCallbacks serviceCallbackReceiver;

    private LocalBroadcastManager broadcaster;

    private Handler handler;
    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run()
        {
            if (mediaPlayer!=null && mediaPlayer.isPlaying())
            {
                broadcastPlaybackProgress(mediaPlayer.getCurrentPosition());
            }
            handler.postDelayed(handlerRunnable, MediaController.HANDLER_DELAY_REPEATING_SEEKBAR);
        }
    };

    private void startPlaybackProgressUpdate()
    {
        handlerRunnable.run();
    }

    private void stopPlaybackProgressUpdate()
    {
        handler.removeCallbacks(handlerRunnable);
    }

    private void broadcastPlaybackProgress(int currentProgress)
    {
        Intent intent = new Intent(ACTION_SEEKBAR_UPDATE);
        intent.putExtra(TAG_INTENT_PROGRESS_INTEGER, currentProgress);
        broadcaster.sendBroadcast(intent);
    }

    private CurrentDataController dataSource;

    private MediaPlayer mediaPlayer;
    private List<AudioTrack> trackList;
    private int currentTrackIndex = -1;

    private boolean isMediaPlayerPrepared=false;
    private boolean isMediaPlayerPaused = false;

    @Override
    public void onCreate()
    {
        super.onCreate();

        handler = new Handler();
        broadcaster = LocalBroadcastManager.getInstance(this);
        startPlaybackProgressUpdate();
        initMediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        serviceInstanceStarted = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopPlaybackProgressUpdate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return new MusicBinder();
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        return super.onUnbind(intent);
    }


    public void initMediaPlayer()
    {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        this.isMediaPlayerPrepared=false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra)
    {
        this.isMediaPlayerPrepared=false;
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        this.isMediaPlayerPrepared=true;
        mp.start();
    }


    /*-------------- Client interaction interface ------------------ */
    @Override
    public void play()
    {
        if (dataSource.getCurrentListPlayed() == null)
            return;

        if (this.serviceCallbackReceiver!=null)
            this.serviceCallbackReceiver.onTrackPause();

        if (isMediaPlayerPaused){
            mediaPlayer.start();
            return;
        }

        long trackID = trackList.get(currentTrackIndex).getTrackID();
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,trackID);
        mediaPlayer.reset();
        try
        {
            mediaPlayer.setDataSource(getApplicationContext(), trackUri);
            mediaPlayer.prepareAsync();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void play(int pos)
    {
        currentTrackIndex = pos;
        play();
        if (this.serviceCallbackReceiver!=null)
            this.serviceCallbackReceiver.onTrackChanged(false,currentTrackIndex,trackList);
    }

    @Override
    public void next()
    {
        stop();
        if (currentTrackIndex+1<dataSource.getCurrentListPlayed().size()){
            play(currentTrackIndex+1);
        }
        else{
            play(0);
        }
        if (this.serviceCallbackReceiver!=null)
            this.serviceCallbackReceiver.onTrackChanged(false,currentTrackIndex,trackList);
    }

    @Override
    public void previous()
    {
        stop();
        if (currentTrackIndex-1>=0){
            play(currentTrackIndex-1);
        }
        else{
            play(dataSource.getCurrentListPlayed().size()-1);
        }
        if (this.serviceCallbackReceiver!=null)
            this.serviceCallbackReceiver.onTrackChanged(false,currentTrackIndex,trackList);
    }

    @Override
    public void pause()
    {
        isMediaPlayerPaused = true;
        mediaPlayer.pause();
        if (this.serviceCallbackReceiver!=null)
            this.serviceCallbackReceiver.onTrackPause();
    }

    @Override
    public void stop()
    {
        isMediaPlayerPaused=false;
        isMediaPlayerPrepared=false;
        mediaPlayer.stop();
    }

    @Override
    public void fastForward()
    {
        if (mediaPlayer!=null && mediaPlayer.isPlaying()){
            if (mediaPlayer.getDuration()>mediaPlayer.getCurrentPosition()+2000)
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+2000);
        }
    }

    @Override
    public void fastRewind()
    {
        if (mediaPlayer!=null && mediaPlayer.isPlaying()){
            if (mediaPlayer.getCurrentPosition()-2000 > 0)
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-2000);
        }
    }

    @Override
    public void setShuffle(boolean shuffle)
    {

    }

    @Override
    public void seekTo(int posInMillis)
    {
        if (mediaPlayer!=null && isMediaPlayerPrepared)
            mediaPlayer.seekTo(posInMillis);
    }

    @Override
    public boolean isAudioPlaying()
    {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void setDataSource(CurrentDataController currentDataController)
    {
        this.dataSource = currentDataController;
        this.dataSource.isObservant(true);
        this.dataSource.register(new CurrentDataController.DataEventsCallBacks() {
            @Override
            public void onNewTrackSelected(List<AudioTrack> newTracksList, int newAudioIndex)
            {
                boolean isListNew=false;
                if (trackList == null)
                {
                    trackList = newTracksList;
                    currentTrackIndex = newAudioIndex;
                    MediaController.this.stop();
                    MediaController.this.play();
                }
                else if (trackList != null && !trackList.equals(newTracksList))
                {
                    trackList = newTracksList;
                    currentTrackIndex = newAudioIndex;
                    MediaController.this.stop();
                    MediaController.this.play();
                    isListNew=true;
                }
                if (serviceCallbackReceiver!=null)
                    serviceCallbackReceiver.onTrackChanged(isListNew,newAudioIndex,newTracksList);
            }
        });
    }

    @Override
    public void registerForMediaControllerCallbacks(MediaControllerCallbacks mediaUpdateCallbacks)
    {
        this.serviceCallbackReceiver = mediaUpdateCallbacks;
        if (trackList!=null && currentTrackIndex!=-1){
            this.serviceCallbackReceiver.onTrackChanged(false,currentTrackIndex,trackList);
            if (isAudioPlaying())
                this.serviceCallbackReceiver.onTrackPlay();
            else
                this.serviceCallbackReceiver.onTrackPause();
        }
    }

    @Override
    public void unregisterMediaUpdateEvents()
    {
        this.serviceCallbackReceiver = null;
    }
}
