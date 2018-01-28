package com.emo.emomediaplayerpro.view_ui.view.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.emo.emomediaplayerpro.view_ui.view.fragments.NavToneAndVolFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoaibanwar on 6/9/17.
 */

public final class NavigationManagerSettingsFlow extends BaseNavigationManager {

    private List<Fragment> existingFragmentsList;

    /* Shoaib: Constructors here */
    public NavigationManagerSettingsFlow() {
        this.existingFragmentsList = new ArrayList<>();
    }


    /* Shoaib: Methods here */
    public boolean bringExistingFragment(String fragmentClassQualifiedName, boolean beAddedToBackStack) {
        for (int i = 0; i < this.existingFragmentsList.size(); i++) {
            if (this.existingFragmentsList.get(i).getClass().toString().equals(fragmentClassQualifiedName)) {
                open(this.existingFragmentsList.get(i), false, beAddedToBackStack);
                return true;
            }
        }
        return false;
    }

    private void addToFragmentInstancesList(Fragment fragment) {
        /* Shoaib: Added to list, so that this is not recreated again, we store its reference */
        this.existingFragmentsList.add(fragment);
    }

    private void addTransactionToBackStack(FragmentTransaction transaction, Fragment fragment) {
        transaction.addToBackStack(fragment.toString());
    }

    /* Shoaib: This open method overloaded only to give default boolean param*/
    public void open(Fragment fragment, boolean beAddedToFragmentReferenceList,
                     boolean beAddedToBackStack) {

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

    public void navigateBack(AppCompatActivity baseActivity) {

        if (mFragmentManager.getBackStackEntryCount() <= 1) {
            // we can finish the base activity since we have no other fragments
            baseActivity.finish();
        } else {
            mFragmentManager.popBackStackImmediate();
        }
    }

    public void startUpInitialFragment_Equalizer() {
        Fragment fragment = NavToneAndVolFragment.newInstance(null, null);
        openAsRoot(fragment);
    }

}
