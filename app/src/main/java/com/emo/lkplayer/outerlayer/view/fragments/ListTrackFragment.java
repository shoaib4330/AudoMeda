package com.emo.lkplayer.outerlayer.view.fragments;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.emo.lkplayer.ConstantsHolder;
import com.emo.lkplayer.R;
import com.emo.lkplayer.innerlayer.interactors.Interactor_ModifyDQ;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;
import com.emo.lkplayer.middlelayer.viewmodel.PlaylistViewModel;
import com.emo.lkplayer.outerlayer.customviews.PlayListDialog;
import com.emo.lkplayer.outerlayer.customviews.TrackInfoDialog;
import com.emo.lkplayer.utilities.Utility;
import com.emo.lkplayer.middlelayer.viewmodel.TrackListingViewModel;
import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.outerlayer.view.FragmentInteractionListener;
import com.emo.lkplayer.outerlayer.view.navigation.NavigationManagerContentFlow;

import java.util.ArrayList;
import java.util.List;


public class ListTrackFragment extends Fragment implements LifecycleRegistryOwner {

    public enum ListingMode {
        MODE_FOLDER, MODE_ALL, MODE_ALBUMS, MODE_ARTISTS, MODE_GENRE, MODE_PLAYLIST, MODE_DQ, MODE_ALLRECENT;
    }

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle()
    {
        return registry;
    }

    public static final String ARG_VAL_OPEN_AS_ALL_RECENTS = "as_all_recents";
    public static final String ARG_VAL_OPEN_AS_ALL_TRACKS = "as_all_tracks";
    public static final String ARG_VAL_OPEN_AS_DQ_TRACKS = "as_dq_tracks";


    private static final String ARG_FOLDER_NAME = "param1";
    private static final String ARG_ALBUM_NAME = "param2";
    private static final String ARG_ARTIST_NAME = "param3";
    private static final String ARG_GENRE_ID = "param4";
    private static final String ARG_PLAYLSIT_NAME = "param5";
    private static final String ARG_AllTRACKS = "all_tracks";
    private static final String ARG_AllRECENTS = "all_recents";
    private static final String ARG_DQTRACKS = "dq_tracks";

    private static final String ARG_FRAG_OPEN_MODE = "mode_enum";
    private static final String ARG_FRAG_PARAM_VAL = "val_according_to_mode";

    long genreID;
    String folderName, albumName, artistName, playlistName;

    private FragmentInteractionListener interactionListener;

    private NavigationManagerContentFlow navigationManager;

    private View rootView;
    private RecyclerView recyclerView;
    private TrackRecylerAdapter trackRecylerAdapter;

    private List<AudioTrack> trackList;
    private TrackListingViewModel trackListingViewModel;
    private PlaylistViewModel playlistViewModel;

    Observer<List<AudioTrack>> observer = new Observer<List<AudioTrack>>() {
        @Override
        public void onChanged(@Nullable List<AudioTrack> audioTrackList)
        {
            trackList = audioTrackList;
            trackRecylerAdapter.updateFoldersList(audioTrackList);
        }
    };

    private ListingMode FRAG_MODE = null;

    public ListTrackFragment()
    {
        // Required empty public constructor
    }

