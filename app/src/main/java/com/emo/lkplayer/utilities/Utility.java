package com.emo.lkplayer.utilities;

import com.emo.lkplayer.innerlayer.model.entities.EQPreset;
import com.emo.lkplayer.innerlayer.model.entities.Playlist;

import java.util.List;

/**
 * Created by shoaibanwar on 6/30/17.
 */

public final class Utility {

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
