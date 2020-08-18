package com.trainchatapp.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter {

    public static int pos = 0;
    private List<Fragment> myFragments;

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> myFrags) {
        super(fm);
        myFragments = myFrags;
    }

    @Override
    public Fragment getItem(int position) {
        return myFragments.get(position);
    }

    @Override
    public int getCount() {
        return myFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    public static int getPos() {
        return pos;
    }

    public static void setPos(int pos) {
        MyFragmentAdapter.pos = pos;
    }
}