    public static ListTrackFragment newInstance(String folderName, String albumName, String artistName, String playlistName, long genreID)
    {
        ListTrackFragment fragment = new ListTrackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FOLDER_NAME, folderName);
        args.putString(ARG_ALBUM_NAME, albumName);
        args.putString(ARG_ARTIST_NAME, artistName);
        args.putString(ARG_PLAYLSIT_NAME, playlistName);
        args.putLong(ARG_GENRE_ID, genreID);
        fragment.setArguments(args);
        return fragment;
    }

    public static ListTrackFragment newInstance(ListingMode mode, String val)
    {
        ListTrackFragment fragment = new ListTrackFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FRAG_OPEN_MODE, mode);
        args.putString(ARG_FRAG_PARAM_VAL, val);
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
        playlistViewModel = ViewModelProviders.of(this).get(PlaylistViewModel.class);

        /* If this call is not made, upon opening Playlist dialog by long click, playlists don't show up*/
        playlistViewModel.getUserDefinedPlaylists().observe(this, new Observer<List<Playlist.UserDefinedPlaylist>>() {
            @Override
            public void onChanged(@Nullable List<Playlist.UserDefinedPlaylist> userDefinedPlaylists)
            {

            }
        });
        /* Get the args passed to this frag and request made for trackslist */
        makeTrackListCall();
        /* Setting up recycler-view's adapter */
        trackRecylerAdapter = new TrackRecylerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                trackListingViewModel.updateCurrentSpecification(folderName, albumName, artistName, playlistName, genreID, (int) v.getTag());
                navigationManager.startPlayBackFragment((int) v.getTag(), trackList);
            }
        }, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                buildAlerDialog((int) v.getTag());
                return true;
            }
        });
    }

    private void makeTrackListCall()
    {

        if (getArguments() != null)
        {
            if (getArguments().getSerializable(ARG_FRAG_OPEN_MODE) != null)
            {
                ListingMode mode = (ListingMode) getArguments().getSerializable(ARG_FRAG_OPEN_MODE);
                String val = getArguments().getString(ARG_FRAG_PARAM_VAL);
                /* Set the fragment mode, in which it is open */
                FRAG_MODE = mode;
                if (mode == ListingMode.MODE_ALL)
                    trackListingViewModel.getAudioTracks(TrackListingViewModel.AllTracksSpecification).observe(this, observer);
                else if (mode == ListingMode.MODE_ALLRECENT)
                    trackListingViewModel.getAudioTracks(TrackListingViewModel.RecentTracksSpecification).observe(this, observer);
                else if (mode == ListingMode.MODE_DQ)
                    playlistName = Interactor_ModifyDQ.DQ_AS_PLAYLIST;
                else if (mode == ListingMode.MODE_PLAYLIST)
                    playlistName = val;
                else if (mode == ListingMode.MODE_ALBUMS)
                    albumName = val;
                else if (mode == ListingMode.MODE_ARTISTS)
                    artistName = val;
                else if (mode == ListingMode.MODE_GENRE)
                    genreID = Long.valueOf(val);
                else if (mode == ListingMode.MODE_FOLDER)
                    folderName = val;
                if (trackListingViewModel.getAudioTracks(folderName, albumName, artistName, playlistName, genreID)!=null)
                    trackListingViewModel.getAudioTracks(folderName, albumName, artistName, playlistName, genreID).observe(this, observer);
            }
        }

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

    /*----------------- private declared methods for this fragment ------------------*/
    private void buildAlerDialog(final int trackPos)
    {
        final AlertDialog alertDialog;
        View dialogView = View.inflate(getContext(), R.layout.dialog_track_options, null);
        ImageButton btn_enqueue = (ImageButton) dialogView.findViewById(R.id.btn_dialog_enqueue);
        ImageButton btn_play = (ImageButton) dialogView.findViewById(R.id.btn_dialog_play);
        ImageButton btn_addToPlaylist = (ImageButton) dialogView.findViewById(R.id.btn_dialog_addToPlayList);
        ImageButton btn_delete = (ImageButton) dialogView.findViewById(R.id.btn_dialog_delete);
        ImageButton btn_info = (ImageButton) dialogView.findViewById(R.id.btn_dialog_info);
        ImageButton btn_ringtone = (ImageButton) dialogView.findViewById(R.id.btn_dialog_ringtone);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        alertDialog = builder.create();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int value = trackPos;
                if (v.getId() == R.id.btn_dialog_enqueue)
                {
                    /* Add the track to the Dynamic Queue */
                    trackListingViewModel.enqueueTrack(trackList.get(trackPos));
                } else if (v.getId() == R.id.btn_dialog_play)
                {
                    trackListingViewModel.updateCurrentSpecification(folderName, albumName, artistName, playlistName, genreID, trackPos);
                }
                else if (v.getId() == R.id.btn_dialog_addToPlayList)
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
                            AudioTrack audioTrack = trackList.get(trackPos);
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
                else if (v.getId() == R.id.btn_dialog_delete)
                {
                    trackListingViewModel.deleteTrackFromDevice(trackList.get(trackPos));
                    trackListingViewModel.getAudioTracks(folderName, albumName, artistName, playlistName, genreID).observe(ListTrackFragment.this, observer);

                } else if (v.getId() == R.id.btn_dialog_info)
                {
                    TrackInfoDialog trackInfoDialog = new TrackInfoDialog(getContext(),trackList.get(trackPos));
                    trackInfoDialog.show();
                } else if (v.getId() == R.id.btn_dialog_ringtone)
                {
                    trackListingViewModel.setRingtone(trackList.get(trackPos));
                }
                alertDialog.dismiss();
            }
        };
        btn_enqueue.setOnClickListener(onClickListener);
        btn_addToPlaylist.setOnClickListener(onClickListener);
        btn_delete.setOnClickListener(onClickListener);
        btn_info.setOnClickListener(onClickListener);
        btn_play.setOnClickListener(onClickListener);
        btn_ringtone.setOnClickListener(onClickListener);

        alertDialog.show();
        alertDialog.getWindow().setLayout(getActivity().getWindow().getDecorView().getWidth(), 500);
    }

    private static class TrackRecylerAdapter extends RecyclerView.Adapter<ListTrackFragment.TrackRecylerAdapter.TrackViewHolder> {

        public static class TrackViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_trackTitle, tv_Duration, tv_ArtistName;

            private static View.OnClickListener mOnClickListener;
            private static View.OnLongClickListener mLongClickListener;

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
                this.itemView.setOnLongClickListener(mLongClickListener);
            }

            public static void setItemViewOnClickListener(View.OnClickListener listener, View.OnLongClickListener longClickListener)
            {
                mOnClickListener = listener;
                mLongClickListener = longClickListener;
            }
        }

        private List<AudioTrack> adapterTrackList;

        public TrackRecylerAdapter(View.OnClickListener listener, View.OnLongClickListener longClickListener)
        {
            TrackRecylerAdapter.TrackViewHolder.setItemViewOnClickListener(listener, longClickListener);
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
