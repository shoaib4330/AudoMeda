package com.emo.lkplayer.view.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emo.lkplayer.R;
import com.emo.lkplayer.Utility;
import com.emo.lkplayer.controller.CurrentDataController;
import com.emo.lkplayer.controller.TrackController;
import com.emo.lkplayer.model.content_providers.TracksProvider;
import com.emo.lkplayer.model.content_providers.Specification.iLoaderSpecification;
import com.emo.lkplayer.model.entities.AudioTrack;
import com.emo.lkplayer.view.FragmentInteractionListener;
import com.emo.lkplayer.view.navigation.NavigationManagerContentFlow;

import java.util.List;


public class ListTrackFragment extends Fragment implements TrackController.TrackControllerEventsListener {

    public static final String ARG_VAL_OPEN_AS_ALL_RECENTS = "as_all_recents";

    private static final String ARG_FOLDER_NAME = "param1";
    private static final String ARG_ALBUM_NAME = "param2";
    private static final String ARG_ARTIST_NAME = "param3";
    private static final String ARG_GENRE_ID = "param4";
    private static final String ARG_IF_AS_ALL_RECENTS = "param5";

    private FragmentInteractionListener interactionListener;

    private NavigationManagerContentFlow navigationManager;

    private View rootView;
    private RecyclerView recyclerView;
    private TrackRecylerAdapter trackRecylerAdapter;

    private List<AudioTrack> trackList;
    private TrackController trackController;

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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String folderName;
        String albumName;
        String artistName;
        long genreID;

        trackController = new TrackController(getContext(), getLoaderManager());

        if (getArguments() != null)
        {
            folderName = getArguments().getString(ARG_FOLDER_NAME);
            albumName = getArguments().getString(ARG_ALBUM_NAME);
            artistName = getArguments().getString(ARG_ARTIST_NAME);
            genreID = getArguments().getLong(ARG_GENRE_ID, -1);

            if (folderName != null)
            {
                trackController.retrieveAudioVideoTracksByFolder(folderName);
            } else if (albumName != null)
            {
                trackController.retrieveAudioVideoTracksByAlbum(albumName);
            } else if (artistName != null)
            {
                trackController.retrieveAudioVideoTracksByArtist(artistName);
            } else if (genreID != -1)
            {
                trackController.retrieveAudioTracksByGenre(genreID);
            }
            else if (getArguments().getString(ARG_IF_AS_ALL_RECENTS)!=null && getArguments().getString(ARG_IF_AS_ALL_RECENTS).equals(ARG_VAL_OPEN_AS_ALL_RECENTS)){
                trackController.retrieveAudioVideoTracksRecentlyAdded();
            }
            else{
                trackController.retrieveAudioVideoTracksAll();
            }
        }

        trackRecylerAdapter = new TrackRecylerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "Track CLicked " + (int) v.getTag(), Toast.LENGTH_SHORT).show();
                new CurrentDataController().setNewTrackListPlusIndex(trackList, (int) v.getTag());
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

    @Override
    public void onStart()
    {
        super.onStart();
        /* shoaib: register to the provider's data delivery events */
        trackController.register(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        /* shoaib: Unregister from the provider's data delivery events */
        trackController.unregister();
    }

    @Override
    public void onTrackListProvision(List<AudioTrack> list)
    {
        this.trackList = list;
        this.trackRecylerAdapter.updateFoldersList(list);
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
