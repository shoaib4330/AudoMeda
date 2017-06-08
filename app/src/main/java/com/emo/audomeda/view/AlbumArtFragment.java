package com.emo.audomeda.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.emo.audomeda.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumArtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumArtFragment extends Fragment {

    private static final String ARG_PARAM1 = "album_art_path";
    private String albumArtPath;

    private ImageView imgv_AlbumArt;


    public AlbumArtFragment() {
        // Required empty public constructor
    }

    public static AlbumArtFragment newInstance(String param1) {
        AlbumArtFragment fragment = new AlbumArtFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
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
        imgv_AlbumArt   = (ImageView) layout.findViewById(R.id.imgV_SliderAlbumArt);

        if (albumArtPath!=null)
        {

        }
        return layout;
    }

}
