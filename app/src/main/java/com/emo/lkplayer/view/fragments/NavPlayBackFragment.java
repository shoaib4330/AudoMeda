package com.emo.lkplayer.view.fragments;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
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
import com.emo.lkplayer.controller.MediaControllerInterface;
import com.emo.lkplayer.controller.CurrentDataController;
import com.emo.lkplayer.controller.MediaControllerService;
import com.emo.lkplayer.controller.TrackController;
import com.emo.lkplayer.customviews.AudoMedaController;
import com.emo.lkplayer.model.content_providers.TracksProvider;
import com.emo.lkplayer.model.entities.AudioTrack;
import com.emo.lkplayer.view.EqualizerActivity;
import com.emo.lkplayer.view.FragmentInteractionListener;
import com.emo.lkplayer.view.navigation.NavigationManagerContentFlow;

import java.util.List;


public class NavPlayBackFragment extends Fragment implements MediaControllerInterface.MediaControllerCallbacks, ViewPager.OnPageChangeListener {

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
            //Toast.makeText(NavPlayBackFragment.this.getContext(), "Update broadcast received", Toast.LENGTH_SHORT).show();
            int currentProgress = intent.getIntExtra(MediaControllerService.TAG_INTENT_PROGRESS_INTEGER, 0);
            if (playback_control_tool != null)
            {
                playback_control_tool.setPlaybackProgress(currentProgress);
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
        Log.d("--NavPlayback Frag:","Oncreate called");
        if (getArguments() != null)
        {
        }
        Intent intent = new Intent(getActivity(), MediaControllerService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                Log.d("--Service Connection:","Service Connected");
                mediaControllerInterface = ((MediaControllerService.MusicBinder) service).getServiceInstance();
                //mediaControllerInterface.registerForMediaControllerCallbacks(NavPlayBackFragment.this);
                List<AudioTrack> list = new CurrentDataController().getCurrentListPlayed(getContext(),getLoaderManager());
                int listindex = new CurrentDataController().getCurrentPlayedTrackIndex();
                mediaControllerInterface.setDataSource(list,listindex);
                Log.d("--Service Connection:","List Sent with size= "+list.size()+" and index= "+listindex);
                mediaControllerInterface.registerForMediaControllerCallbacks(NavPlayBackFragment.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {

            }
        };

        getActivity().startService(intent);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d("--NavPlayback Frag:","OncreateView called");
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
                    playback_control_tool.setPlayPauseButtonState(false);
                } else
                {
                    mediaControllerInterface.play(new CurrentDataController().getCurrentPlayedTrackIndex());
                    playback_control_tool.setPlayPauseButtonState(true);
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
        Log.d("--NavPlayback Frag:","OnStart called");
        super.onStart();
        albumArtSlider.addOnPageChangeListener(this);
        if (mediaControllerInterface != null)
            mediaControllerInterface.registerForMediaControllerCallbacks(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter(MediaControllerService.ACTION_SEEKBAR_UPDATE));

    }

    @Override
    public void onResume()
    {
        Log.d("--NavPlayback Frag:","OnResume called");
        super.onResume();

    }

    @Override
    public void onPause()
    {
        Log.d("--NavPlayback Frag:","OnPause called");
        super.onPause();
    }

    @Override
    public void onStop()
    {
        Log.d("--NavPlayback Frag:","OnStop called");
        super.onStop();
        albumArtSlider.removeOnPageChangeListener(this);
        if (mediaControllerInterface != null)
            mediaControllerInterface.unregisterMediaUpdateEvents();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onDestroyView()
    {
        Log.d("--NavPlayback Frag:","OnDestroyView called");
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
        Log.d("--NavPlayback Frag:","onDetach called");
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
        Log.d("--NavPlayback Frag:","OnDestroy called");
        if (mediaControllerInterface != null)
        {
            mediaControllerInterface.unregisterMediaUpdateEvents();
        }
        getActivity().unbindService(serviceConnection);
        super.onDestroy();
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

        if (item.getItemId()==R.id.menu_playbackFrag_addToPlayList)
        {

        }
        else if (item.getItemId()==R.id.menu_playbackFrag_lyrics)
        {

        }
        else if (item.getItemId()==R.id.menu_playbackFrag_delete)
        {
            //new TrackController(getContext(),getLoaderManager()).deleteTrack(this.);
        }
        else if (item.getItemId()==R.id.menu_playbackFrag_preset)
        {

        }
        else if (item.getItemId()==R.id.menu_playbackFrag_SleepTimer)
        {

        }
        else if (item.getItemId()==R.id.menu_playbackFrag_search)
        {

        }
        else if (item.getItemId()==R.id.menu_playbackFrag_ringtone)
        {

        }
        else if (item.getItemId()==R.id.menu_playbackFrag_info_or_tags)
        {

        }
        else if (item.getItemId()==R.id.menu_playbackFrag_settings)
        {

        }
        else if (item.getItemId()==R.id.menu_playbackFrag_help)
        {

        }
        return true;
    }

    /*---------------------- Required Private Methods --------------------------------------------*/

    private String getTrackArtUri(int trackIndex)
    {
        AudioTrack track = new CurrentDataController().getCurrentListPlayed().get(trackIndex);
        String uri = new TracksProvider(getContext(), getLoaderManager(),null).getTrackArtUriByID(track.getContainingAlbumID());
        return uri;
    }

    /*---------------------- MediaControllerService Callbacks + Implemented Interfaces ------------------- */
    @Override
    public void onRegisterReceiveCurrentPlaybackTrack(int currentTrackIndex,boolean isPlaying)
    {
        //--cond to be removed -- start
        if (currentTrackIndex==-1)
            return;
        //--cond to be removed -- end

        if (this.playback_control_tool != null)
            this.playback_control_tool.setPlayPauseButtonState(isPlaying);

        if (this.playback_control_tool != null)
        {
            List<AudioTrack> list = new CurrentDataController().getCurrentListPlayed();
            if (list == null || list.size()==0)
                return;
            this.playback_control_tool.setPlayable(list.get(currentTrackIndex));
            playback_control_tool.setBackImageBlurred(getTrackArtUri(currentTrackIndex));
        }
        albumArtSlider.setCurrentItem(currentTrackIndex);
    }

    @Override
    public void onTrackChanged(int newIndex)
    {
        //android.os.Debug.waitForDebugger();

    }

    @Override
    public void onTrackPlay(int newIndex, boolean isNew)
    {
        if (this.playback_control_tool != null)
            this.playback_control_tool.setPlayPauseButtonState(true);
        if (isNew)
        {
            if (this.playback_control_tool != null)
            {
                List<AudioTrack> list = new CurrentDataController().getCurrentListPlayed();
                if (list == null)
                    return;
                this.playback_control_tool.setPlayable(list.get(newIndex));
                playback_control_tool.setBackImageBlurred(getTrackArtUri(newIndex));
            }
            albumArtSlider.setCurrentItem(newIndex);
        }
    }

    @Override
    public void onTrackPause()
    {
        if (this.playback_control_tool != null)
            this.playback_control_tool.setPlayPauseButtonState(false);
    }

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
            TracksProvider tracksProvider = new TracksProvider(NavPlayBackFragment.this.getContext(), NavPlayBackFragment.this.getLoaderManager(),null);
            String uriForArtFragment = tracksProvider.getTrackArtUriByID(new CurrentDataController().getCurrentListPlayed().get(position).getContainingAlbumID());
            //playback_control_tool.setBackImageBlurred(uriForArtFragment);
            return AlbumArtFragment.newInstance(uriForArtFragment);
        }

        @Override
        public int getCount()
        {
            if (new CurrentDataController().getCurrentListPlayed(getContext(),getLoaderManager()) == null)
            {
                return 0;
            }
            return new CurrentDataController().getCurrentListPlayed().size();
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader)
        {
            //super.restoreState(state, loader);
        }
    }

}
