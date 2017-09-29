package com.emo.lkplayer.outerlayer.view.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.emo.lkplayer.innerlayer.model.entities.AudioTrack;
import com.emo.lkplayer.innerlayer.model.entities.Folder;
import com.emo.lkplayer.outerlayer.view.fragments.ListAlbumsFragment;
import com.emo.lkplayer.outerlayer.view.fragments.ListPlaylistFragment;
import com.emo.lkplayer.outerlayer.view.fragments.ListTrackFragment;
import com.emo.lkplayer.outerlayer.view.fragments.NavFolderFragment;
import com.emo.lkplayer.outerlayer.view.fragments.NavLibraryFragment;
import com.emo.lkplayer.outerlayer.view.fragments.NavPlayBackFragment;
import com.emo.lkplayer.outerlayer.view.fragments.VideoPlaybackFragment;
import com.emo.lkplayer.outerlayer.view.fragments.VideoTracksListFragment;

import java.util.ArrayList;
import java.util.List;

public final class NavigationManagerContentFlow extends BaseNavigationManager {

    /*--------------------- Methods here to open App's Fragments --------------------------------*/
    public void startPlayBackFragment()
    {
        Fragment fragment = NavPlayBackFragment.newInstance();
        openAsRoot(fragment, BACKSTACK_STATE_PLAYBACK_FRAGMENT_ONTOP); /* Opened as root and added to backstack */
    }

    public void VideoPlaybackFragment_Start(List<AudioTrack> trackList,int selectedTrackPos)
    {
        Fragment fragment = VideoPlaybackFragment.newInstance((ArrayList) trackList,selectedTrackPos);
        open(fragment,false,true);
    }

    public void PopVideoPlaybackFragment()
    {
        mFragmentManager.popBackStackImmediate();
    }

    public void VideoTrackListFragment_Start()
    {
        Fragment fragment = VideoTracksListFragment.newInstance();
        open(fragment,false,true);
    }

    public void startFolderFragment()
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

    public void startAlbumsFragment()
    {
        Fragment ListAlbumsFragment = com.emo.lkplayer.outerlayer.view.fragments.ListAlbumsFragment.newInstance();
        open(ListAlbumsFragment, false, true);
    }

    public void startArtistFragment()
    {
        Fragment ArtistFrag = com.emo.lkplayer.outerlayer.view.fragments.ListArtistFragment.newInstance();
        open(ArtistFrag, false, true);
    }

    public void startGenreFragment()
    {
        Fragment GenreFrag = com.emo.lkplayer.outerlayer.view.fragments.ListGenreFragment.newInstance();
        open(GenreFrag, false, true);
    }

    /* ListTrackFragment----*/
    public void startListTracksFragment(String viaFolderQuery, String viaAlbumQuery, String viaArtistQuery, String playlistName)
    {
        startListTracksFragment(viaFolderQuery,viaAlbumQuery,viaArtistQuery,playlistName,-1);
    }

    public void startListTracksFragment(String viaFolderQuery, String viaAlbumQuery, String viaArtistQuery, String playlistName, long genreID)
    {
        ListTrackFragment.ListingMode mode = ListTrackFragment.ListingMode.MODE_ALL;
        String val = "";
        if (viaFolderQuery!=null)
        {
            mode = ListTrackFragment.ListingMode.MODE_FOLDER;
            val = viaFolderQuery;
        }
        else if (viaAlbumQuery!=null)
        {
            mode = ListTrackFragment.ListingMode.MODE_ALBUMS;
            val = viaAlbumQuery;
        }
        else if (viaArtistQuery!=null)
        {
            mode = ListTrackFragment.ListingMode.MODE_ARTISTS;
            val = viaArtistQuery;
        }
        else if (playlistName!=null)
        {
            mode = ListTrackFragment.ListingMode.MODE_PLAYLIST;
            val = playlistName;
        }
        else if (genreID!=-1)
        {
            mode = ListTrackFragment.ListingMode.MODE_GENRE;
            val = String.valueOf(genreID);
        }
        Fragment listTrackFragment = ListTrackFragment.newInstance(mode,val);
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

    public void ListTracksFragment_WithDQ_Start()
    {
        Fragment listTrackFragment = ListTrackFragment.newInstance(ListTrackFragment.ListingMode.MODE_DQ,null);
        open(listTrackFragment, false, true);
    }

    /* PlaylistFragment methods------*/
    public void Playlists_Fragment_Start()
    {
        ListPlaylistFragment fragment = ListPlaylistFragment.newInstance();
        open(fragment, false, true);
    }
    /*-------------------------------------------------------------------------------------------*/

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

    public void open(Fragment fragment, boolean beAddedToFragmentReferenceList, boolean beAddedToBackStack)
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
