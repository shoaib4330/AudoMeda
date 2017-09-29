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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emo.lkplayer.R;
import com.emo.lkplayer.middlelayer.viewmodel.PlaylistViewModel;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;
import com.emo.lkplayer.outerlayer.view.BaseActivity;
import com.emo.lkplayer.outerlayer.view.navigation.BaseNavigationManager;
import com.emo.lkplayer.outerlayer.view.navigation.NavigationManagerContentFlow;

import java.util.List;


public class ListPlaylistFragment extends Fragment implements LifecycleRegistryOwner{

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle()
    {
        return registry;
    }

    private NavigationManagerContentFlow frag_NavigationManager;

    private TextView tv_noContent;
    private RecyclerView recyclerView;
    private PlaylistRecyclerAdapter recyclerAdapter;

    private List<Playlist.UserDefinedPlaylist> plList;

    private PlaylistViewModel playlistViewModel;

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

        /*Get the viewModel instance firstly*/
        playlistViewModel = ViewModelProviders.of(this).get(PlaylistViewModel.class);
        /* Now start observing the list if it changes in model */
        playlistViewModel.getUserDefinedPlaylists().observe(this, new Observer<List<Playlist.UserDefinedPlaylist>>() {
            @Override
            public void onChanged(@Nullable List<Playlist.UserDefinedPlaylist> userDefinedPlaylists)
            {
                onListProvided(userDefinedPlaylists);
            }
        });
        recyclerAdapter = new PlaylistRecyclerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),plList.get( (int)v.getTag() ).getPlaylistName(),Toast.LENGTH_SHORT ).show();
                frag_NavigationManager.startListTracksFragment(null,null,null,plList.get( (int)v.getTag() ).getPlaylistName());
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
        /* give the list for the first time to the recyclerview via this methods */
        onListProvided(playlistViewModel.getUserDefinedPlaylists().getValue());
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            frag_NavigationManager = (NavigationManagerContentFlow) ((BaseActivity)context).getNavigationManager();
        } else {
            throw new RuntimeException(context.toString()
                    + " problem retrieving Navigation Manager");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void onListProvided(List<Playlist.UserDefinedPlaylist> list) {
        if(tv_noContent==null || recyclerView==null)
            return;
        if (list==null || list.size()==0){
            tv_noContent.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            tv_noContent.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        this.plList = list;
        recyclerAdapter.updatePlayList_List(list);
    }
    /*---------------- Recycler view class defined here for this fragment ------------------------*/
    private static class PlaylistRecyclerAdapter extends RecyclerView.Adapter<ListPlaylistFragment.PlaylistRecyclerAdapter.PlaylistHolder> {

        public static class PlaylistHolder extends RecyclerView.ViewHolder {
            private TextView tv_playlistName, tv_playListTracksCount;

            private static View.OnClickListener mOnClickListener;

            public PlaylistHolder(View itemView) {
                super(itemView);
                tv_playlistName = (TextView) itemView.findViewById(R.id.tv_playlistName);
                tv_playListTracksCount = (TextView) itemView.findViewById(R.id.tv_playlistTotalTracks);
            }

            public void bind(Playlist.UserDefinedPlaylist playlist, int position) {
                this.itemView.setTag(position); /* shoaib: to get clicked item position */
                tv_playlistName.setText(playlist.getPlaylistName());
                tv_playListTracksCount.setText(String.valueOf(playlist.getNumOfTracks())+" Track(s)");
                /* shoaib: this onCickListener will be initialized and assigned by setItemViewOnClickListener */
                this.itemView.setOnClickListener(this.mOnClickListener);
            }

            public static void setItemViewOnClickListener(View.OnClickListener listener) {
                mOnClickListener = listener;
            }
        }

        private List<Playlist.UserDefinedPlaylist> adapterList;

        public PlaylistRecyclerAdapter(View.OnClickListener listener) {
            ListPlaylistFragment.PlaylistRecyclerAdapter.PlaylistHolder.setItemViewOnClickListener(listener);
        }

        public void updatePlayList_List(List<Playlist.UserDefinedPlaylist> list) {
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
