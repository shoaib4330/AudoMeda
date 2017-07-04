package com.emo.lkplayer;

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
}
