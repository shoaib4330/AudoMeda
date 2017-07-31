package com.emo.lkplayer.outerlayer.view.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emo.lkplayer.R;
import com.emo.lkplayer.middlelayer.viewmodel.AlbumsListingViewModel;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.LibraryLeadSelectionEventsListener;
import com.emo.lkplayer.innerlayer.model.entities.Album;
import com.emo.lkplayer.outerlayer.view.navigation.BaseNavigationManager;
import com.emo.lkplayer.outerlayer.view.navigation.NavigationManagerContentFlow;

import java.util.List;


public class ListAlbumsFragment extends Fragment implements AlbumsListingViewModel.AlbumsControllerEventsListener {

    public interface InteractionListener {
        BaseNavigationManager getNavigationManager();
    }

    private View rootView;
    private RecyclerView recyclerView;
    private AlbumRecyclerAdapter albumRecyclerAdapter;
    private NavigationManagerContentFlow frag_NavigationManager;

    /* shoaib: logical part */
    private List<Album> albumList;
    private AlbumsListingViewModel albumsListingViewModel;

    public ListAlbumsFragment()
    {
        // Required empty public constructor
    }

    public static ListAlbumsFragment newInstance()
    {
        ListAlbumsFragment fragment = new ListAlbumsFragment();
        fragment.setArguments(null);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        albumsListingViewModel = new AlbumsListingViewModel(getContext(), getLoaderManager());
        albumsListingViewModel.retrieveAudioVideoAlbumsAll();
        albumRecyclerAdapter = new ListAlbumsFragment.AlbumRecyclerAdapter(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "Folder number: " + (int) v.getTag(), Toast.LENGTH_SHORT).show();
                frag_NavigationManager.startListTracksFragment(null, (albumList.get((int) v.getTag())).getAlbumTitle(), null);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_all_albums, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_albumsList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(albumRecyclerAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof LibraryLeadSelectionEventsListener)
        {
            frag_NavigationManager = (NavigationManagerContentFlow) ((ListAlbumsFragment.InteractionListener) context).getNavigationManager();
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        this.albumsListingViewModel.register(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        this.albumsListingViewModel.unregister();
    }

    @Override
    public void onAlbumListProvision(List<Album> list)
    {
        this.albumList = list;
        this.albumRecyclerAdapter.updateFoldersList(list);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private static class AlbumRecyclerAdapter extends RecyclerView.Adapter<ListAlbumsFragment.AlbumRecyclerAdapter.AlbumViewHolder> {

        public static class AlbumViewHolder extends RecyclerView.ViewHolder {
            private TextView tv_albumTitle, tv_albumArtist;
            private ImageView iv_albumImage;

            private static View.OnClickListener mOnClickListener;

            public AlbumViewHolder(View itemView)
            {
                super(itemView);
                tv_albumTitle = (TextView) itemView.findViewById(R.id.tv_albumTitle);
                tv_albumArtist = (TextView) itemView.findViewById(R.id.tv_albumArtist);
                iv_albumImage = (ImageView) itemView.findViewById(R.id.iv_albumImage);
            }

            public void bind(Album album, int position)
            {
                this.itemView.setTag(position); /* shoaib: to get clicked item position */
                tv_albumTitle.setText(album.getAlbumTitle());
                tv_albumArtist.setText(album.getAlbumArtist());
                if (album.getAlbumArtURI() == null)
                {
                    iv_albumImage.setImageResource(R.drawable.album_default_ico);
                } else
                {
                    iv_albumImage.setImageBitmap(BitmapFactory.decodeFile(album.getAlbumArtURI()));
                }
                /* shoaib: this onCickListener will be initialized and assigned by setItemViewOnClickListener */
                this.itemView.setOnClickListener(this.mOnClickListener);
            }

            public static void setItemViewOnClickListener(View.OnClickListener listener)
            {
                mOnClickListener = listener;
            }
        }

        private List<Album> adapterAlbumList;

        public AlbumRecyclerAdapter(View.OnClickListener listener)
        {
            ListAlbumsFragment.AlbumRecyclerAdapter.AlbumViewHolder.setItemViewOnClickListener(listener);
        }

        public void updateFoldersList(List<Album> albumList)
        {
            this.adapterAlbumList = albumList;
            notifyDataSetChanged();
        }

        @Override
        public ListAlbumsFragment.AlbumRecyclerAdapter.AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.m_album_view, parent, false);
            ListAlbumsFragment.AlbumRecyclerAdapter.AlbumViewHolder fvh = new ListAlbumsFragment.AlbumRecyclerAdapter.AlbumViewHolder(itemView);
            return fvh;
        }

        @Override
        public void onBindViewHolder(ListAlbumsFragment.AlbumRecyclerAdapter.AlbumViewHolder holder, int position)
        {
            holder.bind(this.adapterAlbumList.get(position), position);
        }

        @Override
        public int getItemCount()
        {
            if (this.adapterAlbumList != null)
                return this.adapterAlbumList.size();
            return 0;
        }
    }
}
