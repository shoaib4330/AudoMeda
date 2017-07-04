package com.emo.lkplayer.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emo.lkplayer.R;
import com.emo.lkplayer.model.content_providers.PlaylistProvider;
import com.emo.lkplayer.model.content_providers.Specification.LibraryLeadSelectionEventsListener;
import com.emo.lkplayer.model.content_providers.Specification.PlaylistSpecification;
import com.emo.lkplayer.model.entities.Playlist;

import java.util.List;


public class ListPlaylistFragment extends Fragment implements PlaylistProvider.MediaProviderEventsListener {

    private LibraryLeadSelectionEventsListener eventsListener;

    private TextView tv_noContent;
    private RecyclerView recyclerView;
    private PlaylistRecyclerAdapter recyclerAdapter;

    private PlaylistProvider playlistProvider;
    private List<Playlist> plList;

    public ListPlaylistFragment() {
        // Required empty public constructor
    }


    public static ListPlaylistFragment newInstance() {
        ListPlaylistFragment fragment = new ListPlaylistFragment();
        fragment.setArguments(null);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playlistProvider = new PlaylistProvider(getContext(),getLoaderManager());
        playlistProvider.setSpecification(new PlaylistSpecification());
        playlistProvider.requestTrackData();
        recyclerAdapter = new PlaylistRecyclerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),plList.get( (int)v.getTag() ).getName(),Toast.LENGTH_SHORT ).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_playlist, container, false);
        recyclerView  = (RecyclerView) rootView.findViewById(R.id.recyclerView_playlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerAdapter);
        tv_noContent = (TextView) rootView.findViewById(R.id.tv_noContent);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        playlistProvider.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        playlistProvider.unRegister();
    }

    @Override
    public void onListCreated(List<Playlist> list) {
        if (list==null || list.size()==0){
            tv_noContent.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            tv_noContent.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        this.plList = list;
        recyclerAdapter.updateFoldersList(list);
    }

    private static class PlaylistRecyclerAdapter extends RecyclerView.Adapter<ListPlaylistFragment.PlaylistRecyclerAdapter.PlaylistHolder> {

        public static class PlaylistHolder extends RecyclerView.ViewHolder {
            private TextView tv_playlistName, tv_playListTracksCount;

            private static View.OnClickListener mOnClickListener;

            public PlaylistHolder(View itemView) {
                super(itemView);
                tv_playlistName = (TextView) itemView.findViewById(R.id.tv_playlistName);
                tv_playListTracksCount = (TextView) itemView.findViewById(R.id.tv_playlistTotalTracks);
            }

            public void bind(Playlist playlist, int position) {
                this.itemView.setTag(position); /* shoaib: to get clicked item position */
                tv_playlistName.setText(playlist.getName());
                tv_playListTracksCount.setText(String.valueOf(playlist.getNumTracks())+" Track(s)");
                /* shoaib: this onCickListener will be initialized and assigned by setItemViewOnClickListener */
                this.itemView.setOnClickListener(this.mOnClickListener);
            }

            public static void setItemViewOnClickListener(View.OnClickListener listener) {
                mOnClickListener = listener;
            }
        }

        private List<Playlist> adapterList;

        public PlaylistRecyclerAdapter(View.OnClickListener listener) {
            ListPlaylistFragment.PlaylistRecyclerAdapter.PlaylistHolder.setItemViewOnClickListener(listener);
        }

        public void updateFoldersList(List<Playlist> list) {
            this.adapterList = list;
            notifyDataSetChanged();
        }

        @Override
        public ListPlaylistFragment.PlaylistRecyclerAdapter.PlaylistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.m_playlist_view, parent, false);
            ListPlaylistFragment.PlaylistRecyclerAdapter.PlaylistHolder fvh = new ListPlaylistFragment.PlaylistRecyclerAdapter.PlaylistHolder(itemView);
            return fvh;
        }

        @Override
        public void onBindViewHolder(ListPlaylistFragment.PlaylistRecyclerAdapter.PlaylistHolder holder, int position) {
            holder.bind(this.adapterList.get(position), position);
        }

        @Override
        public int getItemCount() {
            if (this.adapterList != null)
                return this.adapterList.size();
            return 0;
        }
    }
}
