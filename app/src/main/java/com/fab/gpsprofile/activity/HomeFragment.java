package com.fab.gpsprofile.activity;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.astuetz.PagerSlidingTabStrip;

import com.fab.gpsprofile.R;
import com.fab.gpsprofile.adapter.HomePagerAdapter;
import com.fab.gpsprofile.activity.api.BaseFragment;
import com.fab.gpsprofile.interfaces.home.HomeView;
import com.fab.gpsprofile.presenter.HomePresenterImpl;


public class HomeFragment extends BaseFragment implements HomeView {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private View mViewHome;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private HomePresenterImpl mHomePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewHome = inflater.inflate(R.layout.fragment_default, container, false);

        loadViewComponents();
        initPresenter();
        loadSectionsTabs();

        return mViewHome;
    }

    @Override
    public void onDestroy() {
        mHomePresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void loadViewComponents() {
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) mViewHome.findViewById(R.id.fragment_home_pager_sliding_tab);
        mViewPager = (ViewPager) mViewHome.findViewById(R.id.fragment_home_view_pager);
    }

    @Override
    public void initPresenter() {
        mHomePresenter = new HomePresenterImpl(this);
    }

    @Override
    public void loadSectionsTabs() {
        mHomePresenter.loadSectionsTabs();
    }

    @Override
    public void loadViewPager(List<String> listTitleTabs) {
        mViewPager.setAdapter(new HomePagerAdapter(listTitleTabs, getChildFragmentManager()));
    }

    @Override
    public void setColorTabs(int color) {
        mPagerSlidingTabStrip.setTextColor(color);
    }

    @Override
    public void setDividerColorTabs(int color) {
        mPagerSlidingTabStrip.setDividerColor(mViewHome.getResources().getColor(R.color.theme_dialer_primary));
    }

    @Override
    public void setIndicatorColorTabs(int color) {
        mPagerSlidingTabStrip.setDividerColor(color);
    }

    @Override
    public void loadTabs() {
        mPagerSlidingTabStrip.setViewPager(mViewPager);
    }
}