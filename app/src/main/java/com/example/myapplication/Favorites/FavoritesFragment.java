package com.example.myapplication.Favorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by jarod on 10/29/2017.
 */

public class FavoritesFragment extends Fragment {
    private static GoogleApiClient googleApiClient;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private PageAdapter mPageAdapter;
    public static FavoritesFragment newInstance(GoogleApiClient client){
        googleApiClient = client;
        return new FavoritesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText("Restaruant"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Bars"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Cafe"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mPageAdapter = new PageAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    public class PageAdapter extends FragmentPagerAdapter {

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Fragment fragment = new FavoritesPageFragment();
                    return fragment;
                case 1:
                    Fragment fragment2 = new FavoritesPageFragment();
                    return fragment2;
                case 2:
                    Fragment fragment3 = new FavoritesPageFragment();
                    return fragment3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
