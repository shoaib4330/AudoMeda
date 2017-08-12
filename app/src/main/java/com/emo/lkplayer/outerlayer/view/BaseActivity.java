package com.emo.lkplayer.outerlayer.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.emo.lkplayer.utilities.Utility;

public abstract class BaseActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    @Override
    public LifecycleRegistry getLifecycle()
    {
        return registry;
    }

    private LifecycleRegistry registry = new LifecycleRegistry(this);

    private int cTheme = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        cTheme = Utility.onActivityCreateSetTheme(this);
        setContentView(getLayoutResourceId());
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        if (cTheme!= Utility.appThemeCurrent())
            Utility.changeToTheme(this,Utility.appThemeCurrent());
    }

    protected abstract int getLayoutResourceId();

}
