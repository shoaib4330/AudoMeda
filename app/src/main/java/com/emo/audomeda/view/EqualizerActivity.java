package com.emo.audomeda.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.emo.audomeda.R;
import com.emo.audomeda.view.navigation.NavigationManagerContentFlow;
import com.emo.audomeda.view.navigation.NavigationManagerSettingsFlow;

public class EqualizerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationManagerContentFlow.NavigationListener {

    private NavigationManagerSettingsFlow mNavigationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navView_EqualizerAcitiy);
        navigation.getMenu().findItem(R.id.navMenu_toEqualizerFrag_EqualizerActivity).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(this);

        mNavigationManager = new NavigationManagerSettingsFlow();
        mNavigationManager.init(getSupportFragmentManager());
        mNavigationManager.startUpInitialFragment_Equalizer();
    }

    @Override
    public void onBackPressed() {
        mNavigationManager.navigateBack(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navMenu_toEqualizerFrag_EqualizerActivity:
                mNavigationManager.startEqualizerFragment();
                return true;
            case R.id.navMenu_toToneAndVolFrag_EqualizerActivity:
                mNavigationManager.startToneandVolFragment();
                return true;
            case R.id.navMenu_toPlaybackActivity_EqualizerActivity:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onBackstackChanged() {

    }
}
