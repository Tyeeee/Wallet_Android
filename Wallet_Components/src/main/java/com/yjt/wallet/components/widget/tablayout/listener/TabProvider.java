package com.yjt.wallet.components.widget.tablayout.listener;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Create the custom tabs in the tab layout. Set with
 * {@link #setCustomTabView(TabProvider)}
 */
public interface TabProvider {

    /**
     * @return Return the View of {@code position} for the Tabs
     */
    View createTabView(ViewGroup container, int position, PagerAdapter adapter);

}
