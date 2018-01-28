package com.emo.emomediaplayerpro.view_ui.view.fragments;


import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.emo.emomediaplayerpro.R;
import com.emo.emomediaplayerpro.model.domain.entities.EQPreset;
import com.emo.emomediaplayerpro.view_ui.viewmodel.EqualizerViewModel;
import com.emo.emomediaplayerpro.view_ui.customviews.VerticalSliderWrapped;
import com.h6ah4i.android.media.audiofx.IEqualizer;
import com.h6ah4i.android.media.audiofx.IPreAmp;

import static com.emo.emomediaplayerpro.view_ui.view.EqualizerActivity.SHARED_EQVIEWMODEL_KEY;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavEqualizerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavEqualizerFragment extends Fragment implements VerticalSliderWrapped.SliderCallbacks, LifecycleRegistryOwner {

    @Override
    public LifecycleRegistry getLifecycle()
    {
        return registry;
    }

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    public NavEqualizerFragment()
    {
        // Required empty public constructor
    }

    public static NavEqualizerFragment newInstance()
    {
        NavEqualizerFragment fragment = new NavEqualizerFragment();
        return fragment;
    }

    /*----------------- Constants ---------------------- */
    private static final int MAX_NUM_BAND_SEEKBARS = 10;
    private final int[] ARR_ID_VSEEKBARS = new int[]{R.id.slider_eq1, R.id.slider_eq2, R.id.slider_eq3,
            R.id.slider_eq4, R.id.slider_eq5, R.id.slider_eq6, R.id.slider_eq7, R.id.slider_eq8,
            R.id.slider_eq9, R.id.slider_eq10};
    private static final int PREAMP_SLIDER_INDEX = 1176;

    /*----------------- Fields ---------------------- */
    private short numOfEqFrequencyBands;
    private short lowerEqualizerBandLevel;
    private short upperEqualizerBandLevel;
    private VerticalSliderWrapped[] verticalSeekBars = null;
    private VerticalSliderWrapped preAmpSeekbar;


    /*----------------- Model Objects -------------------*/
    private EqualizerViewModel equalizerViewModel = null;
    private IEqualizer equalizer = null;
    private IPreAmp preAmp = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /* Get an instance of the viewModel that's responsible for Equalizer stuff */
        equalizerViewModel = ViewModelProviders.of(this).get(SHARED_EQVIEWMODEL_KEY, EqualizerViewModel.class);
        /* Obtained equalizer instance is enabled, initialized and conntected to media player*/
        equalizer = equalizerViewModel.getEqualizerInstance();
        /* Obtained the total number of bands that equalizer has/supports*/
        numOfEqFrequencyBands = equalizer.getNumberOfBands();
        /* Obtained lower range/level for equalizer bands */
        lowerEqualizerBandLevel = equalizer.getBandLevelRange()[0];
        /* Obtained upper range/level for equalizer bands */
        upperEqualizerBandLevel = equalizer.getBandLevelRange()[1];
        /* Obtained PreAmp Instance*/
        preAmp = equalizerViewModel.getPreAmpInstance();
        /* Enable the preAmp*/
        preAmp.setEnabled(true);

        equalizerViewModel.getLastSavedSelectedPreset().observe(this, new Observer<EQPreset>() {
            @Override
            public void onChanged(@Nullable EQPreset eqPreset)
            {
                try
                {
                    eqPreset.applyToEQ(equalizer);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                updateSlidersAsPerEQBands();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_equalizer, container, false);
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.eqfrag_hsv);
        horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // return true;
                return false;
            }
        });
        verticalSeekBars = new VerticalSliderWrapped[MAX_NUM_BAND_SEEKBARS];
        for (short bandIndex = 0; bandIndex < MAX_NUM_BAND_SEEKBARS && bandIndex < numOfEqFrequencyBands; bandIndex++)
        {
            verticalSeekBars[bandIndex] = (VerticalSliderWrapped) rootView.findViewById(ARR_ID_VSEEKBARS[bandIndex]);
            verticalSeekBars[bandIndex].setIndex(bandIndex);
            verticalSeekBars[bandIndex].setVisibility(View.VISIBLE);
            verticalSeekBars[bandIndex].setSliderBottomText((equalizer.getCenterFreq(bandIndex) / 1000) + " Hz");
            verticalSeekBars[bandIndex].setMinMax(0, upperEqualizerBandLevel - lowerEqualizerBandLevel, true);
            verticalSeekBars[bandIndex].registerToSliderCallbacks(this);
        }
        preAmpSeekbar = (VerticalSliderWrapped) rootView.findViewById(R.id.slider_preAmp);
        preAmpSeekbar.setIndex(PREAMP_SLIDER_INDEX);
        preAmpSeekbar.setSliderBottomText("PreAmp");
        preAmpSeekbar.setMinMax(0, 2, false);
        preAmpSeekbar.registerToSliderCallbacks(this);
        return rootView;
    }

    private void updateSlidersAsPerEQBands()
    {
        for (short bandIndex = 0; bandIndex < MAX_NUM_BAND_SEEKBARS && bandIndex < numOfEqFrequencyBands; bandIndex++)
        {
            short bandVal = equalizer.getBandLevel(bandIndex);
            verticalSeekBars[bandIndex].setPosViaVal(bandVal);
            //verticalSeekBars[bandIndex].invalidate();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }


    /* Method called by vertical SeekBars/Bands of equalizers when they are interacted */
    @Override
    public void onValueChanged(int sliderIndex, float updatedValue)
    {
        Log.d("==", "slider_index= " + sliderIndex + " value= " + updatedValue);

        if (sliderIndex == PREAMP_SLIDER_INDEX)
        {
            /*Following if is a fix for max value greater than 2*/
            if (updatedValue > 2)
                updatedValue = 2;
            preAmp.setLevel(updatedValue);
            return;
        }
        /* These conditionals are temporary fixes upon the updatedValue received here*/
        if (updatedValue > upperEqualizerBandLevel)
            updatedValue = upperEqualizerBandLevel;
        else if (updatedValue < lowerEqualizerBandLevel)
            updatedValue = lowerEqualizerBandLevel;

        equalizer.setBandLevel((short) sliderIndex, (short) (updatedValue));
    }

    @Override
    public void MeasureTakesPlace(int index)
    {
        Log.d("slider", " index= " + index);
        if (index >= 10)
            return;
        /* update slider positions according to their current band values */
        short bandVal = equalizer.getBandLevel((short) index);
        verticalSeekBars[index].setPosViaVal(bandVal);
    }

}
