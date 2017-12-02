package com.yjt.wallet.components.widget.tablayout.listener.implement;

import android.support.v4.view.ViewPager;

import com.hynet.mergepay.components.widget.tablayout.TabLayout;
import com.hynet.mergepay.components.widget.tablayout.TabStrip;

public class InternalViewPagerListener implements ViewPager.OnPageChangeListener {

    private int scrollState;
    private TabStrip tabStrip;
    private TabLayout tabLayout;
    private ViewPager.OnPageChangeListener viewPagerPageChangeListener;

    public InternalViewPagerListener(TabStrip tabStrip, TabLayout tabLayout, ViewPager.OnPageChangeListener viewPagerPageChangeListener) {
        this.tabStrip = tabStrip;
        this.tabLayout = tabLayout;
        this.viewPagerPageChangeListener = viewPagerPageChangeListener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int tabStripChildCount = tabStrip.getChildCount();
        if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
            return;
        }

        tabStrip.onViewPagerPageChanged(position, positionOffset);
        tabLayout.scrollToTab(position, positionOffset);

        if (viewPagerPageChangeListener != null) {
            viewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        scrollState = state;

        if (viewPagerPageChangeListener != null) {
            viewPagerPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
            tabStrip.onViewPagerPageChanged(position, 0f);
            tabLayout.scrollToTab(position, 0);
        }

        for (int i = 0, size = tabStrip.getChildCount(); i < size; i++) {
            tabStrip.getChildAt(i).setSelected(position == i);
        }

        if (viewPagerPageChangeListener != null) {
            viewPagerPageChangeListener.onPageSelected(position);
        }
    }

}
