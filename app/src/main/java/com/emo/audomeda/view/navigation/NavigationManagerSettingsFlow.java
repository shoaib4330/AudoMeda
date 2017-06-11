package com.emo.audomeda.view.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.emo.audomeda.R;
import com.emo.audomeda.view.fragments.EqualizerFragment;
import com.emo.audomeda.view.fragments.ToneAndVolFragment;
import com.emo.audomeda.view.navigation.NavigationManagerContentFlow;

/**
 * Created by shoaibanwar on 6/9/17.
 */

public final class NavigationManagerSettingsFlow {
    /**
     * Listener interface for navigation events.
     */
    public interface NavigationListener {

        /**
         * Callback on backstack changed.
         */
        void onBackstackChanged();
    }


    // ------------------------------------------------------------------------
    // STATIC FIELDS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // STATIC METHODS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // FIELDS
    // ------------------------------------------------------------------------

    private FragmentManager mFragmentManager;

    private NavigationManagerContentFlow.NavigationListener mNavigationListener;


    // ------------------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // METHODS
    // ------------------------------------------------------------------------

    /**
     * Initialize the NavigationManager with a FragmentManager, which will be used at the
     * fragment transactions.
     *
     * @param fragmentManager
     */
    public void init(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (mNavigationListener != null) {
                    mNavigationListener.onBackstackChanged();
                }
            }
        });
    }

    private void open(Fragment fragment) {
        if (mFragmentManager != null) {
            //@formatter:off
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragmentHolder_SettingsActivity, fragment)
                    //.addToBackStack(fragment.toString())
                    .commit();
            //@formatter:on
        }
    }

    private void openAsRoot(Fragment fragment) {
        popEveryFragment();
        open(fragment);
    }

    private void popEveryFragment() {
        // Clear all back stack.
        int backStackCount = mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {

            // Get the back stack fragment id.
            int backStackId = mFragmentManager.getBackStackEntryAt(i).getId();

            mFragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
    }

    public void navigateBack(AppCompatActivity baseActivity) {

        if (mFragmentManager.getBackStackEntryCount() <= 1) {
            // we can finish the base activity since we have no other fragments
            baseActivity.finish();
        } else {
            mFragmentManager.popBackStackImmediate();
        }
    }



    public void startUpInitialFragment_Equalizer(){
        Fragment fragment = EqualizerFragment.newInstance(null,null);
        openAsRoot(fragment);
    }

    public void startEqualizerFragment(){
        Fragment fragment = EqualizerFragment.newInstance(null,null);
        open(fragment);
    }

    public void startToneandVolFragment(){
        Fragment fragment = ToneAndVolFragment.newInstance(null,null);
        open(fragment);
    }

    public boolean isRootFragmentVisible() {
        return mFragmentManager.getBackStackEntryCount() <= 1;
    }

    public NavigationManagerContentFlow.NavigationListener getNavigationListener() {
        return mNavigationListener;
    }

    public void setNavigationListener(NavigationManagerContentFlow.NavigationListener navigationListener) {
        mNavigationListener = navigationListener;
    }

    // ------------------------------------------------------------------------
    // GETTERS / SETTTERS
    // ------------------------------------------------------------------------
}
