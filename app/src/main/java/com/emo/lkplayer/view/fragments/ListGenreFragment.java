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
import com.emo.lkplayer.model.content_providers.GenreProvider;
import com.emo.lkplayer.model.content_providers.Specification.GenreSpecification;
import com.emo.lkplayer.model.content_providers.Specification.LibraryLeadSelectionEventsListener;
import com.emo.lkplayer.model.content_providers.Specification.TracksByGenreSpecification;
import com.emo.lkplayer.model.content_providers.Specification.iLoaderSpecification;
import com.emo.lkplayer.model.entities.Genre;

import java.util.List;


public class ListGenreFragment extends Fragment implements GenreProvider.MediaProviderEventsListener {

    private LibraryLeadSelectionEventsListener eventsListener;

    private GenreProvider genreProvider;

    private RecyclerView recyclerView;
    private GenreRecyclerAdapter genreRecyclerAdapter;

    private List<Genre> genreList;

    public ListGenreFragment() {
        // Required empty public constructor
    }

    public static ListGenreFragment newInstance() {
        ListGenreFragment fragment = new ListGenreFragment();
        fragment.setArguments(null);
        return fragment;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof LibraryLeadSelectionEventsListener) {
            eventsListener = (LibraryLeadSelectionEventsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        genreProvider = new GenreProvider(getContext(),getLoaderManager());
        genreProvider.setSpecification(new GenreSpecification());
        genreProvider.requestTrackData();
        genreRecyclerAdapter = new GenreRecyclerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),genreList.get( (int)v.getTag() ).getGenreName(),Toast.LENGTH_SHORT ).show();
                iLoaderSpecification specification = new TracksByGenreSpecification(genreList.get((int)v.getTag()).getId());
                eventsListener.onSelectionWithSpecificationProvision(specification, true);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rooView  = inflater.inflate(R.layout.fragment_list_genre, container, false);
        recyclerView  = (RecyclerView) rooView.findViewById(R.id.recyclerView_genre);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(genreRecyclerAdapter);
        return rooView;
    }

    @Override
    public void onStart() {
        super.onStart();
        genreProvider.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        genreProvider.unRegister();
    }

    @Override
    public void onListCreated(List<Genre> pTrackList) {
        this.genreList = pTrackList;
        genreRecyclerAdapter.updateFoldersList(pTrackList);
    }

    private static class GenreRecyclerAdapter extends RecyclerView.Adapter<ListGenreFragment.GenreRecyclerAdapter.GenreHolder> {

        public static class GenreHolder extends RecyclerView.ViewHolder {
            private TextView tv_genreName, tv_genreTotalTracks;

            private static View.OnClickListener mOnClickListener;

            public GenreHolder(View itemView) {
                super(itemView);
                tv_genreName = (TextView) itemView.findViewById(R.id.tv_genreName);
                tv_genreTotalTracks = (TextView) itemView.findViewById(R.id.tv_genreTotalTracks);
            }

            public void bind(Genre genre, int position) {
                this.itemView.setTag(position); /* shoaib: to get clicked item position */
                tv_genreName.setText(genre.getGenreName());
                tv_genreTotalTracks.setText(String.valueOf(genre.getCountTracks())+" Track(s)");
                /* shoaib: this onCickListener will be initialized and assigned by setItemViewOnClickListener */
                this.itemView.setOnClickListener(this.mOnClickListener);
            }

            public static void setItemViewOnClickListener(View.OnClickListener listener) {
                mOnClickListener = listener;
            }
        }

        private List<Genre> adapterGenreList;

        public GenreRecyclerAdapter(View.OnClickListener listener) {
            ListGenreFragment.GenreRecyclerAdapter.GenreHolder.setItemViewOnClickListener(listener);
        }

        public void updateFoldersList(List<Genre> artistsList) {
            this.adapterGenreList = artistsList;
            notifyDataSetChanged();
        }

        @Override
        public ListGenreFragment.GenreRecyclerAdapter.GenreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.m_genre_view, parent, false);
            ListGenreFragment.GenreRecyclerAdapter.GenreHolder fvh = new ListGenreFragment.GenreRecyclerAdapter.GenreHolder(itemView);
            return fvh;
        }

        @Override
        public void onBindViewHolder(ListGenreFragment.GenreRecyclerAdapter.GenreHolder holder, int position) {
            holder.bind(this.adapterGenreList.get(position), position);
        }

        @Override
        public int getItemCount() {
            if (this.adapterGenreList != null)
                return this.adapterGenreList.size();
            return 0;
        }
    }
}