package com.emo.emomediaplayerpro.model.data_layer.repository;

import android.content.Context;

import com.emo.emomediaplayerpro.model.domain.entities.EQPreset;
import com.emo.emomediaplayerpro.model.domain.entities.EQPreset.*;
import com.emo.emomediaplayerpro.model.data_layer.AppDatabase;
import com.h6ah4i.android.media.audiofx.IEqualizer;


import java.util.ArrayList;
import java.util.List;

public class PresetRepository {
    /* This repo will use PresetDao to get presets in db plust default presets of Equalizer
    using EQ instance.
     */
    private Context context;

    public PresetRepository (Context context)
    {
        this.context = context.getApplicationContext();
    }

    public void add(EQPreset.UserDefPreset preset)
    {
        AppDatabase database = AppDatabase.getDatabase(context);
        database.presetModelDao().addNewPreset(preset);
    }

    public List<EQPreset.UserDefPreset> Query ()
    {
        /* This Query method takes no specification as parameter
        since we always retrieve all presets from db
         */
        return AppDatabase.getDatabase(context).presetModelDao().getUserDefPresets();
    }

    public List<EQPreset> Query (IEqualizer equalizer)
    {
        /* This method returns default presets of the Equalizer
         */
        int numberOfPresets = equalizer.getNumberOfPresets();
        List<EQPreset> eqPresetList = new ArrayList<>();
        for (int i = 0; i < numberOfPresets ; i++)
        {
            eqPresetList.add(new EQDefaultPreset(equalizer.getPresetName((short)i),(short)i));
        }
        return eqPresetList;
    }
}
