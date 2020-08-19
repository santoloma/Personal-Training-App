package com.trainchatapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import com.trainchatapp.R;
import com.trainchatapp.adapters.ViewPagerAdapter;


public class TabbedBookingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabbed_booking, container, false);
        final TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        final ViewPager viewPager = view.findViewById(R.id.view_pager);
        FragmentManager fm = getActivity().getSupportFragmentManager();

        ViewPagerAdapter myFragmentAdapter = new ViewPagerAdapter(fm);

        myFragmentAdapter.addFragment(new BookingsListFragment(), "All");
        myFragmentAdapter.addFragment(new BookingsListFragment(), "Upcoming");
        myFragmentAdapter.addFragment(new BookingsListFragment(), "Past");
        myFragmentAdapter.addFragment(new BookingsListFragment(), "Cancelled");

        viewPager.setAdapter(myFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}