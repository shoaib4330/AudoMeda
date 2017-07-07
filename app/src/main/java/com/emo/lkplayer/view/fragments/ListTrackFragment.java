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
import com.emo.lkplayer.model.content_providers.TracksProvider;
import com.emo.lkplayer.model.content_providers.Specification.iLoaderSpecification;
import com.emo.lkplayer.model.entities.AudioTrack;
import com.emo.lkplayer.view.FragmentInteractionListener;
import com.emo.lkplayer.view.navigation.NavigationManagerContentFlow;

import java.util.List;


public class ListTrackFragment extends Fragment implements TracksProvider.MediaProviderEventsListener {


    private static final String ARG_PARAM1 = "param1";
    private FragmentInteractionListener interactionListener;

    private NavigationManagerContentFlow navigationManager;

    private View rootView;
    private RecyclerView recyclerView;

    private TrackRecylerAdapter trackRecylerAdapter;

    /* shoaib: logical part */
    private iLoaderSpecification specification;
    private TracksProvider tracksProvider;

    private List<AudioTrack> trackList;

    public ListTrackFragment() {
        // Required empty public constructor
    }


    public static ListTrackFragment newInstance(iLoaderSpecification param1) {
        ListTrackFragment fragment = new ListTrackFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            interactionListener = (FragmentInteractionListener) context;
            this.navigationManager = (NavigationManagerContentFlow) interactionListener.getNavigationManager();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            specification = (iLoaderSpecification) getArguments().getSerializable(ARG_PARAM1);
        }
        tracksProvider = new TracksProvider(getContext(), getLoaderManager());
        tracksProvider.setSpecification(specification);
        tracksProvider.requestTrackData();
        trackRecylerAdapter = new TrackRecylerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Track CLicked "+(int)v.getTag(), Toast.LENGTH_SHORT).show();
                new CurrentDataController().setNewTrackListPlusIndex(trackList,(int)v.getTag());
                Log.d("--ListTrackFragment: ","List Set with size= "+trackList.size()+" and index= "+v.getTag());
                navigationManager.startPlayBackFragment((int)v.getTag(),trackList);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_track_list, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_tracksList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(trackRecylerAdapter);
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        /* shoaib: register to the provider's data delivery events */
        tracksProvider.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        /* shoaib: Unregister from the provider's data delivery events */
        tracksProvider.unRegister();
    }

    @Override
    public void onListCreated(List<AudioTrack> pTrackList) {
        this.trackList = pTrackList;
        this.trackRecylerAdapter.updateFoldersList(pTrackList);
    }


    private static class TrackRecylerAdapter extends RecyclerView.Adapter<ListTrackFragment.TrackRecylerAdapter.TrackViewHolder> {

        public static class TrackViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_trackTitle, tv_Duration, tv_ArtistName;

            private static View.OnClickListener mOnClickListener;

            public TrackViewHolder(View itemView) {
                super(itemView);
                tv_trackTitle = (TextView) itemView.findViewById(R.id.tv_trackTitle);
                tv_Duration = (TextView) itemView.findViewById(R.id.tv_trackDuration);
                tv_ArtistName = (TextView) itemView.findViewById(R.id.tv_trackArtist);
            }

            public void bind(AudioTrack track, int position) {
                this.itemView.setTag(position); /* shoaib: to get clicked item position */
                tv_trackTitle.setText(track.getTrackTitle());
                tv_ArtistName.setText(track.getArtistName());
                tv_Duration.setText(Utility.millisToTrackTimeFormat(track.getTrackDuration()));
                /* shoaib: this onCickListener will be initialized and assigned by setItemViewOnClickListener */
                this.itemView.setOnClickListener(this.mOnClickListener);
            }

            public static void setItemViewOnClickListener(View.OnClickListener listener) {
                mOnClickListener = listener;
            }
        }

        private List<AudioTrack> adapterTrackList;

        public TrackRecylerAdapter(View.OnClickListener listener) {
            TrackRecylerAdapter.TrackViewHolder.setItemViewOnClickListener(listener);
        }

        public void updateFoldersList(List<AudioTrack> tracksList) {
            this.adapterTrackList = tracksList;
            notifyDataSetChanged();
        }

        @Override
        public TrackRecylerAdapter.TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.m_track_view, parent, false);
            TrackRecylerAdapter.TrackViewHolder fvh = new TrackRecylerAdapter.TrackViewHolder(itemView);
            return fvh;
        }

        @Override
        public void onBindViewHolder(TrackRecylerAdapter.TrackViewHolder holder, int position) {
            holder.bind(this.adapterTrackList.get(position), position);
        }

        @Override
        public int getItemCount() {
            if (this.adapterTrackList != null)
                return this.adapterTrackList.size();
            return 0;
        }
    }
}
