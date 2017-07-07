package com.emo.lkplayer.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.emo.lkplayer.R;
import com.emo.lkplayer.model.content_providers.Specification.AlbumsSpecification;
import com.emo.lkplayer.model.content_providers.Specification.ArtistSpecification;
import com.emo.lkplayer.model.content_providers.Specification.GenreSpecification;
import com.emo.lkplayer.model.content_providers.Specification.LibraryLeadSelectionEventsListener;
import com.emo.lkplayer.model.content_providers.Specification.PlaylistSpecification;
import com.emo.lkplayer.model.content_providers.Specification.iLoaderSpecification;
import com.emo.lkplayer.view.fragments.ListAlbumsFragment;
import com.emo.lkplayer.view.fragments.ListArtistFragment;
import com.emo.lkplayer.view.fragments.ListGenreFragment;
import com.emo.lkplayer.view.fragments.ListPlaylistFragment;
import com.emo.lkplayer.view.fragments.ListTrackFragment;
import com.emo.lkplayer.view.fragments.NavPlayBackFragment;
import com.emo.lkplayer.view.navigation.BaseNavigationManager;
import com.emo.lkplayer.view.navigation.NavigationManagerContentFlow;

public class NagizarActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationManagerContentFlow.NavigationListener, LibraryLeadSelectionEventsListener, NavPlayBackFragment.NavPlayBackFragmentInteractionListener{

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE=1011398;
    private NavigationManagerContentFlow mNavigationManager;

    private BottomNavigationView bottomNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nagizar);
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique
            }
        }

        bottomNavBar = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavBar.getMenu().findItem(R.id.navMenu_Nagizar_Folders).setChecked(true);
        bottomNavBar.setOnNavigationItemSelectedListener(this);

        mNavigationManager = new NavigationManagerContentFlow();
        mNavigationManager.init(getSupportFragmentManager(), R.id.fragmentHolder_NagizarActivity);
        mNavigationManager.startPlayBackFragment(-1, null);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mNavigationManager = new NavigationManagerContentFlow();
                    mNavigationManager.init(getSupportFragmentManager(), R.id.fragmentHolder_NagizarActivity);
                    mNavigationManager.startPlayBackFragment(-1, null);

                }
                else
                {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        mNavigationManager.navigateBack(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.navigation_GoToPlayback:
                //finish();
//                if (!mNavigationManager.bringExistingFragment(NavPlayBackFragment.class.toString(), false)) {
//                    mNavigationManager.open(NavPlayBackFragment.newInstance(), true, true);
//                }
                mNavigationManager.startPlayBackFragment(-1, null);
                return true;
            case R.id.navMenu_Nagizar_Library:
//                if (!mNavigationManager.bringExistingFragment(NavLibraryFragment.class.toString(), false)) {
//                    mNavigationManager.open(NavLibraryFragment.newInstance(null, null), true, true);
//                }
                mNavigationManager.startLibraryFragment();
                return true;
            case R.id.navMenu_Nagizar_Folders:
//                if (!mNavigationManager.bringExistingFragment(NavFolderFragment.class.toString(), false)) {
//                    mNavigationManager.open(NavFolderFragment.newInstance(null, null), true, true);
//                }
                mNavigationManager.startFolderFragment(false);
                return true;
        }
        return false;
    }

    @Override
    public void onBackstackChanged()
    {

    }

    @Override
    public void onSelectionWithSpecificationProvision(iLoaderSpecification specification, boolean specNeed)
    {
        if (specification instanceof AlbumsSpecification)
        {
            mNavigationManager.open(ListAlbumsFragment.newInstance(), false, true);
            return;
        }
        if (specification instanceof ArtistSpecification)
        {
            mNavigationManager.open(ListArtistFragment.newInstance(), false, true);
            return;
        }
        if (specification instanceof GenreSpecification)
        {
            mNavigationManager.open(ListGenreFragment.newInstance(), false, true);
            return;
        }
        if (specification instanceof PlaylistSpecification)
        {
            mNavigationManager.open(ListPlaylistFragment.newInstance(), false, true);
            return;
        }
        mNavigationManager.open(ListTrackFragment.newInstance(specification), false, true);

    }

    @Override
    public void showToolbar()
    {
        getSupportActionBar().setTitle("");
        getSupportActionBar().show();
    }

    @Override
    public void hideToolbar()
    {
        getSupportActionBar().hide();
    }

    @Override
    public void showBottomNavBar()
    {
        bottomNavBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomNavBar()
    {
        bottomNavBar.setVisibility(View.GONE);
    }

    @Override
    public BaseNavigationManager getNavigationManager()
    {
        return mNavigationManager;
    }
}
