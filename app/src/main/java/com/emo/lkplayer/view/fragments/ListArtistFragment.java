package com.emo.lkplayer.view.fragments;


import android.content.Context;
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
import com.emo.lkplayer.model.content_providers.ArtistsProvider;
import com.emo.lkplayer.model.content_providers.Specification.ArtistSpecification;
import com.emo.lkplayer.model.content_providers.Specification.LibraryLeadSelectionEventsListener;
import com.emo.lkplayer.model.content_providers.Specification.TracksByArtistSpecification;
import com.emo.lkplayer.model.content_providers.Specification.TracksByGenreSpecification;
import com.emo.lkplayer.model.content_providers.Specification.iLoaderSpecification;
import com.emo.lkplayer.model.entities.Artist;

import java.util.List;


public class ListArtistFragment extends Fragment implements ArtistsProvider.MediaProviderEventsListener {


    private LibraryLeadSelectionEventsListener eventsListener;

    private ArtistsProvider artistsProvider;

    private RecyclerView recyclerView;
    private ArtistRecyclerAdapter artistRecyclerAdapter;

    private List<Artist> artistList;

    public ListArtistFragment() {
        // Required empty public constructor
    }

    public static ListArtistFragment newInstance() {
        ListArtistFragment fragment = new ListArtistFragment();
        fragment.setArguments(null);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artistsProvider = new ArtistsProvider(getContext(),getLoaderManager());
        artistsProvider.setSpecification(new ArtistSpecification());
        artistsProvider.requestTrackData();
        artistRecyclerAdapter = new ArtistRecyclerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),(artistList.get( (int)v.getTag() )).getArtistName(),Toast.LENGTH_SHORT).show();
                iLoaderSpecification specification = new TracksByArtistSpecification(artistList.get((int)v.getTag()).getArtistName());
                eventsListener.onSelectionWithSpecificationProvision(specification, true);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_list_artist, container, false);
        recyclerView  =  (RecyclerView) rootView.findViewById(R.id.recyclerView_artists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(artistRecyclerAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LibraryLeadSelectionEventsListener) {
            eventsListener = (LibraryLeadSelectionEventsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventsListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        artistsProvider.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        artistsProvider.unRegister();
    }

    @Override
    public void onListCreated(List<Artist> list) {
        this.artistList = list;
        artistRecyclerAdapter.updateFoldersList(list);
    }


    private static class ArtistRecyclerAdapter extends RecyclerView.Adapter<ListArtistFragment.ArtistRecyclerAdapter.ArtistViewHolder> {

        public static class ArtistViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_artistName, tv_artistTotalTracks;

            private static View.OnClickListener mOnClickListener;

            public ArtistViewHolder(View itemView) {
                super(itemView);
                tv_artistName = (TextView) itemView.findViewById(R.id.tv_artistName);
                tv_artistTotalTracks = (TextView) itemView.findViewById(R.id.tv_artistTotalTracks);
            }

            public void bind(Artist artist, int position) {
                this.itemView.setTag(position); /* shoaib: to get clicked item position */
                tv_artistName.setText(artist.getArtistName());
                tv_artistTotalTracks.setText(String.valueOf(artist.getNumOfTracks()));
                /* shoaib: this onCickListener will be initialized and assigned by setItemViewOnClickListener */
                this.itemView.setOnClickListener(this.mOnClickListener);
            }

            public static void setItemViewOnClickListener(View.OnClickListener listener) {
                mOnClickListener = listener;
            }
        }

        private List<Artist> adapterArtistList;

        public ArtistRecyclerAdapter(View.OnClickListener listener) {
            ListArtistFragment.ArtistRecyclerAdapter.ArtistViewHolder.setItemViewOnClickListener(listener);
        }

        public void updateFoldersList(List<Artist> artistsList) {
            this.adapterArtistList = artistsList;
            notifyDataSetChanged();
        }

        @Override
        public ListArtistFragment.ArtistRecyclerAdapter.ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.m_artist_view, parent, false);
            ListArtistFragment.ArtistRecyclerAdapter.ArtistViewHolder fvh = new ListArtistFragment.ArtistRecyclerAdapter.ArtistViewHolder(itemView);
            return fvh;
        }

        @Override
        public void onBindViewHolder(ListArtistFragment.ArtistRecyclerAdapter.ArtistViewHolder holder, int position) {
            holder.bind(this.adapterArtistList.get(position), position);
        }

        @Override
        public int getItemCount() {
            if (this.adapterArtistList != null)
                return this.adapterArtistList.size();
            return 0;
        }
    }
}
