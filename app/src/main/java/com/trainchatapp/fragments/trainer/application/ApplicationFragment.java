package com.trainchatapp.fragments.trainer.application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.trainchatapp.adapters.MyFragmentAdapter;
import com.trainchatapp.R;
import com.trainchatapp.fragments.CVFragment;
import com.trainchatapp.fragments.DescriptionFragment;

import java.util.List;
import java.util.Vector;

public class ApplicationFragment extends Fragment {


    private ViewPager screenPager;
    private TabLayout tabIndicator;
    private Button btnNext;
    private Button btnX;
    private int position = 0;
    private Button btnGetStarted;
    private Animation btnAnim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application, container, false);
        btnNext = view.findViewById(R.id.btn_next);
        btnX = view.findViewById(R.id.btn_x);
        tabIndicator = view.findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.button_animation);

        screenPager = view.findViewById(R.id.screen_viewpager);

        TermsAndConditionsFragment termsAndConditionsFragment = new TermsAndConditionsFragment();
        ApplicationIntroFragment applicationIntroFragment = new ApplicationIntroFragment();
        CVFragment cvFragment = new CVFragment();
        FacePhotoFragment faceFragment = new FacePhotoFragment();
        BodyPhotoFragment bodyFragment = new BodyPhotoFragment();
        DescriptionFragment descriptionFragment = new DescriptionFragment();
        IDVerificationFragment idVerificationFragment = new IDVerificationFragment();
        EligibilityToWorkFragment eligibilityToWorkFragment = new EligibilityToWorkFragment();
        AddressPhoneFragment addressPhoneFragment = new AddressPhoneFragment();
        ApplicationOverviewFragment applicationOverviewFragment = new ApplicationOverviewFragment();
        final List<Fragment> fragments = new Vector<Fragment>();

        fragments.add(termsAndConditionsFragment);
        fragments.add(applicationIntroFragment);
        fragments.add(cvFragment);
        fragments.add(idVerificationFragment);
        fragments.add(eligibilityToWorkFragment);
        fragments.add(faceFragment);
        fragments.add(bodyFragment);
        fragments.add(descriptionFragment);
        fragments.add(addressPhoneFragment);
        fragments.add(applicationOverviewFragment);

        PagerAdapter mPagerAdapter = new MyFragmentAdapter(this.getActivity().getSupportFragmentManager(), fragments);

        screenPager.setAdapter(mPagerAdapter);
        tabIndicator.setupWithViewPager(screenPager);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < fragments.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }
            }
        });

        return view;
    }


}