package com.emo.lkplayer.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.emo.lkplayer.R;
import com.emo.lkplayer.customviews.KnobController;

public class NavToneAndVolFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private KnobController knobControllerVolume;
    private KnobController knobControllerStereoX;
    private KnobController knobControllerMono;
    private Button btn_monoSwtich;

    public NavToneAndVolFragment() {
        // Required empty public constructor
    }

    public static NavToneAndVolFragment newInstance(String param1, String param2) {
        NavToneAndVolFragment fragment = new NavToneAndVolFragment();
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
        btn_monoSwtich          = (Button)         rootView.findViewById(R.id.btn_switchMono);
        btn_monoSwtich.setActivated(true);

        btn_monoSwtich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isActivated()){
                    v.setActivated(false);
                }else{
                    v.setActivated(true);
                }

            }
        });

        knobControllerVolume.setTopRightText("Volume");

        knobControllerMono.setTopRightText("Mono");
        knobControllerMono.setBottomLeftText("L");
        knobControllerMono.setBottomRightText("R");

        knobControllerStereoX.setTopRightText("StereoX");
        return rootView;
    }

}
