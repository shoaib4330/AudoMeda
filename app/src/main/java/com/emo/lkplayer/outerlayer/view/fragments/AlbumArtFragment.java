package com.emo.lkplayer.outerlayer.view.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emo.lkplayer.R;
import com.emo.lkplayer.innerlayer.model.entities.Album;
import com.emo.lkplayer.middlelayer.viewmodel.ArtFragViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumArtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumArtFragment extends Fragment {

    private static final String ARG_PARAM1 = "album_art_path";
    private static final String ARG_PARAM2 = "album_id";

    public AlbumArtFragment()
    {
        // Required empty public constructor
    }

    public static AlbumArtFragment newInstance(String trackArtUri)
    {
        AlbumArtFragment fragment = new AlbumArtFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, trackArtUri);
        fragment.setArguments(args);
        return fragment;
    }

    public static AlbumArtFragment newInstance(long albumID)
    {
        AlbumArtFragment fragment = new AlbumArtFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM2, albumID);
        fragment.setArguments(args);
        return fragment;
    }

    /*---------------------------------- Fields ---------------------------- */
    private long albumID = -1;

    private AppCompatImageView imgv_AlbumArt = null;
    private AppCompatImageView imgv_background_blurred = null;

    private ArtFragViewModel viewModel;

    Bitmap blurTemplate = null;
    Bitmap trackBitmap = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            albumID = getArguments().getLong(ARG_PARAM2);
        }
        viewModel = ViewModelProviders.of(this).get(ArtFragViewModel.class);
        if (albumID != -1)
        {
            viewModel.getTrackAlbum(albumID).observeForever(new Observer<List<Album>>() {
                @Override
                public void onChanged(@Nullable List<Album> albumList)
                {
                    if (albumList.size() == 0)
                        return;
                    String artUri = albumList.get(0).getAlbumArtURI();
                    decodeAndAssignBitmaps(artUri);
                    setImages();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_album_art, container, false);
        imgv_AlbumArt = (AppCompatImageView) layout.findViewById(R.id.imgV_SliderAlbumArt);
        imgv_background_blurred = (AppCompatImageView) layout.findViewById(R.id.imgV_back_blurred);
        setImages();
        return layout;
    }

    private void setImages()
    {
        if (imgv_background_blurred != null && imgv_AlbumArt != null && blurTemplate != null && trackBitmap != null)
        {
            this.imgv_AlbumArt.setImageBitmap(trackBitmap);
            this.imgv_background_blurred.setImageBitmap(blurTemplate);
        }
    }

    private void decodeAndAssignBitmaps(String artUri)
    {
        if (artUri == null)
            return;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        blurTemplate = BitmapFactory.decodeFile(artUri, options);
        trackBitmap = BitmapFactory.decodeFile(artUri);
    }

    public Bitmap getAlbumArt()
    {
        return trackBitmap;
    }
}
