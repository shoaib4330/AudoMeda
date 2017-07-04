package com.emo.lkplayer.view.fragments;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emo.lkplayer.R;
import com.emo.lkplayer.controller.AudoMediaController;
import com.emo.lkplayer.controller.CurrentDataController;
import com.emo.lkplayer.controller.MediaController;
import com.emo.lkplayer.customviews.AudoMedaController;
import com.emo.lkplayer.model.entities.AudioTrack;
import com.emo.lkplayer.view.EqualizerActivity;
import com.emo.lkplayer.view.FragmentInteractionListener;
import com.emo.lkplayer.view.navigation.NavigationManagerContentFlow;

import java.util.List;


public class NavPlayBackFragment extends Fragment implements AudoMediaController.MediaControllerCallbacks {

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
            int currentProgress = intent.getIntExtra(MediaController.TAG_INTENT_PROGRESS_INTEGER, 0);
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
    private AudoMediaController audoMediaController;

    private CurrentDataController dataController;

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
        if (getArguments() != null)
        {
        }
        Intent intent = new Intent(getActivity(), MediaController.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                audoMediaController = ((MediaController.MusicBinder) service).getServiceInstance();
                audoMediaController.setDataSource(new CurrentDataController());
                audoMediaController.registerForMediaControllerCallbacks(NavPlayBackFragment.this);
                //initPlaybackControlTool();
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {

            }
        }, Context.BIND_AUTO_CREATE);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
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
        albumArtSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        return rootView;
    }

    private void initPlaybackControlTool()
    {
        if (audoMediaController != null)
        {
            //playback_control_tool.setPlayPauseButtonState(audoMediaController.isAudioPlaying());
        }
        playback_control_tool.setTouchEventsListener(new AudoMedaController.AudoMedaControllerTouchEventsListener() {
            @Override
            public void onPlayButton_Click()
            {
                if (audoMediaController.isAudioPlaying())
                {
                    audoMediaController.pause();
                    playback_control_tool.setPlayPauseButtonState(false);
                } else
                {
                    audoMediaController.play();
                    playback_control_tool.setPlayPauseButtonState(true);
                }

            }

            @Override
            public void onFastForward_Click()
            {
                audoMediaController.fastForward();
            }

            @Override
            public void onFastRewind_Click()
            {
                audoMediaController.fastRewind();
            }

            @Override
            public void onSkipNext_Click()
            {
                audoMediaController.next();
            }

            @Override
            public void onSkipPrev_Click()
            {
                audoMediaController.previous();
            }

            @Override
            public void onSeekBarPositionChange(int newPositionInMillis)
            {
                audoMediaController.seekTo(newPositionInMillis);
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
        super.onStart();
        if (audoMediaController != null)
            audoMediaController.registerForMediaControllerCallbacks(this);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, new IntentFilter(MediaController.ACTION_SEEKBAR_UPDATE));

    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (audoMediaController != null)
            audoMediaController.unregisterMediaUpdateEvents();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onDestroyView()
    {
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
//            if (!navigationManager.bringExistingFragment(NavFolderFragment.class.toString(), false)) {
//                navigationManager.open(NavFolderFragment.newInstance(null, null), true, true);
//            }
            navigationManager.startFolderFragment(true);
        }
        return true;
    }


    /*---------------------- MediaController Callbacks ------------------- */

    @Override
    public void onTrackChanged(boolean sourceChanged, int newIndex, List<AudioTrack> newTracksList)
    {
        if (this.playback_control_tool != null)
            this.playback_control_tool.setPlayable(newTracksList.get(newIndex));
    }

    @Override
    public void onTrackPlay()
    {
        if (this.playback_control_tool!=null)
            this.playback_control_tool.setPlayPauseButtonState(true);
    }

    @Override
    public void onTrackPause()
    {
        if (this.playback_control_tool!=null)
            this.playback_control_tool.setPlayPauseButtonState(false);
    }


    private static class SliderPagerAdapter extends FragmentStatePagerAdapter {


        public SliderPagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);

        }

        @Override
        public Fragment getItem(int position)
        {
            String uriForArtFragment = new CurrentDataController().getCurrentListPlayed().get(position).getAssociatedArtPath();
            return AlbumArtFragment.newInstance(null);
        }

        @Override
        public int getCount()
        {
            if (new CurrentDataController().getCurrentListPlayed() == null)
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
