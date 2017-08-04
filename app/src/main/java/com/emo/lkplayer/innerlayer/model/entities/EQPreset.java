package com.emo.lkplayer.innerlayer.model.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.emo.lkplayer.outerlayer.storage.daos.DBConstants;
import com.emo.lkplayer.outerlayer.storage.daos.TypeConverter_IntegerArray;
import com.h6ah4i.android.media.audiofx.IEqualizer;

/**
 * Created by shoaibanwar on 7/26/17.
 */

public abstract class EQPreset {

    public static final int PRESET_ARR_SIZE = 10;
    @PrimaryKey
    protected String presetName;

    public EQPreset(String presetName)
    {
        this.presetName = presetName;
    }

    /* ------------- User defined preset -----------------------*/
    @Entity (tableName = DBConstants.TABLE_USERDEFINEDEQPRESET_NAME)
    public static class UserDefPreset extends EQPreset {
        @TypeConverters(TypeConverter_IntegerArray.class)
        private int[] arr;

        public UserDefPreset(String presetName,int[] arr)
        {
            super(presetName);
            this.arr = arr.clone();
        }

        public int[] getArr()
        {
            return arr;
        }

        @Override
        public void applyToEQ(IEqualizer equalizer) throws Exception
        {
            int numBands = equalizer.getNumberOfBands();
            if (numBands>10)
                throw new Exception("Above 10 bands not supported");
            for (short i = 0; i < numBands ; i++)
            {
                equalizer.setBandLevel(i,(short)arr[i]);
            }
        }
    }

    /* ------------- An extension to support default preset by Android framework --------*/
    public static class EQDefaultPreset extends EQPreset {
        private short index;

        public EQDefaultPreset(String presetName,short index)
        {
            super(presetName);
            this.index = index;
        }

        @Override
        public void applyToEQ(IEqualizer equalizer)
        {
            equalizer.usePreset(index);
        }
    }

    public String getPresetName()
    {
        return presetName;
    }

    public abstract void applyToEQ(IEqualizer equalizer) throws Exception;
}
