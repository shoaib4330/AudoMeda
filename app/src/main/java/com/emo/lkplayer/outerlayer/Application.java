package com.emo.lkplayer.outerlayer;

import android.content.Intent;

import com.emo.lkplayer.outerlayer.androidservices.MediaControllerService;
import com.emo.lkplayer.outerlayer.androidservices.MediaControllerService.Constants.ServiceClientIntentExtras;
/**
 * Created by shoaibanwar on 7/23/17.
 */

public class Application extends android.app.Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        /* Init all the neccessary components for the app via using following class*/
        AppMediaManager.init(getApplicationContext());
    }

}
