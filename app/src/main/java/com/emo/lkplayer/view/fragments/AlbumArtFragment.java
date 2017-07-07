package com.emo.lkplayer.view.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emo.lkplayer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumArtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumArtFragment extends Fragment {

    private static final String ARG_PARAM1 = "album_art_path";
    private String albumArtPath;

    private AppCompatImageView imgv_AlbumArt;
    private AppCompatImageView imgv_background_blurred;
    //private int full_frame_width;
    //private int full_frame_height;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public AlbumArtFragment() {
        // Required empty public constructor
    }

    public static AlbumArtFragment newInstance(String trackArtUri) {
        AlbumArtFragment fragment = new AlbumArtFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, trackArtUri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            albumArtPath = getArguments().getString(ARG_PARAM1);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout     = inflater.inflate(R.layout.fragment_album_art, container, false);
        imgv_AlbumArt   = (AppCompatImageView) layout.findViewById(R.id.imgV_SliderAlbumArt);
        imgv_background_blurred = (AppCompatImageView) layout.findViewById(R.id.imgV_back_blurred);

        if (albumArtPath!=null)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap blurTemplate = BitmapFactory.decodeFile(albumArtPath, options);

            Bitmap trackBitmap = BitmapFactory.decodeFile(albumArtPath);
            this.imgv_AlbumArt.setImageBitmap(trackBitmap);
            this.imgv_background_blurred.setImageBitmap(blurTemplate);
        }
        return layout;
    }

}
