package com.emo.lkplayer.outerlayer.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.emo.lkplayer.R;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.AudioAlbumsSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.ArtistSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.GenreSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.LibraryLeadSelectionEventsListener;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.PlaylistSpecification;
import com.emo.lkplayer.outerlayer.storage.content_providers.Specification.iLoaderSpecification;
import com.emo.lkplayer.outerlayer.view.fragments.ListAlbumsFragment;
import com.emo.lkplayer.outerlayer.view.fragments.ListArtistFragment;
import com.emo.lkplayer.outerlayer.view.fragments.ListGenreFragment;
import com.emo.lkplayer.outerlayer.view.fragments.ListPlaylistFragment;
import com.emo.lkplayer.outerlayer.view.fragments.NavFolderFragment;
import com.emo.lkplayer.outerlayer.view.fragments.NavLibraryFragment;
import com.emo.lkplayer.outerlayer.view.fragments.NavPlayBackFragment;
import com.emo.lkplayer.outerlayer.view.navigation.BaseNavigationManager;
import com.emo.lkplayer.outerlayer.view.navigation.NavigationManagerContentFlow;

public class NagizarActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationManagerContentFlow.NavigationListener, LibraryLeadSelectionEventsListener, FragmentInteractionListener.FragmentAndToolbarInteractionListener,
        NavFolderFragment.InteractionListener, NavLibraryFragment.InteractionListener, ListAlbumsFragment.InteractionListener,
        ListGenreFragment.InteractionListener, ListArtistFragment.InteractionListener, ListPlaylistFragment.InteractionListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1011398;
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
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {

                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique
            }
        }

        bottomNavBar = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavBar.getMenu().findItem(R.id.navMenu_Nagizar_Folders).setChecked(true);
        bottomNavBar.setOnNavigationItemSelectedListener(this);

        mNavigationManager = new NavigationManagerContentFlow();
        mNavigationManager.init(getSupportFragmentManager(), R.id.fragmentHolder_NagizarActivity);
        mNavigationManager.startPlayBackFragment();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mNavigationManager = new NavigationManagerContentFlow();
                    mNavigationManager.init(getSupportFragmentManager(), R.id.fragmentHolder_NagizarActivity);
                    mNavigationManager.startPlayBackFragment();
                } else
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
                mNavigationManager.startPlayBackFragment();
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
    public void onBackstackChanged()
    {

    }

    @Override
    public void onSelectionWithSpecificationProvision(iLoaderSpecification specification, boolean specNeed)
    {
        if (specification instanceof AudioAlbumsSpecification)
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
