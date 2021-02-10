package com.occ.flightmanager.drawer.flights;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdapterForViewPager extends FragmentPagerAdapter {

    private final int count;

    public AdapterForViewPager(@NonNull FragmentManager fm, int count) {
        //noinspection deprecation
        super(fm);
        this.count = count;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0)
            return new Fragment_sf_Domestic();
        else
            return new Fragment_sf_Abroad();
    }

    @Override
    public int getCount() {
        return count;
    }
}
