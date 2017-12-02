package com.yjt.wallet.components.widget.tablayout.listener.implement;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yjt.wallet.components.utils.ViewUtil;
import com.yjt.wallet.components.widget.tablayout.listener.TabProvider;


public class SimpleTabProvider implements TabProvider {

    private final LayoutInflater inflater;
    private final int tabViewLayoutId;
    private final int tabViewTextViewId;

    public SimpleTabProvider(Context context, int layoutResId, int textViewId) {
        inflater = LayoutInflater.from(context);
        tabViewLayoutId = layoutResId;
        tabViewTextViewId = textViewId;
    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        View tabView = null;
        TextView tabTitleView = null;

        if (tabViewLayoutId != View.NO_ID) {
            tabView = inflater.inflate(tabViewLayoutId, container, false);
        }

        if (tabViewTextViewId != View.NO_ID && tabView != null) {
            tabTitleView = ViewUtil.getInstance().findView(tabView, tabViewTextViewId);
        }

        if (tabTitleView == null && TextView.class.isInstance(tabView)) {
            tabTitleView = (TextView) tabView;
        }

        if (tabTitleView != null) {
            tabTitleView.setText(adapter.getPageTitle(position));
        }

        return tabView;
    }

}