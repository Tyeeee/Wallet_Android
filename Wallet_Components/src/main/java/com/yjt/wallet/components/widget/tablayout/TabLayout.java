package com.yjt.wallet.components.widget.tablayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yjt.wallet.components.R;
import com.yjt.wallet.components.utils.ViewUtil;
import com.yjt.wallet.components.widget.tablayout.listener.OnTabClickListener;
import com.yjt.wallet.components.widget.tablayout.listener.TabColorizer;
import com.yjt.wallet.components.widget.tablayout.listener.TabProvider;
import com.yjt.wallet.components.widget.tablayout.listener.implement.InternalViewPagerListener;
import com.yjt.wallet.components.widget.tablayout.listener.implement.SimpleTabProvider;


public class TabLayout extends HorizontalScrollView {

    private static final boolean DEFAULT_DISTRIBUTE_EVENLY = false;
    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TITLE_OFFSET_AUTO_CENTER = -1;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final boolean TAB_VIEW_TEXT_ALL_CAPS = true;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;
    private static final int TAB_VIEW_TEXT_COLOR = 0xFC000000;
    private static final int TAB_VIEW_TEXT_MIN_WIDTH = 0;
    private static final boolean TAB_CLICKABLE = true;

    protected final TabStrip tabStrip;
    private int titleOffset;
    private int tabViewBackgroundResId;
    private boolean tabViewTextAllCaps;
    private ColorStateList tabViewTextColors;
    private float tabViewTextSize;
    private int tabViewTextHorizontalPadding;
    private int tabViewTextMinWidth;
    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener viewPagerPageChangeListener;
    private com.yjt.wallet.components.widget.tablayout.listener.OnScrollChangeListener onScrollChangeListener;
    private TabProvider tabProvider;
    private InternalTabClickListener internalTabClickListener;
    private OnTabClickListener onTabClickListener;
    private boolean distributeEvenly;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        final float density = dm.density;

