package com.emo.emomediaplayerpro.view_ui.view.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emo.emomediaplayerpro.R;
import com.emo.emomediaplayerpro.view_ui.view.BaseActivity;
import com.emo.emomediaplayerpro.view_ui.view.navigation.NavigationManagerContentFlow;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavLibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavLibraryFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View libraryLead_allSongs, libraryLead_albums, libraryLead_artists, libraryLead_genres,
            libraryLead_playlists, libraryLead_queue, libraryLead_topRated, libraryLead_recentlyAdded,
            libraryLead_videoTracksList;

    private NavigationManagerContentFlow frag_NavigationManager;

    public NavLibraryFragment()
    {
        // Required empty public constructor
    }


    public static NavLibraryFragment newInstance(String param1, String param2)
    {
        NavLibraryFragment fragment = new NavLibraryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof BaseActivity)
        {
            frag_NavigationManager = (NavigationManagerContentFlow) ((BaseActivity) context).getNavigationManager();
        } else
        {
            throw new RuntimeException(context.toString()
                    + " problem retrieving Navigation Manager");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private void initViews(View gRootView)
    {
        libraryLead_allSongs = gRootView.findViewById(R.id.leadItem_allSongs);
        libraryLead_albums = gRootView.findViewById(R.id.leadItem_albums);
        libraryLead_artists = gRootView.findViewById(R.id.leadItem_artists);
        libraryLead_genres = gRootView.findViewById(R.id.leadItem_genres);
        libraryLead_playlists = gRootView.findViewById(R.id.leadItem_playlists);
        libraryLead_queue = gRootView.findViewById(R.id.leadItem_queue);
        libraryLead_topRated = gRootView.findViewById(R.id.leadItem_topRated);
        libraryLead_recentlyAdded = gRootView.findViewById(R.id.leadItem_recentlyAdded);
        libraryLead_videoTracksList = gRootView.findViewById(R.id.leadItem_videoTracks);

        libraryLead_allSongs.setOnClickListener(this);
        libraryLead_albums.setOnClickListener(this);
        libraryLead_artists.setOnClickListener(this);
        libraryLead_genres.setOnClickListener(this);
        libraryLead_playlists.setOnClickListener(this);
        libraryLead_queue.setOnClickListener(this);
        libraryLead_topRated.setOnClickListener(this);
        libraryLead_recentlyAdded.setOnClickListener(this);
        libraryLead_videoTracksList.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);
        /* initialize all views and assign them listeners*/
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        int leadID = v.getId();
        if (leadID == R.id.leadItem_allSongs)
        {
            frag_NavigationManager.startListTracksFragment_WithAllTracks();
            return;
        } else if (leadID == R.id.leadItem_albums)
        {
            frag_NavigationManager.startAlbumsFragment();
            return;
        } else if (leadID == R.id.leadItem_artists)
        {
            frag_NavigationManager.startArtistFragment();
            return;
        } else if (leadID == R.id.leadItem_genres)
        {
            frag_NavigationManager.startGenreFragment();
            return;
        } else if (leadID == R.id.leadItem_playlists)
        {
            frag_NavigationManager.Playlists_Fragment_Start();
            return;
        } else if (leadID == R.id.leadItem_queue)
        {
            frag_NavigationManager.ListTracksFragment_WithDQ_Start();
        } else if (leadID == R.id.leadItem_topRated)
        {

        } else if (leadID == R.id.leadItem_recentlyAdded)
        {
            frag_NavigationManager.startListTracksFragment_WithAllRecentTracks();
            return;
        } else if (leadID == R.id.leadItem_videoTracks)
        {
            frag_NavigationManager.VideoTrackListFragment_Start();
        }
    }
}
