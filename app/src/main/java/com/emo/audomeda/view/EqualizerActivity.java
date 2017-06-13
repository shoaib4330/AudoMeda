package com.emo.audomeda.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.emo.audomeda.R;
import com.emo.audomeda.customviews.KnobController;
import com.emo.audomeda.view.navigation.NavigationManagerContentFlow;
import com.emo.audomeda.view.navigation.NavigationManagerSettingsFlow;

public class EqualizerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationManagerContentFlow.NavigationListener, View.OnClickListener {

    private NavigationManagerSettingsFlow mNavigationManager;

    private KnobController knob_bass;
    private KnobController knob_treble;

    private Button btn_equ;
    private Button btn_tone;
    private Button btn_limit;
    private Button btn_preset;
    private Button btn_save;
    private Button btn_reset;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navView_EqualizerAcitiy);
        navigation.getMenu().findItem(R.id.navMenu_toEqualizerFrag_EqualizerActivity).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(this);

        knob_bass       = (KnobController) findViewById(R.id.knob_bass);
        knob_treble     = (KnobController) findViewById(R.id.knob_treble);

        btn_equ         = (Button) findViewById(R.id.btn_equ);
        btn_tone        = (Button) findViewById(R.id.btn_tone);
        btn_limit       = (Button) findViewById(R.id.btn_limit);
        btn_preset      = (Button) findViewById(R.id.btn_preset);
        btn_save         = (Button) findViewById(R.id.btn_save);
        btn_reset         = (Button) findViewById(R.id.btn_reset);

        btn_equ.setOnClickListener(this);
        btn_limit.setOnClickListener(this);
        btn_tone.setOnClickListener(this);
        btn_preset.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_reset.setOnClickListener(this);

        knob_bass.setBottomLeftText("Bass");
        knob_treble.setBottomLeftText("Treble");

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

    @Override
    public void onClick(View v) {
        if (v.isActivated()){
            v.setActivated(false);
        }
        else{
            v.setActivated(true);
        }
    }
}