        int tabBackgroundResId = View.NO_ID;
        boolean textAllCaps = TAB_VIEW_TEXT_ALL_CAPS;
        ColorStateList textColors;
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP, dm);
        int textHorizontalPadding = (int) (TAB_VIEW_PADDING_DIPS * density);
        int textMinWidth = (int) (TAB_VIEW_TEXT_MIN_WIDTH * density);
        boolean distributeEvenly = DEFAULT_DISTRIBUTE_EVENLY;
        int customTabLayoutId = View.NO_ID;
        int customTabTextViewId = View.NO_ID;
        boolean clickable = TAB_CLICKABLE;
        int titleOffset = (int) (TITLE_OFFSET_DIPS * density);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout, defStyle, 0);
        tabBackgroundResId = a.getResourceId(R.styleable.TabLayout_tl_defaultTabBackground, tabBackgroundResId);
        textAllCaps = a.getBoolean(R.styleable.TabLayout_tl_defaultTabTextAllCaps, textAllCaps);
        textColors = a.getColorStateList(R.styleable.TabLayout_tl_defaultTabTextColor);
        textSize = a.getDimension(R.styleable.TabLayout_tl_defaultTabTextSize, textSize);
        textHorizontalPadding = a.getDimensionPixelSize(R.styleable.TabLayout_tl_defaultTabTextHorizontalPadding, textHorizontalPadding);
        textMinWidth = a.getDimensionPixelSize(R.styleable.TabLayout_tl_defaultTabTextMinWidth, textMinWidth);
        customTabLayoutId = a.getResourceId(R.styleable.TabLayout_tl_customTabTextLayoutId, customTabLayoutId);
        customTabTextViewId = a.getResourceId(R.styleable.TabLayout_tl_customTabTextViewId, customTabTextViewId);
        distributeEvenly = a.getBoolean(R.styleable.TabLayout_tl_distributeEvenly, distributeEvenly);
        clickable = a.getBoolean(R.styleable.TabLayout_tl_clickable, clickable);
        titleOffset = a.getLayoutDimension(R.styleable.TabLayout_tl_titleOffset, titleOffset);
        a.recycle();

        this.titleOffset = titleOffset;
        this.tabViewBackgroundResId = tabBackgroundResId;
        this.tabViewTextAllCaps = textAllCaps;
        this.tabViewTextColors = (textColors != null) ? textColors : ColorStateList.valueOf(TAB_VIEW_TEXT_COLOR);
        this.tabViewTextSize = textSize;
        this.tabViewTextHorizontalPadding = textHorizontalPadding;
        this.tabViewTextMinWidth = textMinWidth;
        this.internalTabClickListener = clickable ? new InternalTabClickListener() : null;
        this.distributeEvenly = distributeEvenly;

        this.tabStrip = new TabStrip(context, attrs);

        if (customTabLayoutId != View.NO_ID) {
            setCustomTabView(customTabLayoutId, customTabTextViewId);
        }
        if (distributeEvenly && tabStrip.isIndicatorAlwaysInCenter()) {
            throw new UnsupportedOperationException("'distributeEvenly' and 'indicatorAlwaysInCenter' both use does not support");
        }

        // Make sure that the Tab Strips fills this View
        setFillViewport(!tabStrip.isIndicatorAlwaysInCenter());

        addView(tabStrip, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangeListener != null) {
            onScrollChangeListener.onScrollChanged(l, oldl);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (tabStrip.isIndicatorAlwaysInCenter() && tabStrip.getChildCount() > 0) {
            View firstTab = tabStrip.getChildAt(0);
            View lastTab = tabStrip.getChildAt(tabStrip.getChildCount() - 1);
            int start = (w - ViewUtil.getInstance().getMeasuredWidth(firstTab)) / 2 - ViewUtil.getInstance().getMarginStart(firstTab);
            int end = (w - ViewUtil.getInstance().getMeasuredWidth(lastTab)) / 2 - ViewUtil.getInstance().getMarginEnd(lastTab);
            tabStrip.setMinimumWidth(tabStrip.getMeasuredWidth());
            ViewCompat.setPaddingRelative(this, start, getPaddingTop(), end, getPaddingBottom());
            setClipToPadding(false);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // Ensure first scroll
        if (changed && viewPager != null) {
            scrollToTab(viewPager.getCurrentItem(), 0);
        }
    }

    /**
     * Set the behavior of the Indicator scrolling feedback.
     *
     * @param interpolator {@link TabIndicationInterpolator}
     */
    public void setIndicationInterpolator(TabIndicationInterpolator interpolator) {
        tabStrip.setIndicationInterpolator(interpolator);
    }

    /**
     * Set the custom {@link TabColorizer} to be used.
     * <p>
     * If you only require simple customisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)} to achieve
     * similar effects.
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        tabStrip.setCustomTabColorizer(tabColorizer);
    }

    /**
     * Set the color used for styling the tab text. This will need to be called prior to calling
     * {@link #setViewPager(ViewPager)} otherwise it will not get set
     *
     * @param color to use for tab text
     */
    public void setDefaultTabTextColor(int color) {
        tabViewTextColors = ColorStateList.valueOf(color);
    }

    /**
     * Sets the colors used for styling the tab text. This will need to be called prior to calling
     * {@link #setViewPager(ViewPager)} otherwise it will not get set
     *
     * @param colors ColorStateList to use for tab text
     */
    public void setDefaultTabTextColor(ColorStateList colors) {
        tabViewTextColors = colors;
    }

    /**
     * Set the same weight for tab
     */
    public void setDistributeEvenly(boolean distributeEvenly) {
        this.distributeEvenly = distributeEvenly;
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setSelectedIndicatorColors(int... colors) {
        tabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Sets the colors to be used for tab dividers. These colors are treated as a circular array.
     * Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setDividerColors(int... colors) {
        tabStrip.setDividerColors(colors);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link TabLayout} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        viewPagerPageChangeListener = listener;
    }

    /**
     * Set {@link com.yjt.wallet.components.widget.tablayout.listener.OnScrollChangeListener} for obtaining values of scrolling.
     *
     * @param listener the {@link com.yjt.wallet.components.widget.tablayout.listener.OnScrollChangeListener} to set
     */
    public void setOnScrollChangeListener(com.yjt.wallet.components.widget.tablayout.listener.OnScrollChangeListener listener) {
        onScrollChangeListener = listener;
    }

    /**
     * Set {@link OnTabClickListener} for obtaining click event.
     *
     * @param listener the {@link OnTabClickListener} to set
     */
    public void setOnTabClickListener(OnTabClickListener listener) {
        onTabClickListener = listener;
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId  id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        tabProvider = new SimpleTabProvider(getContext(), layoutResId, textViewId);
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param provider {@link TabProvider}
     */
    public void setCustomTabView(TabProvider provider) {
        tabProvider = provider;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        tabStrip.removeAllViews();
        this.viewPager = viewPager;
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.addOnPageChangeListener(new InternalViewPagerListener(tabStrip, this, viewPagerPageChangeListener));
            populateTabStrip();
        }
    }

    /**
     * Returns the view at the specified position in the tabs.
     *
     * @param position the position at which to get the view from
     *
     * @return the view at the specified position or null if the position does not exist within the
     * tabs
     */
    public View getTabAt(int position) {
        return tabStrip.getChildAt(position);
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, int)}.
     */
    protected TextView createDefaultTabView(CharSequence title) {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText(title);
        textView.setTextColor(tabViewTextColors);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabViewTextSize);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

        if (tabViewBackgroundResId != View.NO_ID) {
            textView.setBackgroundResource(tabViewBackgroundResId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                                                     outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(tabViewTextAllCaps);
        }

        textView.setPadding(
                tabViewTextHorizontalPadding, 0,
                tabViewTextHorizontalPadding, 0);

        if (tabViewTextMinWidth > 0) {
            textView.setMinWidth(tabViewTextMinWidth);
        }

        return textView;
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = viewPager.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            final View tabView = (tabProvider == null)
                    ? createDefaultTabView(adapter.getPageTitle(i))
                    : tabProvider.createTabView(tabStrip, i, adapter);
            if (tabView == null) {
                throw new IllegalStateException("tabView is null.");
            }
            if (distributeEvenly) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }
            if (internalTabClickListener != null) {
                tabView.setOnClickListener(internalTabClickListener);
            }
            tabStrip.addView(tabView);
            if (i == viewPager.getCurrentItem()) {
                tabView.setSelected(true);
            }
        }
    }

    public void scrollToTab(int tabIndex, float positionOffset) {
        final int tabStripChildCount = tabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        final boolean isLayoutRtl = ViewUtil.getInstance().isLayoutRtl(this);
        View selectedTab = tabStrip.getChildAt(tabIndex);
        int widthPlusMargin = ViewUtil.getInstance().getWidth(selectedTab) + ViewUtil.getInstance().getMarginHorizontally(selectedTab);
        int extraOffset = (int) (positionOffset * widthPlusMargin);

        if (tabStrip.isIndicatorAlwaysInCenter()) {

            if (0f < positionOffset && positionOffset < 1f) {
                View nextTab = tabStrip.getChildAt(tabIndex + 1);
                int selectHalfWidth = ViewUtil.getInstance().getWidth(selectedTab) / 2 + ViewUtil.getInstance().getMarginEnd(selectedTab);
                int nextHalfWidth = ViewUtil.getInstance().getWidth(nextTab) / 2 + ViewUtil.getInstance().getMarginStart(nextTab);
                extraOffset = Math.round(positionOffset * (selectHalfWidth + nextHalfWidth));
            }

            View firstTab = tabStrip.getChildAt(0);
            int x;
            if (isLayoutRtl) {
                int first = ViewUtil.getInstance().getWidth(firstTab) + ViewUtil.getInstance().getMarginEnd(firstTab);
                int selected = ViewUtil.getInstance().getWidth(selectedTab) + ViewUtil.getInstance().getMarginEnd(selectedTab);
                x = ViewUtil.getInstance().getEnd(selectedTab) - ViewUtil.getInstance().getMarginEnd(selectedTab) - extraOffset;
                x -= (first - selected) / 2;
            } else {
                int first = ViewUtil.getInstance().getWidth(firstTab) + ViewUtil.getInstance().getMarginStart(firstTab);
                int selected = ViewUtil.getInstance().getWidth(selectedTab) + ViewUtil.getInstance().getMarginStart(selectedTab);
                x = ViewUtil.getInstance().getStart(selectedTab) - ViewUtil.getInstance().getMarginStart(selectedTab) + extraOffset;
                x -= (first - selected) / 2;
            }

            scrollTo(x, 0);
            return;

        }

        int x;
        if (titleOffset == TITLE_OFFSET_AUTO_CENTER) {

            if (0f < positionOffset && positionOffset < 1f) {
                View nextTab = tabStrip.getChildAt(tabIndex + 1);
                int selectHalfWidth = ViewUtil.getInstance().getWidth(selectedTab) / 2 + ViewUtil.getInstance().getMarginEnd(selectedTab);
                int nextHalfWidth = ViewUtil.getInstance().getWidth(nextTab) / 2 + ViewUtil.getInstance().getMarginStart(nextTab);
                extraOffset = Math.round(positionOffset * (selectHalfWidth + nextHalfWidth));
            }

            if (isLayoutRtl) {
                x = -ViewUtil.getInstance().getWidthWithMargin(selectedTab) / 2 + getWidth() / 2;
                x -= ViewUtil.getInstance().getPaddingStart(this);
            } else {
                x = ViewUtil.getInstance().getWidthWithMargin(selectedTab) / 2 - getWidth() / 2;
                x += ViewUtil.getInstance().getPaddingStart(this);
            }

        } else {

            if (isLayoutRtl) {
                x = (tabIndex > 0 || positionOffset > 0) ? titleOffset : 0;
            } else {
                x = (tabIndex > 0 || positionOffset > 0) ? -titleOffset : 0;
            }

        }

        int start = ViewUtil.getInstance().getStart(selectedTab);
        int startMargin = ViewUtil.getInstance().getMarginStart(selectedTab);
        if (isLayoutRtl) {
            x += start + startMargin - extraOffset - getWidth() + ViewUtil.getInstance().getPaddingHorizontally(this);
        } else {
            x += start - startMargin + extraOffset;
        }
        scrollTo(x, 0);
    }

    private class InternalTabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                if (v == tabStrip.getChildAt(i)) {
                    if (onTabClickListener != null) {
                        onTabClickListener.onTabClicked(i);
                    }
                    viewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }
}
