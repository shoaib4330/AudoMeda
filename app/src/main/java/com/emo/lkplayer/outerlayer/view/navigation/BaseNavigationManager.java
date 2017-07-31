package com.emo.lkplayer.outerlayer.view.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

/**
 * Created by shoaibanwar on 7/2/17.
 */

public abstract class BaseNavigationManager implements Serializable {
    /**
     * Listener interface for navigation events.
     */
    public interface NavigationListener {
        void onBackstackChanged();
    }

    /* reference to the Navigatin Events Listerner */
    protected NavigationListener mNavigationListener;

    protected int Fragment_Container_ID;

    protected FragmentManager mFragmentManager;

    /**
     * Initialize the NavigationManager with a FragmentManager, which will be used at the
     * fragment transactions.
     *
     * @param fragmentManager
     */
    public void init(FragmentManager fragmentManager,int Fragment_Container_ID) {
        this.Fragment_Container_ID = Fragment_Container_ID;
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

    protected void open(Fragment fragment) {
        if (mFragmentManager != null) {
            //@formatter:off
            mFragmentManager.beginTransaction()
                    .replace(this.Fragment_Container_ID, fragment)
                    .addToBackStack(fragment.toString())
                    .commit();
            //@formatter:on
            }
    }

    protected void openAsRoot(Fragment fragment) {
        /* Shoaib: This is actually useless here, since we have not added any
        any transaction to backstack. Popping is redundant. */
        popEveryFragment();
        open(fragment);
    }

    public void navigateBack(AppCompatActivity baseActivity) {
        if (mFragmentManager.getBackStackEntryCount() <= 1) {
            // we can finish the base activity since we have no other fragments
            baseActivity.finish();
        } else {
            mFragmentManager.popBackStackImmediate();
        }
    }

    protected void popEveryFragment() {
        // Clear all back stack.
        int backStackCount = mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            // Get the back stack fragment id.
            int backStackId = mFragmentManager.getBackStackEntryAt(i).getId();
            mFragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public boolean isRootFragmentVisible() {
        return mFragmentManager.getBackStackEntryCount() <= 1;
    }

    public NavigationListener getNavigationListener() {
        return mNavigationListener;
    }

    public void setNavigationListener(NavigationListener navigationListener) {
        mNavigationListener = navigationListener;
    }
}
