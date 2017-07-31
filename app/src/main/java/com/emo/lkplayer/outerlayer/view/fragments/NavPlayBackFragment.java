package com.emo.lkplayer.outerlayer.view.fragments;


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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.emo.lkplayer.R;
import com.emo.lkplayer.outerlayer.androidservices.MediaControllerInterface;
import com.emo.lkplayer.outerlayer.androidservices.MediaControllerService;
import com.emo.lkplayer.outerlayer.customviews.AudoMedaController;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.iPlayable;
import com.emo.lkplayer.outerlayer.view.EqualizerActivity;
import com.emo.lkplayer.outerlayer.view.FragmentInteractionListener;
import com.emo.lkplayer.outerlayer.view.navigation.NavigationManagerContentFlow;
import com.emo.lkplayer.middlelayer.viewmodel.PlayBackViewModel;
import com.emo.lkplayer.outerlayer.androidservices.MediaControllerService.Constants.ServiceSentActionConstants;
import java.util.List;


public class NavPlayBackFragment extends Fragment implements ViewPager.OnPageChangeListener,
        LifecycleRegistryOwner{

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle()
    {
        return registry;
    }

    public interface NavPlayBackFragmentInteractionListener extends FragmentInteractionListener {
        void showToolbar();

        void hideToolbar();

        void showBottomNavBar();

        void hideBottomNavBar();
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

    private NavPlayBackFragmentInteractionListener interactionListener;
    private NavigationManagerContentFlow navigationManager;

    private NavPlayBackFragment.SliderPagerAdapter fragmentStatePagerAdapter;
    private MediaControllerInterface mediaControllerInterface;

    private ServiceConnection serviceConnection;
    private boolean isServiceConnected = false;

    private PlayBackViewModel viewModel;
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
        Live_trackList = viewModel.getTracksList();
        Live_trackList.observe(this, new Observer<List<AudioTrack>>() {
            @Override
            public void onChanged(@Nullable List<AudioTrack> list)
            {
                Log.d("--Observerd List:", "onChanged List");
                if (list==null || list.size()==0)
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
        iPlayable playable = Live_trackList.getValue().get(currentTrackIndex);
        this.playback_control_tool.setPlayable(playable);
        playback_control_tool.setBackImageBlurred(getTrackArtUri(currentTrackIndex));
        albumArtSlider.setCurrentItem(currentTrackIndex);
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
                }
                else
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
        });
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof NavPlayBackFragmentInteractionListener)
        {
            interactionListener = (NavPlayBackFragmentInteractionListener) context;
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

        if (mediaControllerInterface!=null)
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

        if (mediaControllerInterface!=null)
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
            //interactionListener.showToolbar();
            //interactionListener.hideBottomNavBar();
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
            navigationManager.startFolderFragment(true);
        }

        if (item.getItemId() == R.id.menu_playbackFrag_addToPlayList)
        {

        } else if (item.getItemId() == R.id.menu_playbackFrag_lyrics)
        {

        } else if (item.getItemId() == R.id.menu_playbackFrag_delete)
        {
            //new TrackListingViewModel(getContext(),getLoaderManager()).deleteTrack(this.);
        } else if (item.getItemId() == R.id.menu_playbackFrag_preset)
        {

        } else if (item.getItemId() == R.id.menu_playbackFrag_SleepTimer)
        {

        } else if (item.getItemId() == R.id.menu_playbackFrag_search)
        {

        } else if (item.getItemId() == R.id.menu_playbackFrag_ringtone)
        {

        } else if (item.getItemId() == R.id.menu_playbackFrag_info_or_tags)
        {

        } else if (item.getItemId() == R.id.menu_playbackFrag_settings)
        {

        } else if (item.getItemId() == R.id.menu_playbackFrag_help)
        {

        }
        return true;
    }

    /*---------------------- Required Private Methods --------------------------------------------*/

    private String getTrackArtUri(int trackIndex)
    {
        AudioTrack track = viewModel.getTracksList().getValue().get(viewModel.getCurrentTrackIndex().getValue());
        String uri = viewModel.getTrackArtUriByID(track.getContainingAlbumID(), getContext());
        return uri;
    }

    /*---------------------- MediaControllerService Callbacks + Implemented Interfaces ------------------- */
//    @Override
//    public void onRegisterReceiveCurrentPlaybackTrack(int currentTrackIndex, boolean isPlaying)
//    {
//        //--cond to be removed
//        if (currentTrackIndex == -1)
//        {
//            Log.d("tbr-Service Client", "onRegisterInterface currentTrackIndex is -1");
//            return;
//        }
//        changePlayPauseButtonStateForControl(isPlaying);
//        changePlayableForControl(currentTrackIndex);
//    }
//
//    @Override
//    public void onTrackChanged(int newIndex)
//    {
//        changePlayableForControl(newIndex);
//    }
//
//    @Override
//    public void onTrackPlay()
//    {
//        changePlayPauseButtonStateForControl(true);
//    }
//
//    @Override
//    public void onTrackPause()
//    {
//        changePlayPauseButtonStateForControl(false);
//    }

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
            String uriForArtFragment = viewModel.getTrackArtUriByID(viewModel.getTracksList().getValue().get(position).getContainingAlbumID(), getContext());
            //playback_control_tool.setBackImageBlurred(uriForArtFragment);
            return AlbumArtFragment.newInstance(uriForArtFragment);
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