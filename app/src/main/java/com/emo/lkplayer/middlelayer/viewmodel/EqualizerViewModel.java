package com.emo.lkplayer.middlelayer.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.emo.lkplayer.innerlayer.CurrentSessionInteractor;
import com.emo.lkplayer.innerlayer.model.entities.EQPreset;
import com.h6ah4i.android.media.audiofx.IEqualizer;
import com.h6ah4i.android.media.audiofx.IPreAmp;

import java.util.List;

public class EqualizerViewModel extends AndroidViewModel {

    private IEqualizer equalizer = null;
    private IPreAmp preAmp = null;
    private static MutableLiveData<EQPreset> presetLiveData= new MutableLiveData<>();

    public EqualizerViewModel(Application application)
    {
        super(application);
    }

    public IEqualizer getEqualizerInstance()
    {
        if (equalizer == null)
            equalizer = new CurrentSessionInteractor(this.getApplication().getApplicationContext()).getEqualizer();
        return equalizer;
    }

    public void setEQUseState(boolean toUse)
    {
        if (equalizer!=null)
            equalizer.setEnabled(toUse);
        new CurrentSessionInteractor(this.getApplication()).setEqLastUseState(toUse);
    }

    public boolean getEQUseState()
    {
        return new CurrentSessionInteractor(this.getApplication()).getEqLastUseState();
    }

    public IPreAmp getPreAmpInstance()
    {
        if (preAmp == null)
            preAmp = new CurrentSessionInteractor(this.getApplication().getApplicationContext()).getPreAmp();
        return preAmp;
    }

    public List<EQPreset> getAllEqPresets()
    {
        return new CurrentSessionInteractor(this.getApplication().getApplicationContext()).getAllEqPresets();
    }

    public EQPreset getEQResetPreset()
    {
        List<EQPreset> list = new CurrentSessionInteractor(this.getApplication().getApplicationContext()).getAllEqPresets();
        for (int i = 0; i < list.size() ; i++)
        {
            if ("Flat".equals(list.get(i).getPresetName()))
                return list.get(i);
        }
        return null;
    }

    public void setEqPreset(EQPreset eqPreset)
    {
        this.presetLiveData.setValue(eqPreset);
        new CurrentSessionInteractor(this.getApplication().getApplicationContext()).saveNewSelectedEqPreset(eqPreset.getPresetName());
    }

    public LiveData<EQPreset> getLastSavedSelectedPreset()
    {
        return presetLiveData;
    }

    public void addNewPreset(String presetName,int[] bandLevelArr)
    {
        new CurrentSessionInteractor(this.getApplication()).addNewPreset(presetName,bandLevelArr);
    }


}
