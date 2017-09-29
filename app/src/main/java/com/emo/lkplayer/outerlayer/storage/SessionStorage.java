package com.emo.lkplayer.outerlayer.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.BaseLoaderSpecification;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class SessionStorage {

    /* ------------------------- Constants --------------------------- */
    private static final String IS_TONE_USED_STORED = "emo.lkplayer.istoneused";
    private static final String IS_LIMIT_USED_STORED = "emo.lkplayer.islimitused";
    private static final String TRACK_INDEX_PREF_NAME = "emo.lkplayer.trackindexpref";
    private static final String IS_EQUALIZER_USED_STORED = "emo.lkplayer.iseqused";
    private static final String SINGLE_SPEC_FILE_STORED_NAME = "singlespecfilebylkplayer";
    private static final String CURRENT_USED_PRESET_NAME_STORED = "emo.lkplayer.currentpresetused";
    private static final String CURRENTTRACK_INDEX_INTEGER_NAME = "emo.lkplayer.ctrackinteger";
    //private static final String LAST_SET__LEFT_FLOAT = "emo.lkplayer.lastvolleft";
    //private static final String LAST_SET_VOLUME_Right_FLOAT = "emo.lkplayer.lastvolRight";
    //private static final String LAST_SET_VOLUME_OVERALL = "emo.lkplayer.lastvolOverAll";
    private static final String LAST_SET_STEREO_STATE = "emo.lkplayer.stereoState";

    private Context context;
    private FileOutputStream fos;
    private FileInputStream fis;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private SharedPreferences sharedPreferences;

    public SessionStorage(Context context)
    {
        this.context = context.getApplicationContext();
        sharedPreferences = this.context.getSharedPreferences(TRACK_INDEX_PREF_NAME, Context.MODE_PRIVATE);
    }

    //@formatter:off
                    /*-- Storage methods used for Storage-Reading of audio data --*/
    //@formatter:on
    public void storeCurrentTrackIndex(int newIndex)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CURRENTTRACK_INDEX_INTEGER_NAME, newIndex);
        editor.commit();
    }

    public int getCurrentTrackIndex()
    {
        int index = sharedPreferences.getInt(CURRENTTRACK_INDEX_INTEGER_NAME, 0);
        return index;
    }

    public BaseLoaderSpecification getSpecification()
    {
        return (BaseLoaderSpecification) readObject(SINGLE_SPEC_FILE_STORED_NAME);
    }

    public void writeSpecification(BaseLoaderSpecification specification)
    {
        if (specification != null)
            writeObject(SINGLE_SPEC_FILE_STORED_NAME, specification);
    }

    //@formatter:off
                    /*------ Methods for Storage & Reading of Equalizer data ----*/
    //@formatter:on
    public void saveEqaulizerUseState(boolean isBeingUsed)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_EQUALIZER_USED_STORED, isBeingUsed);
        editor.commit();
    }

    public boolean getEqaulizerUseState()
    {
        return sharedPreferences.getBoolean(IS_EQUALIZER_USED_STORED, true);
    }

    //@formatter:off
                    /*---------------- Preset methods here --------------------*/
    //@formatter:on
    public void saveCurrentUsedPreset(String presetName)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CURRENT_USED_PRESET_NAME_STORED, presetName);
        editor.commit();
    }

    public String getCurrentUsedPresetName()
    {
        return sharedPreferences.getString(CURRENT_USED_PRESET_NAME_STORED, null);
    }

    //@formatter:off
                    /*---------------- Limit methods here --------------------*/
    //@formatter:on
    public void saveLimitUseState(boolean isBeingUsed)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LIMIT_USED_STORED, isBeingUsed);
        editor.commit();
    }

    public boolean getLimitUseState()
    {
        return sharedPreferences.getBoolean(IS_LIMIT_USED_STORED, true);
    }

    //@formatter:off
                    /*----------------- Tone and Vol methods ----------------------*/
    //@formatter:on
    public void saveLeftRightBalance(float left, float right)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //editor.putFloat(LAST_SET_VOLUME_LEFT_FLOAT, left);
        //editor.putFloat(LAST_SET_VOLUME_Right_FLOAT, right);
        editor.commit();
    }

//    public void saveVolumeOverAll(float volume)
//    {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putFloat(LAST_SET_VOLUME_OVERALL,volume);
//        editor.commit();
//    }

    public float getBalanceLeft()
    {
        //return sharedPreferences.getFloat(LAST_SET_VOLUME_LEFT_FLOAT, 50.0f);
        return 0.0f;
    }

    public float getBalanceRight()
    {
        //return sharedPreferences.getFloat(LAST_SET_VOLUME_Right_FLOAT, 50.0f);
        return 0.0f;
    }

//    public float getVolumeOverAll()
//    {
//        return sharedPreferences.getFloat(LAST_SET_VOLUME_OVERALL, 50.0f);
//    }

    public void saveToneUseState(boolean isBeingUsed)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_TONE_USED_STORED, isBeingUsed);
        editor.commit();
    }

    public boolean getToneUseState()
    {
        return sharedPreferences.getBoolean(IS_TONE_USED_STORED, true);
    }

    public void saveStereoUseState(boolean stereo)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LAST_SET_STEREO_STATE, stereo);
        editor.commit();
    }

    public boolean getSetereoUseState()
    {
        return sharedPreferences.getBoolean(LAST_SET_STEREO_STATE,false);
    }

    //formatter:off
                    /*----------- Private method of class -----------------------*/
    //formatter:on
    private synchronized Object readObject(String theFileName)
    {
        Object object = null;

        try
        {

            fis = context.openFileInput(theFileName);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Log.e("fis", "fis Exception Caught");

            return null;
        }

        try
        {
            ois = new ObjectInputStream(fis);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            object = ois.readObject();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            fis.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            ois.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return object;
    }

    private synchronized void writeObject(String theFileName, Object object)
    {

        try
        {
            fos = context.openFileOutput(theFileName, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        try
        {
            oos = new ObjectOutputStream(fos);
        } catch (IOException e)
        {
            e.printStackTrace();
            Log.e("objectOutputStream", "objectOutput Exception caught");
        }

        try
        {
            oos.writeObject(object);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            oos.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
