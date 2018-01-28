package com.emo.emomediaplayerpro.view_ui.view.fragments;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emo.emomediaplayerpro.R;
import com.emo.emomediaplayerpro.model.domain.entities.AudioTrack;
import com.emo.emomediaplayerpro.view_ui.viewmodel.VideoTrackViewModel;
import com.emo.emomediaplayerpro.view_ui.view.FragmentInteractionListener;
import com.emo.emomediaplayerpro.view_ui.view.FragmentInteractionListener.FragmentAndToolbarInteractionListener;
import com.emo.emomediaplayerpro.view_ui.view.navigation.NavigationManagerContentFlow;
import com.emo.emomediaplayerpro.view_ui.view.reusables.TrackRecyclerAdapter;

import java.util.List;

public class VideoTracksListFragment extends Fragment implements LifecycleRegistryOwner {

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle()
    {
        return registry;
    }

    public VideoTracksListFragment()
    {
        // Required empty public constructor
    }

    public static VideoTracksListFragment newInstance()
    {
        VideoTracksListFragment fragment = new VideoTracksListFragment();;
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

    /*----------------- Communication fields ---------------------*/
    private FragmentInteractionListener interactionListener;

    /*----------------- Navigation fields ---------------------*/
    private NavigationManagerContentFlow navigationManager;

    /*----------------- View fields ---------------------*/
    private View rootView;
    private RecyclerView recyclerView;
    private TrackRecyclerAdapter trackRecylerAdapter;

    /*----------------- Data fields ---------------------*/
    private List<AudioTrack> trackList;
    private VideoTrackViewModel videoTrackViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.videoTrackViewModel = ViewModelProviders.of(this).get(VideoTrackViewModel.class);
        videoTrackViewModel.getVideoTracksList().observe(this, new Observer<List<AudioTrack>>() {
            @Override
            public void onChanged(@Nullable List<AudioTrack> videoTrackList)
            {
                trackList = videoTrackList;
                if (trackRecylerAdapter!=null)
                    trackRecylerAdapter.updateFoldersList(trackList);
            }
        });

        trackRecylerAdapter = new TrackRecyclerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "Track number: "+((int)v.getTag())+" clicked", Toast.LENGTH_SHORT).show();
                navigationManager.VideoPlaybackFragment_Start(trackList,(int)v.getTag());
            }
        }, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                /* Do nothing, we do not allow addition of video songs to playlist or dynamic queue */
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video_tracks_list, container, false);
        recyclerView  = (RecyclerView) rootView.findViewById(R.id.recyclerView_videoTracksList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(trackRecylerAdapter);
        return rootView;
    }

}
