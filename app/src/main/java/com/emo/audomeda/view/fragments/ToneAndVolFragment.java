package com.emo.audomeda.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emo.audomeda.R;
import com.emo.audomeda.customviews.KnobController;

public class ToneAndVolFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private KnobController knobControllerVolume;
    private KnobController knobControllerStereoX;
    private KnobController knobControllerMono;

    public ToneAndVolFragment() {
        // Required empty public constructor
    }

    public static ToneAndVolFragment newInstance(String param1, String param2) {
        ToneAndVolFragment fragment = new ToneAndVolFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View rootView           = inflater.inflate(R.layout.fragment_tone_and_vol, container, false);
        knobControllerVolume    = (KnobController) rootView.findViewById(R.id.knob_volume);
        knobControllerStereoX   = (KnobController) rootView.findViewById(R.id.knob_stereoX);
        knobControllerMono      = (KnobController) rootView.findViewById(R.id.knob_mono);

        knobControllerVolume.setTopRightText("Volume");

        knobControllerMono.setTopRightText("Mono");
        knobControllerMono.setBottomLeftText("L");
        knobControllerMono.setBottomRightText("R");

        knobControllerStereoX.setTopRightText("StereoX");
        return rootView;
    }

}
