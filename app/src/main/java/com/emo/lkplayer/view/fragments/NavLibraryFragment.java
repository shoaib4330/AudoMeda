package com.emo.lkplayer.view.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.emo.lkplayer.R;
import com.emo.lkplayer.model.content_providers.Specification.AlbumsSpecification;
import com.emo.lkplayer.model.content_providers.Specification.AllorAlbumTrackSpecification;
import com.emo.lkplayer.model.content_providers.Specification.ArtistSpecification;
import com.emo.lkplayer.model.content_providers.Specification.GenreSpecification;
import com.emo.lkplayer.model.content_providers.Specification.LibraryLeadSelectionEventsListener;
import com.emo.lkplayer.model.content_providers.Specification.PlaylistSpecification;
import com.emo.lkplayer.model.content_providers.Specification.iLoaderSpecification;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavLibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavLibraryFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1,mParam2;

    private View libraryLead_allSongs,libraryLead_albums,libraryLead_artists,libraryLead_genres,
    libraryLead_playlists,libraryLead_queue,libraryLead_topRated,libraryLead_recentlyAdded;

    private LibraryLeadSelectionEventsListener eventsListener;

    public NavLibraryFragment() {
        // Required empty public constructor
    }


    public static NavLibraryFragment newInstance(String param1, String param2) {
        NavLibraryFragment fragment = new NavLibraryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        eventsListener = (LibraryLeadSelectionEventsListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private void initViews(View gRootView){
        libraryLead_allSongs        = gRootView.findViewById(R.id.leadItem_allSongs);
        libraryLead_albums          = gRootView.findViewById(R.id.leadItem_albums);
        libraryLead_artists         = gRootView.findViewById(R.id.leadItem_artists);
        libraryLead_genres          = gRootView.findViewById(R.id.leadItem_genres);
        libraryLead_playlists       = gRootView.findViewById(R.id.leadItem_playlists);
        libraryLead_queue           = gRootView.findViewById(R.id.leadItem_queue);
        libraryLead_topRated        = gRootView.findViewById(R.id.leadItem_topRated);
        libraryLead_recentlyAdded   = gRootView.findViewById(R.id.leadItem_recentlyAdded);

        libraryLead_allSongs.setOnClickListener(this);
        libraryLead_albums.setOnClickListener(this);
        libraryLead_artists.setOnClickListener(this);
        libraryLead_genres.setOnClickListener(this);
        libraryLead_playlists.setOnClickListener(this);
        libraryLead_queue.setOnClickListener(this);
        libraryLead_topRated.setOnClickListener(this);
        libraryLead_recentlyAdded.setOnClickListener(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);
        /* initialize all views and assign them listeners*/
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int leadID = v.getId();
        if (leadID==R.id.leadItem_allSongs)
        {
            Toast.makeText(getContext(), "All Songs Clicked", Toast.LENGTH_SHORT).show();
            iLoaderSpecification specification = new AllorAlbumTrackSpecification();
            eventsListener.onSelectionWithSpecificationProvision(specification,true);
            return;
        }
        else if (leadID==R.id.leadItem_albums){
            Toast.makeText(getContext(), "All Albums Clicked", Toast.LENGTH_SHORT).show();
            iLoaderSpecification specification = new AlbumsSpecification();
            eventsListener.onSelectionWithSpecificationProvision(specification,false);
            return;
        }
        else if (leadID==R.id.leadItem_artists){
            Toast.makeText(getContext(), "Artists Clicked", Toast.LENGTH_SHORT).show();
            iLoaderSpecification specification = new ArtistSpecification();
            eventsListener.onSelectionWithSpecificationProvision(specification,false);
            return;
        }
        else if (leadID==R.id.leadItem_genres){
            Toast.makeText(getContext(), "Genres Clicked", Toast.LENGTH_SHORT).show();
            iLoaderSpecification specification = new GenreSpecification();
            eventsListener.onSelectionWithSpecificationProvision(specification,false);
            return;
        }
        else if (leadID==R.id.leadItem_playlists){
            Toast.makeText(getContext(), "Playlist Clicked", Toast.LENGTH_SHORT).show();
            iLoaderSpecification specification = new PlaylistSpecification();
            eventsListener.onSelectionWithSpecificationProvision(specification,false);
            return;
        }
        else if (leadID==R.id.leadItem_queue){

        }
        else if (leadID==R.id.leadItem_topRated){

        }
        else if (leadID==R.id.leadItem_recentlyAdded){
            Toast.makeText(getContext(), "Recently Added Clicked", Toast.LENGTH_SHORT).show();
            iLoaderSpecification specification = new AllorAlbumTrackSpecification.RecentlyAddedTracksSpecification();
            eventsListener.onSelectionWithSpecificationProvision(specification,true);
            return;
        }
        Toast.makeText(getContext(),"Item: "+leadID,Toast.LENGTH_LONG).show();
    }
}
