package com.emo.lkplayer.outerlayer.view.fragments;


import android.arch.lifecycle.Observer;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.emo.lkplayer.R;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.EQPreset;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;
import com.emo.lkplayer.middlelayer.actioners.AudioActioner;
import com.emo.lkplayer.outerlayer.customviews.AudoMedaController;
import com.emo.lkplayer.outerlayer.customviews.LyricsDialog;
import com.emo.lkplayer.outerlayer.customviews.PlayListDialog;
import com.emo.lkplayer.outerlayer.customviews.SleepTimerDialog;
import com.emo.lkplayer.outerlayer.customviews.TrackInfoDialog;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.VideoTracksSpecification;
import com.emo.lkplayer.outerlayer.view.EqualizerActivity;
import com.emo.lkplayer.outerlayer.view.FragmentInteractionListener.FragmentAndToolbarInteractionListener;
import com.emo.lkplayer.outerlayer.view.navigation.NavigationManagerContentFlow;
import com.emo.lkplayer.utilities.Utility;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoPlaybackFragment extends Fragment {

    public VideoPlaybackFragment()
    {
        // Required empty public constructor
        Log.d("vidplayback", "constructor called");
    }

    public static VideoPlaybackFragment newInstance(ArrayList param1, int selectedVideoTrackPos)
    {
        Log.d("vidplayback", "newInstance called");
        VideoPlaybackFragment fragment = new VideoPlaybackFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM2, selectedVideoTrackPos);
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof FragmentAndToolbarInteractionListener)
        {
            interactionListener = (FragmentAndToolbarInteractionListener) context;
            this.navigationManager = (NavigationManagerContentFlow) interactionListener.getNavigationManager();
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void setupMediaPlayer()
    {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                playMediaPlayer();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                nextTrack();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra)
            {
                return true;
            }
        });
    }

    private void setupAudoMedaController()
    {
        audoMedaController = (AudoMedaController) rootView.findViewById(R.id.audomedaController_videoPlaybackFrag);
        audoMedaController.setPlayPauseButtonState(false);
        audoMedaController.setTouchEventsListener(new AudoMedaController.AudoMedaControllerTouchEventsListener() {
            @Override
            public void onPlayButton_Click()
            {
                if (mediaPlayer != null)
                {
                    if (mediaPlayer.isPlaying())
                    {
                        mediaPlayer.pause();
                        audoMedaController.setPlayPauseButtonState(false);
                        return;
                    }
                    mediaPlayer.start();
                    audoMedaController.setPlayPauseButtonState(true);
                }
            }

            @Override
            public void onFastForward_Click()
            {
                if (mediaPlayer != null && mediaPlayer.isPlaying())
                {
                    if (mediaPlayer.getDuration() > mediaPlayer.getCurrentPosition() + 5000)
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
                }
            }

            @Override
            public void onFastRewind_Click()
            {
                if (mediaPlayer != null && mediaPlayer.isPlaying())
                {
                    if (mediaPlayer.getCurrentPosition() - 5000 > 0)
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
                }
            }

            @Override
            public void onSkipNext_Click()
            {
                if (mediaPlayer != null)
                {
                    nextTrack();
                }
            }

            @Override
            public void onSkipPrev_Click()
            {
                if (mediaPlayer != null)
                {
                    prevTrack();
                }
            }

            @Override
            public void onSeekBarPositionChange(int newPosition)
            {
                if (mediaPlayer != null)
                    mediaPlayer.seekTo(newPosition);
            }
        });
    }

    private void nextTrack()
    {
        if (this.mParam_videoTracksList == null || this.mParam_videoTracksList.size() == 0)
            return;
        if (this.mParam_selectedVideoTrack + 1 >= this.mParam_videoTracksList.size())
        {
            trackChanged(0);
            return;
        } else
        {
            trackChanged(this.mParam_selectedVideoTrack + 1);
        }
    }

    private void prevTrack()
    {
        if (this.mParam_videoTracksList == null || this.mParam_videoTracksList.size() == 0)
            return;
        if (this.mParam_selectedVideoTrack - 1 < 0)
        {
            trackChanged(this.mParam_videoTracksList.size() - 1);
            return;
        } else
        {
            trackChanged(this.mParam_selectedVideoTrack - 1);
        }
    }

    private void playMediaPlayer()
    {
        mediaPlayer.start();
        audoMedaController.setPlayPauseButtonState(true);
    }

    private void trackChanged(int position)
    {
        if (mParam_videoTracksList != null && mParam_videoTracksList.size() != 0)
        {
            if (position == -1)
                position = 0;
            this.mParam_selectedVideoTrack = position;
            Uri uri = ContentUris.withAppendedId(AudioTrack.URI_VIDEO_TRACKS, mParam_videoTracksList.get(position).getTrackID());
            mediaPlayer.reset();
            try
            {
                mediaPlayer.setDataSource(getContext(), uri);
                mediaPlayer.prepareAsync();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            audoMedaController.setPlayable(mParam_videoTracksList.get(mParam_selectedVideoTrack));
        }
    }


    /* --------------- Constants --------------------*/
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /* --------------- Communication + Navigation ------*/
    private NavigationManagerContentFlow navigationManager = null;
    private FragmentAndToolbarInteractionListener interactionListener = null;

    /* ---------------- View+Framework fields ----------------*/
    private View rootView;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private AudoMedaController audoMedaController;
    private Handler handler = new Handler();
    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run()
        {
            if (mediaPlayer != null)
            {
                if (audoMedaController != null)
                {
                    audoMedaController.setPlaybackProgress(mediaPlayer.getCurrentPosition());
                }
            }
            handler.postDelayed(handlerRunnable, 1000);
        }
    };

    /* --------------- Data Fields -----------------*/
    private List<AudioTrack> mParam_videoTracksList;
    private int mParam_selectedVideoTrack = -1;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("vidplayback", "onCreate called");
        setHasOptionsMenu(true);
        if (getArguments() != null)
        {
            mParam_videoTracksList = (ArrayList) getArguments().getSerializable(ARG_PARAM1);
            mParam_selectedVideoTrack = getArguments().getInt(ARG_PARAM2);
        }
        /* Setup media player */
        setupMediaPlayer();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d("vidplayback", "onCreateView called");
        if (interactionListener != null)
        {
            interactionListener.showToolbar();
            interactionListener.hideBottomNavBar();
        }

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_video_playback, container, false);
        surfaceView = (SurfaceView) rootView.findViewById(R.id.surfView_ForVideo);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                mediaPlayer.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {

            }
        });
        /* Setup media controller layout */
        setupAudoMedaController();
         /* After all is setup, lets set the selected track*/
        trackChanged(mParam_selectedVideoTrack);
        return rootView;
    }

    @Override
    public void onStart()
    {
        Log.d("vidplayback", "onStart called");
        super.onStart();
        /* if audio tracks are being played, pause them */
        new AudioActioner.AudioActionerImpl(getContext()).pauseAudio();
        /* handler keeps seekbar updating for track */
        handler.postDelayed(handlerRunnable, 1000);
    }

    @Override
    public void onResume()
    {
        Log.d("vidplayback", "onResume called");
        super.onResume();
        if (!mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    @Override
    public void onPause()
    {
        Log.d("vidplayback", "onPause called");
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_videoplayback_frag, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_back)
        {
            navigationManager.PopVideoPlaybackFragment();
        }
        else if (item.getItemId() == R.id.menu_AudPlayback)
        {
            navigationManager.startPlayBackFragment();
        }
        else if (item.getItemId() == R.id.menu_next)
        {
            nextTrack();
        } else if (item.getItemId() == R.id.menu_previous)
        {
            prevTrack();
        } else if (item.getItemId() == R.id.menu_play)
        {
            if (mediaPlayer != null)
            {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    audoMedaController.setPlayPauseButtonState(false);
                    return true;
                }
                mediaPlayer.start();
                audoMedaController.setPlayPauseButtonState(true);
            }
        }
        return true;
    }

    @Override
    public void onStop()
    {
        Log.d("vidplayback", "onStop called");
        super.onStop();
        /* cancel handler runnables that are running */
        handler.removeCallbacks(handlerRunnable);
        /* Stop the media player */
        //this.mediaPlayer.stop();
    }

    @Override
    public void onDestroyView()
    {
        Log.d("vidplayback", "onDestroyView called");
        super.onDestroyView();
        if (interactionListener != null)
        {
            interactionListener.hideToolbar();
            interactionListener.showBottomNavBar();
        }
    }

    @Override
    public void onDetach()
    {
        Log.d("vidplayback", "onDetach called");
        super.onDetach();
    }

    @Override
    public void onDestroy()
    {
        Log.d("vidplayback", "onDestroy called");
        super.onDestroy();
        /* Release the media player */
        this.mediaPlayer.release();
    }
}
