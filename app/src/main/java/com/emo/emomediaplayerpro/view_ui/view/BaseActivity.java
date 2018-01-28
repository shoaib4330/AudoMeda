package com.emo.emomediaplayerpro.view_ui.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.emo.emomediaplayerpro.view_ui.view.navigation.BaseNavigationManager;
import com.emo.emomediaplayerpro.utilities.Utility;

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

    /* Custom Added methods */
    protected abstract int getLayoutResourceId();

    public abstract BaseNavigationManager getNavigationManager();

}
