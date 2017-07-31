package com.emo.lkplayer.outerlayer.view.fragments;


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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emo.lkplayer.ConstantsHolder;
import com.emo.lkplayer.R;
import com.emo.lkplayer.utilities.Utility;
import com.emo.lkplayer.middlelayer.viewmodel.TrackListingViewModel;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.outerlayer.view.FragmentInteractionListener;
import com.emo.lkplayer.outerlayer.view.navigation.NavigationManagerContentFlow;

import java.util.List;


public class ListTrackFragment extends Fragment implements LifecycleRegistryOwner {

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return registry;
    }

    public static final int MY_LOADER_ID = ConstantsHolder.LOADER_ID_TRACKLISTING_FRAGMENT;

    public static final String ARG_VAL_OPEN_AS_ALL_RECENTS = "as_all_recents";
    public static final String ARG_VAL_OPEN_AS_ALL_TRACKS = "as_all_tracks";

    private static final String ARG_FOLDER_NAME = "param1";
    private static final String ARG_ALBUM_NAME = "param2";
    private static final String ARG_ARTIST_NAME = "param3";
    private static final String ARG_GENRE_ID = "param4";
    private static final String ARG_AllTRACKS = "all_tracks";
    private static final String ARG_AllRECENTS = "all_recents";

    long genreID;
    String folderName, albumName , artistName;

    private FragmentInteractionListener interactionListener;

    private NavigationManagerContentFlow navigationManager;

    private View rootView;
    private RecyclerView recyclerView;
    private TrackRecylerAdapter trackRecylerAdapter;

    private List<AudioTrack> trackList;
    private TrackListingViewModel trackListingViewModel;

    public ListTrackFragment()
    {
        // Required empty public constructor
    }

    public static ListTrackFragment newInstance(String folderName, String albumName, String artistName, long genreID)
    {
        ListTrackFragment fragment = new ListTrackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FOLDER_NAME, folderName);
        args.putString(ARG_ALBUM_NAME, albumName);
        args.putString(ARG_ARTIST_NAME, artistName);
        args.putLong(ARG_GENRE_ID, genreID);
        fragment.setArguments(args);
        return fragment;
    }

    public static ListTrackFragment newInstanceWithAllTracks()
    {
        ListTrackFragment fragment = new ListTrackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_AllTRACKS, ARG_VAL_OPEN_AS_ALL_TRACKS);
        fragment.setArguments(args);
        return fragment;
    }

    public static ListTrackFragment newInstanceWithAllRecents()
    {
        ListTrackFragment fragment = new ListTrackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_AllRECENTS, ARG_VAL_OPEN_AS_ALL_RECENTS);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        trackListingViewModel = ViewModelProviders.of(this).get(TrackListingViewModel.class);

        if (getArguments() != null)
        {
            if (ARG_VAL_OPEN_AS_ALL_TRACKS.equals(getArguments().getString(ARG_AllTRACKS)))
            {
                trackListingViewModel.getAudioTracks(TrackListingViewModel.AllTracksSpecification).observe(this, new Observer<List<AudioTrack>>() {
                    @Override
                    public void onChanged(@Nullable List<AudioTrack> tracks)
                    {
                        trackList = tracks;
                        trackRecylerAdapter.updateFoldersList(tracks);
                    }
                });
            }
            else if (ARG_VAL_OPEN_AS_ALL_RECENTS.equals(getArguments().getString(ARG_AllRECENTS)))
            {
                trackListingViewModel.getAudioTracks(TrackListingViewModel.RecentTracksSpecification).observe(this, new Observer<List<AudioTrack>>() {
                    @Override
                    public void onChanged(@Nullable List<AudioTrack> tracks)
                    {
                        trackList = tracks;
                        trackRecylerAdapter.updateFoldersList(tracks);
                    }
                });
            }
            else{
                genreID     = getArguments().getLong(ARG_GENRE_ID, -1);
                albumName   = getArguments().getString(ARG_ALBUM_NAME);
                folderName  = getArguments().getString(ARG_FOLDER_NAME);
                artistName  = getArguments().getString(ARG_ARTIST_NAME);

                trackListingViewModel.getAudioTracks(folderName,albumName,artistName,genreID).observe(this, new Observer<List<AudioTrack>>() {
                    @Override
                    public void onChanged(@Nullable List<AudioTrack> tracks)
                    {
                        trackList = tracks;
                        trackRecylerAdapter.updateFoldersList(tracks);
                    }
                });
            }

        }

        trackRecylerAdapter = new TrackRecylerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                trackListingViewModel.updateCurrentSpecification(folderName,albumName,artistName,genreID,(int) v.getTag());
                Toast.makeText(getContext(), "Track CLicked " + (int) v.getTag(), Toast.LENGTH_SHORT).show();
                Log.d("--ListTrackFragment: ", "List Set with size= " + trackList.size() + " and index= " + v.getTag());
                navigationManager.startPlayBackFragment((int) v.getTag(), trackList);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_track_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_tracksList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(trackRecylerAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener)
        {
            interactionListener = (FragmentInteractionListener) context;
            this.navigationManager = (NavigationManagerContentFlow) interactionListener.getNavigationManager();
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private static class TrackRecylerAdapter extends RecyclerView.Adapter<ListTrackFragment.TrackRecylerAdapter.TrackViewHolder> {

        public static class TrackViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_trackTitle, tv_Duration, tv_ArtistName;

            private static View.OnClickListener mOnClickListener;

            public TrackViewHolder(View itemView)
            {
                super(itemView);
                tv_trackTitle = (TextView) itemView.findViewById(R.id.tv_trackTitle);
                tv_Duration = (TextView) itemView.findViewById(R.id.tv_trackDuration);
                tv_ArtistName = (TextView) itemView.findViewById(R.id.tv_trackArtist);
            }

            public void bind(AudioTrack track, int position)
            {
                this.itemView.setTag(position); /* shoaib: to get clicked item position */
                tv_trackTitle.setText(track.getTrackTitle());
                tv_ArtistName.setText(track.getArtistName());
                tv_Duration.setText(Utility.millisToTrackTimeFormat(track.getTrackDuration()));
                /* shoaib: this onCickListener will be initialized and assigned by setItemViewOnClickListener */
                this.itemView.setOnClickListener(this.mOnClickListener);
            }

            public static void setItemViewOnClickListener(View.OnClickListener listener)
            {
                mOnClickListener = listener;
            }
        }

        private List<AudioTrack> adapterTrackList;

        public TrackRecylerAdapter(View.OnClickListener listener)
        {
            TrackRecylerAdapter.TrackViewHolder.setItemViewOnClickListener(listener);
        }

        public void updateFoldersList(List<AudioTrack> tracksList)
        {
            this.adapterTrackList = tracksList;
            notifyDataSetChanged();
        }

        @Override
        public TrackRecylerAdapter.TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.m_track_view, parent, false);
            TrackRecylerAdapter.TrackViewHolder fvh = new TrackRecylerAdapter.TrackViewHolder(itemView);
            return fvh;
        }

        @Override
        public void onBindViewHolder(TrackRecylerAdapter.TrackViewHolder holder, int position)
        {
            holder.bind(this.adapterTrackList.get(position), position);
        }

        @Override
        public int getItemCount()
        {
            if (this.adapterTrackList != null)
                return this.adapterTrackList.size();
            return 0;
        }
    }
}
