package com.emo.lkplayer.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.emo.lkplayer.R;
import com.emo.lkplayer.view.navigation.NavigationManagerContentFlow;

public class NagizarActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,NavigationManagerContentFlow.NavigationListener {

    private NavigationManagerContentFlow mNavigationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nagizar);
        getSupportActionBar().setTitle("");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.getMenu().findItem(R.id.navMenu_Nagizar_Folders).setChecked(true);
        navigation.setOnNavigationItemSelectedListener(this);

        mNavigationManager = new NavigationManagerContentFlow();
        mNavigationManager.init(getSupportFragmentManager());
        mNavigationManager.startUpInitialFragment_Nagizar();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mNavigationManager.navigateBack(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_GoToPlayback:
                finish();
                return true;
            case R.id.navMenu_Nagizar_Library:
                mNavigationManager.startLibraryFragment();
                return true;
            case R.id.navMenu_Nagizar_Folders:
                mNavigationManager.startFolderFragment();
                return true;
        }
        return false;
    }

    @Override
    public void onBackstackChanged() {

    }
}
