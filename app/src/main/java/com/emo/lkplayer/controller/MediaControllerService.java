package com.emo.lkplayer.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.emo.lkplayer.R;
import com.emo.lkplayer.model.entities.AudioTrack;
import com.emo.lkplayer.view.NagizarActivity;

import java.io.IOException;
import java.util.List;

/**
 * Created by shoaibanwar on 7/3/17.
 */

public class MediaControllerService extends Service implements MediaControllerInterface, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    interface Constants {
        String MAIN_ACTION = "com.emo.audomeda.action.main";
        String PREV_ACTION = "com.emo.audomeda.action.prev";
        String PLAY_ACTION = "com.emo.audomeda.action.play";
        String NEXT_ACTION = "com.emo.audomeda.action.next";
        String STOP_ACTION = "com.emo.audomeda.action.stop";

        String TEXT_NOTIFICATION_PLAY= "Play";
        String TEXT_NOTIFICATION_PAUSE= "Pause";

        int NOTIFICATION_ID = 1131;

    }

    public class MusicBinder extends Binder {
        public MediaControllerInterface getServiceInstance()
        {
            return MediaControllerService.this;
        }
    }

    public static final String ACTION_SEEKBAR_UPDATE = "com.emo.audomeda_local_CURRENTSEEKPOSITION_update";
    public static final String TAG_INTENT_PROGRESS_INTEGER = "currentProgress";

    private static final int HANDLER_DELAY_REPEATING_SEEKBAR = 1000;

    private boolean serviceInstanceStarted = false;
    private MediaControllerInterface.MediaControllerCallbacks serviceCallbackReceiver;

    private LocalBroadcastManager broadcaster;

    private Handler handler;
    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run()
        {
            if (mediaPlayer != null && mediaPlayer.isPlaying())
            {
                broadcastPlaybackProgress(mediaPlayer.getCurrentPosition());
            }
            handler.postDelayed(handlerRunnable, MediaControllerService.HANDLER_DELAY_REPEATING_SEEKBAR);
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

    private MediaPlayer mediaPlayer;
    private List<AudioTrack> serviceTracksList;
    private int currentTrackIndex = -1;

    private boolean isMediaPlayerPrepared = false;
    private boolean isMediaPlayerPaused = false;
    private boolean isMediaPlayerPlaying = false;

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
        if (!serviceInstanceStarted)
            serviceInstanceStarted = true;

        if (intent.getAction() == null)
        {

        }
        else if (intent.getAction().equals(Constants.PREV_ACTION))
        {
            Log.i("Media Service--", "Clicked Previous");
            previous();
            buildAndNotify();
        }
        else if (intent.getAction().equals(Constants.PLAY_ACTION))
        {
            if (isAudioPlaying()){
                pause();
                if (this.serviceCallbackReceiver==null)
                    buildAndNotify();
            }
            else{
                play();
                buildAndNotify();
            }
        }
        else if (intent.getAction().equals(Constants.NEXT_ACTION))
        {
            Log.i("Media Service--", "Clicked Next");
            next();
            buildAndNotify();
        }
        else if (intent.getAction().equals(Constants.STOP_ACTION))
        {
            Log.i("Media Service--", "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        return START_NOT_STICKY;
        //return super.onStartCommand(intent,flags,startId);
    }

    private Notification buildNotification()
    {
        String playText = Constants.TEXT_NOTIFICATION_PLAY;
        int playPauseIconID = android.R.drawable.ic_media_play;
        if (isAudioPlaying())
        {
            playPauseIconID = android.R.drawable.ic_media_pause;
            playText = Constants.TEXT_NOTIFICATION_PAUSE;
        }
        else
        {
            playPauseIconID = android.R.drawable.ic_media_play;
            playText = Constants.TEXT_NOTIFICATION_PLAY;
        }

        Intent notificationIntent = new Intent(this, NagizarActivity.class);
        notificationIntent.setAction(Constants.MAIN_ACTION);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent previousIntent = new Intent(this, MediaControllerService.class);
        previousIntent.setAction(Constants.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent playIntent = new Intent(this, MediaControllerService.class);
        playIntent.setAction(Constants.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent nextIntent = new Intent(this, MediaControllerService.class);
        nextIntent.setAction(Constants.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent stopIntent = new Intent(this, MediaControllerService.class);
        stopIntent.setAction(Constants.STOP_ACTION);
        PendingIntent pStopIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.notlargeicon);

        String currTrackText = "Active";
        if (isAudioPlaying())
            currTrackText = this.serviceTracksList.get(currentTrackIndex).getTrackTitle();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("lk Media Player")
                .setContentText(currTrackText).setSmallIcon(R.drawable.noticonsmall)
                .setLargeIcon(icon)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setWhen(0)
                //.addAction(android.R.drawable.ic_media_previous, "Prev", ppreviousIntent)
                .addAction(playPauseIconID, playText, pplayIntent)
                .addAction(android.R.drawable.ic_media_next, "Next", pnextIntent)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel,"Stop",pStopIntent);

        return notificationBuilder.build();
    }

    private void buildAndNotify()
    {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFICATION_ID, buildNotification());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopPlaybackProgressUpdate();
        this.mediaPlayer.release();
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

        this.unregisterMediaUpdateEvents();
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
        this.isMediaPlayerPrepared = false;
        this.isMediaPlayerPlaying = false;
        next();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra)
    {
        isMediaPlayerPlaying = false;
        this.isMediaPlayerPrepared = false;
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        this.isMediaPlayerPrepared = true;
        mp.start();
    }


    /*-------------- Client interaction interface ------------------ */
    @Override
    public void play()
    {
        /* This method works if mediaplayer is stopped,paused, it will play the paused or stopped
        track, given the indexOfTrackToBePlayed is already set by play(int pos) method.
         */
        /* Track is already Playing, I will return. If it was paused I will play it */
        if (isAudioPlaying())
            return;
        else if (this.serviceTracksList == null)
            return;

        /* if a visible client is connected, tell him that track has played, and start service
        as foreground on play only when visible client is connected.
         */
        isMediaPlayerPlaying = true;

        if (this.serviceCallbackReceiver != null)
        {
            startForeground(Constants.NOTIFICATION_ID, buildNotification());
            this.serviceCallbackReceiver.onTrackPlay(currentTrackIndex,true);
        }

        if (isMediaPlayerPaused)
        {
            isMediaPlayerPaused = false;
            mediaPlayer.start();
            return;
        }

        long trackID = new CurrentDataController().getCurrentListPlayed().get(new CurrentDataController().getCurrentPlayedTrackIndex()).getTrackID();
        Uri cUri;
        if (this.serviceTracksList.get(currentTrackIndex).getTrackType()==AudioTrack.TRACK_TYPE_AUDIO)
            cUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        else
            cUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Uri trackUri = ContentUris.withAppendedId(cUri, trackID);
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
        if (this.serviceTracksList==null || this.serviceTracksList.size()==0)
            return;

        if (currentTrackIndex == pos)
        {
            play();
            return;
        }

        /* Update the model, that current track has now changed, this is mandatory to to here, since
        track can change while no View is connected therefore this service/controller has to update
        the model.
         */
        new CurrentDataController().setNewTrackListPlusIndex(new CurrentDataController().getCurrentListPlayed(),pos);

        if (this.serviceCallbackReceiver != null)
        {
            this.serviceCallbackReceiver.onTrackPlay(pos,true);
            this.serviceCallbackReceiver.onTrackChanged(pos);
        }

        currentTrackIndex = pos;
        stop();
        play();
        if (this.serviceCallbackReceiver != null)
            this.serviceCallbackReceiver.onTrackChanged(currentTrackIndex);
    }

    @Override
    public void next()
    {
        if (currentTrackIndex + 1 < this.serviceTracksList.size())
        {
            play(currentTrackIndex + 1);
        } else
        {
            play(0);
        }
    }

    @Override
    public void previous()
    {
        if (currentTrackIndex - 1 >= 0)
        {
            play(currentTrackIndex - 1);
        } else
        {
            play(this.serviceTracksList.size() - 1);
        }
    }

    @Override
    public void pause()
    {
        isMediaPlayerPlaying = false;
        isMediaPlayerPaused = true;
        mediaPlayer.pause();
        /* if a visible client is connected, tell him that track has paused, and stopForeground
        on pause only when visible client is connected.
         */
        if (this.serviceCallbackReceiver != null)
        {
            stopForeground(true);
            this.serviceCallbackReceiver.onTrackPause();
        }
    }

    @Override
    public void stop()
    {
        isMediaPlayerPlaying = false;
        isMediaPlayerPaused  = false;
        if (isMediaPlayerPrepared)
        {
            mediaPlayer.stop();
        }
        isMediaPlayerPrepared=false;
        if (this.serviceCallbackReceiver != null)
            this.serviceCallbackReceiver.onTrackPause();
    }

    @Override
    public void fastForward()
    {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
        {
            if (mediaPlayer.getDuration() > mediaPlayer.getCurrentPosition() + 2000)
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 2000);
        }
    }

    @Override
    public void fastRewind()
    {
        if (mediaPlayer != null && mediaPlayer.isPlaying())
        {
            if (mediaPlayer.getCurrentPosition() - 2000 > 0)
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 2000);
        }
    }

    @Override
    public void setShuffle(boolean shuffle)
    {

    }

    @Override
    public void seekTo(int posInMillis)
    {
        if (mediaPlayer != null && isMediaPlayerPrepared)
            mediaPlayer.seekTo(posInMillis);
    }

    @Override
    public boolean isAudioPlaying()
    {
        return isMediaPlayerPlaying;
    }

    @Override
    public void setDataSource(List<AudioTrack> trackList, int selectedTrackIndex)
    {
        if ( (trackList == null && this.serviceTracksList == null) || selectedTrackIndex == -1)
        {
            Log.d("ToDo Taks:", "Load something (a track list) from database here");
            return;
        }

        if (this.serviceTracksList == null)
        {
            this.serviceTracksList = trackList;
            //this.currentTrackIndex = selectedTrackIndex;
        } else if (this.serviceTracksList.equals(trackList) && this.currentTrackIndex == selectedTrackIndex)
        {
            //do nothing, let the current track go on in same state...
            return;
        } else if (!this.serviceTracksList.equals(trackList))
        {
            this.serviceTracksList = trackList;
            //this.currentTrackIndex = selectedTrackIndex;
        } else if (this.serviceTracksList.equals(trackList) && this.currentTrackIndex != selectedTrackIndex)
        {
            //this.currentTrackIndex = selectedTrackIndex;
        }
        stop();
        this.currentTrackIndex=selectedTrackIndex;

        Log.d("--Service: ","List Set with size= "+trackList.size()+" and index= "+selectedTrackIndex);

        //play(selectedTrackIndex);
    }

    @Override
    public void registerForMediaControllerCallbacks(MediaControllerCallbacks mediaUpdateCallbacks)
    {
        Log.d("--Service registerCall:","");
        this.serviceCallbackReceiver = mediaUpdateCallbacks;
        this.serviceCallbackReceiver.onRegisterReceiveCurrentPlaybackTrack(currentTrackIndex,isAudioPlaying());
    }

    @Override
    public void unregisterMediaUpdateEvents()
    {
        this.serviceCallbackReceiver = null;
    }
}
