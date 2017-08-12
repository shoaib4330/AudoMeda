package com.emo.lkplayer.outerlayer.view;

import com.emo.lkplayer.outerlayer.view.navigation.BaseNavigationManager;

/**
 * Created by shoaibanwar on 7/2/17.
 */

public interface FragmentInteractionListener {
    BaseNavigationManager getNavigationManager();

    interface FragmentAndToolbarInteractionListener extends FragmentInteractionListener
    {
        void showToolbar();

        void hideToolbar();

        void showBottomNavBar();

        void hideBottomNavBar();
    }
}
