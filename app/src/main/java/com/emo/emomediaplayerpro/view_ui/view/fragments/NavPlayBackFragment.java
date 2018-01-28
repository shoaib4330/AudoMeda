package com.emo.emomediaplayerpro.view_ui.view.fragments;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.emo.emomediaplayerpro.R;
import com.emo.emomediaplayerpro.model.domain.entities.EQPreset;
import com.emo.emomediaplayerpro.model.domain.entities.Playlist;
import com.emo.emomediaplayerpro.view_ui.viewmodel.EqualizerViewModel;
import com.emo.emomediaplayerpro.view_ui.viewmodel.PlaylistViewModel;
import com.emo.emomediaplayerpro.model.domain.androidservices.MediaControllerInterface;
import com.emo.emomediaplayerpro.model.domain.androidservices.MediaControllerService;
import com.emo.emomediaplayerpro.view_ui.customviews.AudoMedaController;
import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.model.domain.entities.iPlayable;
import com.emo.emomediaplayerpro.view_ui.customviews.LyricsDialog;
import com.emo.emomediaplayerpro.view_ui.customviews.PlayListDialog;
import com.emo.emomediaplayerpro.view_ui.customviews.SleepTimerDialog;
import com.emo.emomediaplayerpro.view_ui.customviews.TrackInfoDialog;
import com.emo.emomediaplayerpro.view_ui.view.EqualizerActivity;
import com.emo.emomediaplayerpro.view_ui.view.FragmentInteractionListener;
import com.emo.emomediaplayerpro.view_ui.view.navigation.NavigationManagerContentFlow;
import com.emo.emomediaplayerpro.view_ui.viewmodel.PlayBackViewModel;
import com.emo.emomediaplayerpro.model.domain.androidservices.MediaControllerService.Constants.ServiceSentActionConstants;
import com.emo.emomediaplayerpro.utilities.Utility;

import java.util.List;


