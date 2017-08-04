package com.emo.lkplayer.outerlayer.view.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.outerlayer.view.fragments.ListTrackFragment;
import com.emo.lkplayer.outerlayer.view.fragments.NavFolderFragment;
import com.emo.lkplayer.outerlayer.view.fragments.NavLibraryFragment;
import com.emo.lkplayer.outerlayer.view.fragments.NavPlayBackFragment;

import java.util.List;

/**
 * Created by shoaibanwar on 6/9/17.
 */

public final class NavigationManagerContentFlow extends BaseNavigationManager {

    //private List<Fragment> existingFragmentsList;

    //private NavPlayBackFragment playBackFragment =null;
    private static final String BACKSTACK_STATE_PLAYBACK_FRAGMENT_ONTOP = "only_navplaybackfrag_added_yet";
    private static final String BACKSTACK_STATE_FOLDER_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT = "folder_fragment_on_top_of_navplaybackfrag";
    private static final String BACKSTACK_STATE_LIBRARY_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT = "library_fragment_on_top_of_navplaybackfrag";

    private NavFolderFragment folderFragment = null;
    private NavLibraryFragment libraryFragment = null;

    /* -----------------Shoaib: Constructors here----------------------- */
    public NavigationManagerContentFlow()
    {
    }

    /* -----------------Shoaib: Methods here -----------------------------*/
    private void addToFragmentInstancesList(Fragment fragment)
    {
    }

    private void addTransactionToBackStack(FragmentTransaction transaction, Fragment fragment)
    {
        transaction.addToBackStack(fragment.toString());
    }

    public void open(Fragment fragment, boolean beAddedToFragmentReferenceList,
                     boolean beAddedToBackStack)
    {

        if (mFragmentManager == null)
            return;

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(this.Fragment_Container_ID, fragment);
        if (beAddedToBackStack)
            addTransactionToBackStack(transaction, fragment);
        transaction.commit();
        if (beAddedToFragmentReferenceList)
            addToFragmentInstancesList(fragment);
    }

    public void startPlayBackFragment(int position, List<AudioTrack> list)
    {
        Fragment fragment = NavPlayBackFragment.newInstance();
        openAsRoot(fragment, BACKSTACK_STATE_PLAYBACK_FRAGMENT_ONTOP); /* Opened as root and added to backstack */
    }

    public void startFolderFragment(boolean addToBackStack)
    {
        if (folderFragment == null)
            folderFragment = NavFolderFragment.newInstance(null, null);
        /* if top fragment on backstack is the NavPlayBackFragment Instance */
        int index = mFragmentManager.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backStackEntry = mFragmentManager.getBackStackEntryAt(index);
        String entryTag = backStackEntry.getName();
        if (entryTag.equals(BACKSTACK_STATE_PLAYBACK_FRAGMENT_ONTOP))
        {
            open(folderFragment, BACKSTACK_STATE_FOLDER_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT);
        } else if (entryTag.equals(BACKSTACK_STATE_LIBRARY_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT))
        {
            mFragmentManager.popBackStack(BACKSTACK_STATE_LIBRARY_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            open(folderFragment, BACKSTACK_STATE_FOLDER_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT);
        } else
        {
            mFragmentManager.popBackStack(BACKSTACK_STATE_FOLDER_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT, 0);
        }
    }

    public void startLibraryFragment()
    {
        if (libraryFragment == null)
            libraryFragment = NavLibraryFragment.newInstance(null, null);
        /* if top fragment on backstack is the NavPlayBackFragment Instance */
        int index = mFragmentManager.getBackStackEntryCount() - 1;
        FragmentManager.BackStackEntry backStackEntry = mFragmentManager.getBackStackEntryAt(index);
        String entryTag = backStackEntry.getName();
        if (entryTag.equals(BACKSTACK_STATE_PLAYBACK_FRAGMENT_ONTOP))
        {
            open(libraryFragment, BACKSTACK_STATE_LIBRARY_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT);
        } else if (entryTag.equals(BACKSTACK_STATE_FOLDER_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT))
        {
            mFragmentManager.popBackStack(BACKSTACK_STATE_FOLDER_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            open(libraryFragment, BACKSTACK_STATE_LIBRARY_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT);
        } else
        {
            if (index - 1 < 0)
                return;
            FragmentManager.BackStackEntry backStackEntry1 = mFragmentManager.getBackStackEntryAt(index - 1);
            String entryTag1 = backStackEntry1.getName();
            if (entryTag1.equals(BACKSTACK_STATE_FOLDER_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT))
            {
                mFragmentManager.popBackStack(BACKSTACK_STATE_FOLDER_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                open(libraryFragment, BACKSTACK_STATE_LIBRARY_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT);
                return;
            }
            /* there is no instance of library fragment on stack already, */
            mFragmentManager.popBackStack(BACKSTACK_STATE_LIBRARY_FRAGMENT_ONTOP_ABOVE_PLAYBACK_FRAGMENT, 0);
        }
    }

    public void startListTracksFragment(String viaFolderQuery, String viaAlbumQuery, String viaArtistQuery, String playlistName)
    {
        Fragment listTrackFragment = ListTrackFragment.newInstance(viaFolderQuery, viaAlbumQuery, viaArtistQuery, playlistName, -1);
        open(listTrackFragment, false, true);
    }

    public void startListTracksFragment(String viaFolderQuery, String viaAlbumQuery, String viaArtistQuery, String playlistName, long genreID)
    {
        Fragment listTrackFragment = ListTrackFragment.newInstance(viaFolderQuery, viaAlbumQuery, viaArtistQuery, playlistName, genreID);
        open(listTrackFragment, false, true);
    }

    public void startListTracksFragment_WithAllTracks()
    {
        Fragment listTrackFragment = ListTrackFragment.newInstance(ListTrackFragment.ListingMode.MODE_ALL,null);
        open(listTrackFragment, false, true);
    }

    public void startListTracksFragment_WithAllRecentTracks()
    {
        Fragment listTrackFragment = ListTrackFragment.newInstance(ListTrackFragment.ListingMode.MODE_ALLRECENT,null);
        open(listTrackFragment, false, true);
    }

    public void startListTracksFragment_WithDQ()
    {
        Fragment listTrackFragment = ListTrackFragment.newInstance(ListTrackFragment.ListingMode.MODE_DQ,null);
        open(listTrackFragment, false, true);
    }

    @Override
    protected void openAsRoot(Fragment fragment)
    {
        popEveryFragment();
        open(fragment, false, false);
    }

    private void openAsRoot(Fragment fragment, String backStackEntryTag)
    {
        popEveryFragment();
        open(fragment, backStackEntryTag);
    }

    private void open(Fragment fragment, String backStackEntryTag)
    {
        if (mFragmentManager != null)
        {
            //@formatter:off
            mFragmentManager.beginTransaction()
                    .replace(this.Fragment_Container_ID, fragment, fragment.getClass().getName())
                    .addToBackStack(backStackEntryTag)
                    .commit();
            //@formatter:on
        }
    }
}
