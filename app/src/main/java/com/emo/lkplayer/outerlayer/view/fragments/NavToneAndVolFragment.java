package com.emo.lkplayer.outerlayer.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.emo.lkplayer.R;
import com.emo.lkplayer.innerlayer.interactors.CurrentSessionInteractor;
import com.emo.lkplayer.innerlayer.interactors.Interactor_VolumeAndStereos;
import com.emo.lkplayer.middlelayer.actioners.ToneVolStereoActions;
import com.emo.lkplayer.outerlayer.customviews.KnobController;
import com.h6ah4i.android.media.audiofx.IBassBoost;

public class NavToneAndVolFragment extends Fragment {

    private ToneVolStereoActions actionHandler = new ToneVolStereoActions() {
        @Override
        public void setLeftRightBalance(float left, float right)
        {
            new Interactor_VolumeAndStereos(getContext()).setLeftRightBalance(left, right);
        }

        @Override
        public void setVolumeOverAll(float volume)
        {
            new Interactor_VolumeAndStereos(getContext()).setVolume(volume);
        }

        @Override
        public void switchMonoStereo(boolean ifStereo)
        {
            new Interactor_VolumeAndStereos(getContext()).setStereoUseState(ifStereo);
        }

        @Override
        public void setStereoX(float value)
        {
            new Interactor_VolumeAndStereos(getContext()).setStereoX(value);
        }

        @Override
        public boolean getStereoState()
        {
            return new Interactor_VolumeAndStereos(getContext()).getStereoUseState();
        }

        @Override
        public float getVolume()
        {
            return new Interactor_VolumeAndStereos(getContext()).getVolume();
        }

        @Override
        public IBassBoost getBassBoost()
        {
            return new CurrentSessionInteractor(getContext()).getBassBoost();
        }
    };

    private KnobController knobControllerVolume;
    private KnobController knobControllerStereoX;
    private KnobController knobControllerMono;
    private Button btn_monoSwtich;


    public NavToneAndVolFragment()
    {
        // Required empty public constructor
    }

    public static NavToneAndVolFragment newInstance(String param1, String param2)
    {
        NavToneAndVolFragment fragment = new NavToneAndVolFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tone_and_vol, container, false);
        knobControllerVolume = (KnobController) rootView.findViewById(R.id.knob_volume);
        knobControllerStereoX = (KnobController) rootView.findViewById(R.id.knob_stereoX);
        knobControllerMono = (KnobController) rootView.findViewById(R.id.knob_mono);
        btn_monoSwtich = (Button) rootView.findViewById(R.id.btn_switchMono);
        btn_monoSwtich.setActivated(!actionHandler.getStereoState());

        btn_monoSwtich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!actionHandler.getStereoState())
                {
                    actionHandler.switchMonoStereo(true);
                    v.setActivated(false);
                } else
                {
                    actionHandler.switchMonoStereo(false);
                    v.setActivated(true);
                }
            }
        });

        knobControllerVolume.setTopRightText("Volume");
        knobControllerMono.setTopRightText("Mono");
        knobControllerMono.setBottomLeftText("L");
        knobControllerMono.setBottomRightText("R");
        knobControllerStereoX.setTopRightText("StereoX");


        knobControllerMono.setKnobControllerEventsListener(new KnobController.KnobControllerEventsListener() {
            @Override
            public void onRotate(int percentage)
            {
                Log.d("knob", "Mono: " + percentage);
                actionHandler.setLeftRightBalance(percentage, percentage);
            }
        });

        knobControllerVolume.setKnobControllerEventsListener(new KnobController.KnobControllerEventsListener() {
            @Override
            public void onRotate(int percentage)
            {
                Log.d("knob", "Volume: " + percentage);
                actionHandler.setVolumeOverAll(percentage);
            }
        });

        knobControllerStereoX.setKnobControllerEventsListener(new KnobController.KnobControllerEventsListener() {
            @Override
            public void onRotate(int percentage)
            {
                actionHandler.setStereoX(percentage);
            }
        });

        //knobControllerVolume.setCurrentLevel(50);
        return rootView;
    }

}
