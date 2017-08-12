package com.emo.lkplayer.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.emo.lkplayer.R;
import com.emo.lkplayer.innerlayer.model.entities.EQPreset;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;

import java.util.List;

/**
 * Created by shoaibanwar on 6/30/17.
 */

public final class Utility {

    private static final String SHARED_PREFS_THEME = "com.emo.lkplayer.shthemeprefs";

    private static int sTheme = 0;
    public final static int THEME_DEFAULT_DARK = 0;
    public final static int THEME_2ND_LIGHT = 1;

    public static final String[] themeArr = new String[]{"Dark Theme","Light Scana Theme"};

    public static void readThemeHistory(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_THEME,Context.MODE_PRIVATE);
        sTheme = sharedPreferences.getInt("themenum",0);
    }

    public static void setThemeHistory(Context context,int themeNum)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_THEME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("themenum",themeNum);
        editor.commit();
    }

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /** Set the theme of the activity, according to the configuration. */
    public static int onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case THEME_DEFAULT_DARK:
                activity.setTheme(R.style.AppTheme);
                break;
            case THEME_2ND_LIGHT:
                activity.setTheme(R.style.AppThemeLight);
                break;
        }
        return sTheme;
    }

    public static int appThemeCurrent()
    {
        return sTheme;
    }

    public static String millisToTrackTimeFormat(long timeMillis){
        String minutesString = "00";
        String secondsString = "00";
        int timeInSeconds = (int)timeMillis/1000;
        int minutes = timeInSeconds/60;
        int seconds = timeInSeconds%60;
        if (minutes<10){
            minutesString = "0"+minutes;
        }
        else{
            minutesString = String.valueOf(minutes);
        }
        if (seconds<10){
            secondsString = "0"+seconds;
        }
        else{
            secondsString = String.valueOf(seconds);
        }
        return minutesString+":"+secondsString;
    }

    public static String[] EQListToStringArray(List<EQPreset> list)
    {
        String[] strArr = new String[list.size()];
        for (int i = 0; i < list.size() ; i++)
        {
            strArr[i] = list.get(i).getPresetName();
        }
        return strArr;
    }

    public static String[] PlaylistListToStringArray(List<Playlist.UserDefinedPlaylist> list)
    {
        String[] strArr = new String[list.size()];
        for (int i = 0; i < list.size() ; i++)
        {
            strArr[i] = list.get(i).getPlaylistName();
        }
        return strArr;
    }

    public static String LongArrListToINQueryString(List<Long> idList)
    {
        String qString = "(";
        for (int i = 0; i < idList.size(); i++)
        {
            String idString = String.valueOf(idList.get(i));
            idString = idString.replace(".0", "");
            if (i + 1 == idList.size())
            {
                idString = "'" + idString + "'";
            } else
            {
                idString = "'" + idString + "',";
            }
            qString = qString + idString;
        }
        return qString = qString + ")";
    }
}
