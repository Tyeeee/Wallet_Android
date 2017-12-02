package com.yjt.wallet.components.widget.tablayout.v4;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

public class FragmentPagerItemAdapter extends FragmentPagerAdapter {

    private final FragmentPagerItems pages;
    private final SparseArrayCompat<WeakReference<Fragment>> holder;
    private boolean destroyItem;

    public FragmentPagerItemAdapter(FragmentManager fm, FragmentPagerItems pages, boolean destroyItem) {
        super(fm);
        this.pages = pages;
        this.holder = new SparseArrayCompat<>(pages.size());
        this.destroyItem = destroyItem;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public Fragment getItem(int position) {
        return getPagerItem(position).instantiate(pages.getContext(), position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object item = super.instantiateItem(container, position);
        if (item instanceof Fragment) {
            holder.put(position, new WeakReference<>((Fragment) item));
        }
        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (destroyItem) {
            holder.remove(position);
            super.destroyItem(container, position, object);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getPagerItem(position).getTitle();
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    public Fragment getPage(int position) {
        final WeakReference<Fragment> weakRefItem = holder.get(position);
        return (weakRefItem != null) ? weakRefItem.get() : null;
    }

    protected FragmentPagerItem getPagerItem(int position) {
        return pages.get(position);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        if (destroyItem) {
            super.destroyItem(container, position, object);
        }
    }

}
