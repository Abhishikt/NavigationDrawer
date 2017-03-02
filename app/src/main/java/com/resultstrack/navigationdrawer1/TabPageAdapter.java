package com.resultstrack.navigationdrawer1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by abhishikt sk on 3/2/2017.
 */

public class TabPageAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public TabPageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DashboardFragment tab1 = new DashboardFragment();
                return tab1;
            case 1:
                SummaryFragment tab2 = new SummaryFragment();
                return tab2;
            case 2:
                SettingsFragment tab3 = new SettingsFragment();
                return tab3;
            default:
                DashboardFragment tabD = new DashboardFragment();
                return tabD;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