public class NavPlayBackFragment extends Fragment implements ViewPager.OnPageChangeListener,
        LifecycleRegistryOwner {

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle()
    {
        return registry;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(ServiceSentActionConstants.ACTION_SERVICE_PLAYS_TRACK))
            {
                changePlayPauseButtonStateForControl(true);
            } else if (intent.getAction().equals(ServiceSentActionConstants.ACTION_SERVICE_STOPS_TRACK))
            {
                changePlayPauseButtonStateForControl(false);
            } else if (intent.getAction().equals(ServiceSentActionConstants.ACTION_SERVICE_CHANGES_TRACK))
            {
                int trackIndex = intent.getIntExtra(ServiceSentActionConstants.INTENT_EXTRA_TRACKINDEX_INT, 0);
                changePlayableForControl(trackIndex);
            } else if (intent.getAction().equals(ServiceSentActionConstants.ACTION_SEEKBAR_UPDATE))
            {
                int currentProgress = intent.getIntExtra(ServiceSentActionConstants.TAG_INTENT_PROGRESS_INTEGER, 0);
                updateSeekbarProgressForControl(currentProgress);
            }
        }
    };


    private AudoMedaController playback_control_tool;
    private ViewPager albumArtSlider;

    private FragmentInteractionListener.FragmentAndToolbarInteractionListener interactionListener;
    private NavigationManagerContentFlow navigationManager;

    private NavPlayBackFragment.SliderPagerAdapter fragmentStatePagerAdapter;
    private MediaControllerInterface mediaControllerInterface;

    private ServiceConnection serviceConnection;
    private boolean isServiceConnected = false;

    private PlayBackViewModel viewModel;
    private PlaylistViewModel playlistViewModel;
    private EqualizerViewModel equalizerViewModel;
    private LiveData<List<AudioTrack>> Live_trackList = null;

    public NavPlayBackFragment()
    {
        // Required empty public constructor
    }

    public static NavPlayBackFragment newInstance()
    {
        NavPlayBackFragment fragment = new NavPlayBackFragment();
        fragment.setArguments(null);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("--NavPlayback Frag:", "Oncreate called");

        viewModel = ViewModelProviders.of(this).get(PlayBackViewModel.class);
        equalizerViewModel = ViewModelProviders.of(this).get(EqualizerViewModel.class);
        playlistViewModel = ViewModelProviders.of(this).get(PlaylistViewModel.class);
        /* this loads all the playlists already, so that when dialog is open, dialog is not empty*/
        playlistViewModel.getUserDefinedPlaylists().observe(this, new Observer<List<Playlist.UserDefinedPlaylist>>() {
            @Override
            public void onChanged(@Nullable List<Playlist.UserDefinedPlaylist> userDefinedPlaylists)
            {

            }
        });
        Live_trackList = viewModel.getTracksList();
        Live_trackList.observe(this, new Observer<List<AudioTrack>>() {
            @Override
            public void onChanged(@Nullable List<AudioTrack> list)
            {
                Log.d("--Observerd List:", "onChanged List");
                if (list == null || list.size() == 0)
                    return;
                albumArtSlider.getAdapter().notifyDataSetChanged();
                changePlayableForControl(viewModel.getCurrentTrackIndex().getValue());
            }
        });

        /* Bind to the Music Playback Service */
        buildServiceConnection();
    }

    private void buildServiceConnection()
    {
        Intent intent = new Intent(getActivity(), MediaControllerService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                Log.d("--Service Connection:", "Service Connected");
                isServiceConnected = true;
                mediaControllerInterface = ((MediaControllerService.MusicBinder) service).getServiceInstance();
                mediaControllerInterface.setClientConnected(true);
                changePlayPauseButtonStateForControl(mediaControllerInterface.isAudioPlaying());
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                Log.d("--Service Connection:", "Service Disconnected");
                isServiceConnected = false;
            }
        };

        if (!isServiceConnected)
        {
            //getActivity().startService(intent);
            getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void changePlayPauseButtonStateForControl(boolean setAsPlaying)
    {
        if (this.playback_control_tool != null)
            this.playback_control_tool.setPlayPauseButtonState(setAsPlaying);
    }

    private void changePlayableForControl(int currentTrackIndex)
    {
        albumArtSlider.setCurrentItem(currentTrackIndex);
        iPlayable playable = Live_trackList.getValue().get(currentTrackIndex);
        this.playback_control_tool.setPlayable(playable);
        //playback_control_tool.setBackImageBlurred(getTrackArtUri(tempIndex));

    }

    private void updateSeekbarProgressForControl(int progress)
    {
        if (playback_control_tool != null)
        {
            playback_control_tool.setPlaybackProgress(progress);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d("--NavPlayback Frag:", "OncreateView called");
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        if (interactionListener != null)
        {
            interactionListener.showToolbar();
            interactionListener.hideBottomNavBar();
            navigationManager = (NavigationManagerContentFlow) interactionListener.getNavigationManager();
        }

        View rootView = inflater.inflate(R.layout.fragment_nav_playback, container, false);
        playback_control_tool = (AudoMedaController) rootView.findViewById(R.id.view_mediaConroller);

        initPlaybackControlTool();

        fragmentStatePagerAdapter = new NavPlayBackFragment.SliderPagerAdapter(getChildFragmentManager());
        albumArtSlider = (ViewPager) rootView.findViewById(R.id.vp_AlbumArtSlider);
        albumArtSlider.setAdapter(fragmentStatePagerAdapter);
        return rootView;
    }

    private void initPlaybackControlTool()
    {
        playback_control_tool.setTouchEventsListener(new AudoMedaController.AudoMedaControllerTouchEventsListener() {
            @Override
            public void onPlayButton_Click()
            {
                if (mediaControllerInterface.isAudioPlaying())
                {
                    mediaControllerInterface.pause();
                } else
                {
                    mediaControllerInterface.play();
                }
            }

            @Override
            public void onFastForward_Click()
            {
                mediaControllerInterface.fastForward();
            }

            @Override
            public void onFastRewind_Click()
            {
                mediaControllerInterface.fastRewind();
            }

            @Override
            public void onSkipNext_Click()
            {
                mediaControllerInterface.next();
            }

            @Override
            public void onSkipPrev_Click()
            {
                mediaControllerInterface.previous();
            }

            @Override
            public void onSeekBarPositionChange(int newPositionInMillis)
            {
                mediaControllerInterface.seekTo(newPositionInMillis);
            }

            @Override
            public void onRepeatSelected(View button)
            {
                //if not repeating
                //set to repeating
                button.setActivated(true);
                //if repeating
                //set to not repeating
                button.setActivated(false);
            }

            @Override
            public void onShuffleSelected(View button)
            {
                //if not shuffling
                //set to shuffling
                button.setActivated(true);
                //if shuffling
                //set to not shuffling
                button.setActivated(false);
            }
        });
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener.FragmentAndToolbarInteractionListener)
        {
            interactionListener = (FragmentInteractionListener.FragmentAndToolbarInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart()
    {
        Log.d("--NavPlayback Frag:", "OnStart called");
        super.onStart();
        albumArtSlider.addOnPageChangeListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServiceSentActionConstants.ACTION_SEEKBAR_UPDATE);
        intentFilter.addAction(ServiceSentActionConstants.ACTION_SERVICE_CHANGES_TRACK);
        intentFilter.addAction(ServiceSentActionConstants.ACTION_SERVICE_PLAYS_TRACK);
        intentFilter.addAction(ServiceSentActionConstants.ACTION_SERVICE_STOPS_TRACK);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);

        if (mediaControllerInterface != null)
        {
            mediaControllerInterface.setClientConnected(true);
            changePlayPauseButtonStateForControl(mediaControllerInterface.isAudioPlaying());
        }

    }

    @Override
    public void onResume()
    {
        Log.d("--NavPlayback Frag:", "OnResume called");
        super.onResume();
    }

    @Override
    public void onPause()
    {
        Log.d("--NavPlayback Frag:", "OnPause called");
        super.onPause();
    }

    @Override
    public void onStop()
    {
        Log.d("--NavPlayback Frag:", "OnStop called");
        super.onStop();
        albumArtSlider.removeOnPageChangeListener(this);
        if (mediaControllerInterface != null)
            mediaControllerInterface.unregisterMediaUpdateEvents();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);

        if (mediaControllerInterface != null)
            mediaControllerInterface.setClientConnected(false);
    }

    @Override
    public void onDestroyView()
    {
        Log.d("--NavPlayback Frag:", "OnDestroyView called");
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
        Log.d("--NavPlayback Frag:", "onDetach called");
        super.onDetach();
        if (interactionListener != null)
        {
            interactionListener.hideToolbar();
            interactionListener.showBottomNavBar();
        }
        interactionListener = null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.d("--NavPlayback Frag:", "OnDestroy called");
        if (mediaControllerInterface != null)
            mediaControllerInterface.unregisterMediaUpdateEvents();

        if (isServiceConnected)
        {
            Log.d("--NavPlayback Frag:", "Service unbind called");
            getActivity().unbindService(serviceConnection);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        if (interactionListener != null)
        {
            inflater.inflate(R.menu.menu_activity_playback, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menuBtn_ToneVol)
        {
            Intent intent = new Intent(getActivity(), EqualizerActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuBtn_Equalizer)
        {
            Intent intent = new Intent(getActivity(), EqualizerActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuBtn_Library)
        {
            navigationManager.startFolderFragment();
        }

        if (item.getItemId() == R.id.menu_playbackFrag_addToPlayList)
        {
            final List<Playlist.UserDefinedPlaylist> plist = playlistViewModel.getUserDefinedPlaylists().getValue();
            PlayListDialog dialog = new PlayListDialog(getContext(), playlistViewModel.getUserDefinedPlaylists().getValue());
            dialog.initPlusBuildDialog(new PlayListDialog.DialogInteractionEventsListener() {
                @Override
                public void onItemClickListener(AdapterView<?> parent, View view, int position, long id, AlertDialog playListDialog)
                {
                    if (plist == null)
                    {
                        Toast.makeText(getContext(), "NULL", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    playListDialog.dismiss();
                    String playlistName = plist.get(position).getPlaylistName();
                    AudioTrack audioTrack = viewModel.getTracksList().getValue().get(viewModel.getCurrentTrackIndex().getValue());
                    playlistViewModel.addAudioTrackToPlaylist(audioTrack, playlistName);
                }

                @Override
                public void onNewPlayListCreated(String playListName)
                {
                    if (playListName == null || playListName.isEmpty())
                        return;
                    playlistViewModel.addNewPlayList(playListName);
                }
            });
        }
        else if (item.getItemId() == R.id.menu_playbackFrag_lyrics)
        {
            final LyricsDialog dialog = new LyricsDialog(getContext());
            dialog.show();
            viewModel.getLyrics(viewModel.getTracksList().getValue().get(viewModel.getCurrentTrackIndex().getValue()))
                    .observe(this, new Observer<String>() {
                        @Override
                        public void onChanged(@Nullable String lyrics)
                        {
                            dialog.updateLyrics(lyrics);
                        }
                    });
        }
        else if (item.getItemId() == R.id.menu_playbackFrag_preset)
        {
            final List<EQPreset> presetArr = equalizerViewModel.getAllEqPresets();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.itemview_icon_text, R.id.tv_itemView_icon_text);
            arrayAdapter.addAll(Utility.EQListToStringArray(presetArr));

            ListView listView = new ListView(getContext());
            listView.setAdapter(arrayAdapter);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(listView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getWindow().setLayout(800, 1000);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    try
                    {
                        presetArr.get(position).applyToEQ(equalizerViewModel.getEqualizerInstance());
                        equalizerViewModel.setEqPreset(presetArr.get(position));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    alertDialog.dismiss();
                }
            });
            return true;
        }
        else if (item.getItemId() == R.id.menu_playbackFrag_SleepTimer)
        {
            final SleepTimerDialog dialog = new SleepTimerDialog(getContext(), new SleepTimerDialog.DialogCallbacks() {
                @Override
                public void onTimerSet(int minutes, AlertDialog malertDialog)
                {
                    viewModel.setSleepTimer(minutes);
                    malertDialog.dismiss();
                }

                @Override
                public void onTimerdisable(AlertDialog malertDialog)
                {
                    viewModel.setSleepTimeOff();
                    malertDialog.dismiss();
                }
            });
            dialog.show();
        }
        else if (item.getItemId() == R.id.menu_playbackFrag_ringtone)
        {
            viewModel.setRingtone(viewModel.getTracksList().getValue().get(viewModel.getCurrentTrackIndex().getValue()));
        }
        else if (item.getItemId() == R.id.menu_playbackFrag_info_or_tags)
        {
            AudioTrack audioTrack = viewModel.getTracksList().getValue().get(viewModel.getCurrentTrackIndex().getValue());
            TrackInfoDialog trackInfoDialog = new TrackInfoDialog(getContext(),audioTrack);
            trackInfoDialog.show();
        }
        else if (item.getItemId() == R.id.menu_playbackFrag_themes)
        {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, Utility.themeArr);

            ListView listView = new ListView(getContext());
            listView.setAdapter(arrayAdapter);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(listView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Utility.setThemeHistory(getContext(),position);
                    Utility.changeToTheme(getActivity(),position);
                    alertDialog.dismiss();
                }
            });
            return true;
        } else if (item.getItemId() == R.id.menu_playbackFrag_help)
        {

        }
        return true;
    }

    /*---------------------- Required Private Methods --------------------------------------------*/



    /*------------------------------------------*/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int position)
    {
        /* This will only make a call when this client is bound/connected to service */
        if (mediaControllerInterface != null)
        {
            mediaControllerInterface.play(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }

    private class SliderPagerAdapter extends FragmentStatePagerAdapter {

        public SliderPagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);

        }

        @Override
        public Fragment getItem(int position)
        {
            return AlbumArtFragment.newInstance(viewModel.getTracksList().getValue().get(position).getContainingAlbumID());
        }

        @Override
        public int getCount()
        {
            if (Live_trackList == null)
            {
                return 0;
            }
            if (Live_trackList.getValue() == null || Live_trackList.getValue().size() == 0)
            {
                return 0;
            }
            return Live_trackList.getValue().size();
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader)
        {
            //super.restoreState(state, loader);
        }
    }
}
