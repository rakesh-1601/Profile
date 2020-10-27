package com.example.profile.Adapters;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.profile.Fragments.Tab1;
import com.example.profile.Fragments.Tab2;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Fragment[] childFragments;
    private String[] titles = {"Personal Info", "Other Interest"};


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        childFragments = new Fragment[]{
                new Tab1(), //0
                new Tab2(), //1
        };

    }

    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length; //3 items
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
