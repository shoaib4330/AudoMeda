package com.emo.lkplayer.outerlayer.androidservices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.emo.lkplayer.innerlayer.interactors.CurrentSessionInteractor;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.outerlayer.view.NagizarActivity;
import com.h6ah4i.android.media.IBasicMediaPlayer;

import java.io.IOException;
import java.util.List;

import static com.emo.lkplayer.outerlayer.androidservices.MediaControllerService.Constants.ServiceSentActionConstants.ACTION_SEEKBAR_UPDATE;
import static com.emo.lkplayer.outerlayer.androidservices.MediaControllerService.Constants.ServiceSentActionConstants.TAG_INTENT_PROGRESS_INTEGER;



public class MediaControllerService extends Service implements MediaControllerInterface, IBasicMediaPlayer.OnCompletionListener,
        IBasicMediaPlayer.OnPreparedListener, IBasicMediaPlayer.OnErrorListener{

    /* Interface that contains all constants (notification action constants) */
    public interface Constants {
        String MAIN_ACTION = "com.emo.audomeda.action.main";
        String PREV_ACTION = "com.emo.audomeda.action.prev";
        String PLAY_ACTION = "com.emo.audomeda.action.play";
        String PAUSE_ACTION = "com.emo.audomeda.action.pause"; //Notification does not use this one
        String NEXT_ACTION = "com.emo.audomeda.action.next";
        String STOP_ACTION = "com.emo.audomeda.action.stop";

        String TEXT_NOTIFICATION_PLAY= "Play";
        String TEXT_NOTIFICATION_PAUSE= "Pause";

        int NOTIFICATION_ID = 1131;

        /* Interface that contains all constants (that service sends to its clients/receivers) */
        interface ServiceSentActionConstants{
            String ACTION_SERVICE_CHANGES_TRACK = "com.emo.audomeda.action.connectionclient.trackchanged";
            String ACTION_SERVICE_PLAYS_TRACK = "com.emo.audomeda.action.connectionclient.trackplayed";
            String ACTION_SERVICE_STOPS_TRACK = "com.emo.audomeda.action.connectionclient.trackstopped";
            String ACTION_SEEKBAR_UPDATE = "com.emo.audomeda_local_CURRENTSEEKPOSITION_update";

            String INTENT_EXTRA_TRACKINDEX_INT = "com.emo.audomeda.action.connectionclient.trackIndex";
            String TAG_INTENT_PROGRESS_INTEGER = "currentProgress";
        }
    }

    /* Inner class, has to be inside service, when clients connect using service connection,
    * this class provides and interface for interaction*/
    public class MusicBinder extends Binder {
        public MediaControllerInterface getServiceInstance()
        {
            return MediaControllerService.this;
        }
    }

    //formatter:off
        /*-------------- MediaPlayer Callback Interfaces implemented------------------*/
    //formatter:on
    @Override
    public void onCompletion(IBasicMediaPlayer mp)
    {
        this.isMediaPlayerPrepared = false;
        this.isMediaPlayerPlaying = false;
        next();
    }

    @Override
    public boolean onError(IBasicMediaPlayer mp, int what, int extra)
    {
        isMediaPlayerPlaying = false;
        this.isMediaPlayerPrepared = false;
        return true;
    }

    @Override
    public void onPrepared(IBasicMediaPlayer mp)
    {
        mp.start();
        this.isMediaPlayerPrepared = true;
        sendServiceActionBroadCaset(Constants.ServiceSentActionConstants.ACTION_SERVICE_PLAYS_TRACK);
    }

    //formatter:off
        /*-------------- Service Class's fields start here------------------*/
    //formatter:on
    /* -------------------  Constants -------------------*/
    private static final int HANDLER_DELAY_REPEATING_SEEKBAR = 1000;

    /* -------------------  Fields ---------------------*/
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

    /* Interactor --> A use-case object that handles logic for current app's session maintainance */
    private CurrentSessionInteractor currentSessionInteractor;

    /* Data fields */
    private LiveData<List<AudioTrack>> live_serviceTracksList=null;
    private LiveData<Integer> live_currentTrackIndex=null;
    private int currentTrackIndex = -1;

    /* Media player instance used for all playback */
    private IBasicMediaPlayer mediaPlayer;

    /*-- Flags to maintain current state of this class --*/
    private boolean isClientConnected = false;
    private boolean isMediaPlayerPrepared = false;
    private boolean isMediaPlayerPaused   = false;
    private boolean isMediaPlayerPlaying  = false;
    private boolean serviceInstanceStarted = false;

    private IntentFilter audioActionCallsReceiverIntentFilter = new IntentFilter();
    private BroadcastReceiver audioActionCallsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(Constants.PLAY_ACTION))
            {
                if (!isAudioPlaying())
                    play();
            }
            else if (intent.getAction().equals(Constants.PAUSE_ACTION))
            {
                if (isAudioPlaying())
                    pause();
            }
        }
    };


    //formatter:off
        /*---------------------------- Methods start here----------------------------*/
    //formatter:on

    /* -------  Private Methods -------------*/
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

    private void sendServiceActionBroadCaset(String action)
    {
        Intent intent = new Intent(action);
        if (action.equals(Constants.ServiceSentActionConstants.ACTION_SERVICE_CHANGES_TRACK))
        {
            intent.putExtra(Constants.ServiceSentActionConstants.INTENT_EXTRA_TRACKINDEX_INT,currentTrackIndex);
        }
        broadcaster.sendBroadcast(intent);
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
            currTrackText = this.live_serviceTracksList.getValue().get(currentTrackIndex).getTrackTitle();

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

    private void changeCurrentTrackPlayedIndex(int currentTrackIndex, boolean tellModel)
    {
        this.currentTrackIndex = currentTrackIndex;
        sendServiceActionBroadCaset(Constants.ServiceSentActionConstants.ACTION_SERVICE_CHANGES_TRACK);
        if (tellModel)
            this.currentSessionInteractor.updateCurrentProviderQueryPlusIndex(null,null,null,null,-1,currentTrackIndex);
    }

    private void initMediaPlayer(IBasicMediaPlayer mMediaPlayer)
    {
        this.mediaPlayer = mMediaPlayer;
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    /* -------  Service's Framework overridden Methods ------*/
    @Override
    public void onCreate()
    {
        super.onCreate();
        /* Add actions that service will listen to via broadcast-receiver */
        audioActionCallsReceiverIntentFilter.addAction(Constants.PLAY_ACTION);
        audioActionCallsReceiverIntentFilter.addAction(Constants.PAUSE_ACTION);
        /* Register to receive calls via broadcast-receiver */
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(audioActionCallsReceiver,
                audioActionCallsReceiverIntentFilter);
        /* Get the interactor object at the very start, it will be used to interact with the model*/
        currentSessionInteractor = new CurrentSessionInteractor(getApplicationContext());
        /* Setup Media player, receive media player instace that is used by whole application globally*/
        initMediaPlayer(currentSessionInteractor.getMediaPlayer());
        /* Creating a new Handler to schedule runnables on this same service thread*/
        handler = new Handler();
        /* Broadcaster makes certain broadcasts using the handler created above.*/
        broadcaster = LocalBroadcastManager.getInstance(this);
        /* Start broadcasting playback progress*/
        startPlaybackProgressUpdate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (!serviceInstanceStarted)
        {
            serviceInstanceStarted = true;
            this.live_serviceTracksList = this.currentSessionInteractor.getCurrentAudioTracksList();
            this.live_serviceTracksList.observeForever(new Observer<List<AudioTrack>>() {
                @Override
                public void onChanged(@Nullable List<AudioTrack> audioTrackList)
                {
                    /* when list is changed, we call playOnly(int newPos), since its implied
                    that index would have changed...*/
                    playOnly(live_currentTrackIndex.getValue());
                }
            });
            this.live_currentTrackIndex = this.currentSessionInteractor.getCurrentTrackIndex();
            this.currentTrackIndex = live_currentTrackIndex.getValue();
        }

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
                if (!this.isClientConnected)
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


    /*--------- Client interaction plus playback handling interface Methods ----- */
    @Override
    public void play()
    {
        /* This method works if mediaplayer is stopped,paused, it will play the paused or stopped
        track, given the indexOfTrackToBePlayed is already set by play(int pos) method.
         */
        /* Track is already Playing, I will return. If it was paused I will play it */
        if (isAudioPlaying())
            return;
        else if (this.live_serviceTracksList.getValue() == null)
            throw new IllegalStateException("Play called when Tracks List for Service not set");

        /* if a visible client is connected, tell him that track has played, and start service
        as foreground on play only when visible client is connected.
         */
        isMediaPlayerPlaying = true;
        if (isClientConnected)
            startForeground(Constants.NOTIFICATION_ID, buildNotification());

        if (isMediaPlayerPaused)
        {
            isMediaPlayerPaused = false;
            mediaPlayer.start();
            sendServiceActionBroadCaset(Constants.ServiceSentActionConstants.ACTION_SERVICE_PLAYS_TRACK);
            return;
        }

        long trackID = live_serviceTracksList.getValue().get(currentTrackIndex).getTrackID();
        Uri cUri;
        if (this.live_serviceTracksList.getValue().get(currentTrackIndex).getTrackType()==AudioTrack.TRACK_TYPE_AUDIO)
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
        if (this.live_serviceTracksList.getValue()==null || this.live_serviceTracksList.getValue().size()==0)
            throw new IllegalStateException("PlaybackService play(int pos) called while data (trackList) is not set");
        if (currentTrackIndex == pos)
        {
            play();
            return;
        }
        changeCurrentTrackPlayedIndex(pos,true);
        stop();
        play();
    }

    /* Only plays new audio, does not tell model that audio index has changed */
    private void playOnly(int pos)
    {
        if (this.live_serviceTracksList.getValue()==null || this.live_serviceTracksList.getValue().size()==0)
        {
            if (this.live_serviceTracksList.getValue().size()==0)
                return;
            throw new IllegalStateException("PlaybackService play(int pos) called while data (trackList) is not set");
        }
        changeCurrentTrackPlayedIndex(pos,false);
        stop();
        play();
    }

    @Override
    public void next()
    {
        if (currentTrackIndex + 1 < this.live_serviceTracksList.getValue().size())
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
            play(this.live_serviceTracksList.getValue().size() - 1);
        }
    }

    @Override
    public void pause()
    {
        isMediaPlayerPlaying = false;
        isMediaPlayerPaused = true;
        mediaPlayer.pause();
        sendServiceActionBroadCaset(Constants.ServiceSentActionConstants.ACTION_SERVICE_STOPS_TRACK);
        /* if a visible client is connected, tell him that track has paused, and stopForeground
        on pause only when visible client is connected.
         */
        if (isClientConnected)
        {
            stopForeground(true);
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
        sendServiceActionBroadCaset(Constants.ServiceSentActionConstants.ACTION_SERVICE_STOPS_TRACK);
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
        stop();
        play(selectedTrackIndex);
        Log.d("--Service: ","List Set with size= "+trackList.size()+" and index= "+selectedTrackIndex);
    }

    @Override
    public int getCurrentTrackIndex()
    {
        if (this.live_serviceTracksList.getValue()==null)
            throw new IllegalStateException("PlaybackService cannot return current index, Tracks List not set for service");
        else if (this.live_serviceTracksList.getValue().size()==0)
            throw new IllegalStateException("PlaybackService cannot return current index, Tracks List is empty (size is 0)");
        else if (currentTrackIndex==-1)
            throw new IllegalStateException("PlaybackService cannot return current index, currentTrackIndex is -1 (not set yet)");

        return currentTrackIndex;
    }

    @Override
    public void setClientConnected(boolean isConnected)
    {
        this.isClientConnected = isConnected;
    }

    @Override
    public void unregisterMediaUpdateEvents()
    {
    }
}
