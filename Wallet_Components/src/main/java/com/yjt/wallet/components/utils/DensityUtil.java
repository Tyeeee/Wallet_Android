package com.yjt.wallet.components.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

public class DensityUtil {

    private static DensityUtil densityUtil;
    private Context context;

    private DensityUtil(Context context) {
        // cannot be instantiated
        this.context = context;
    }

    public static synchronized DensityUtil getInstance(Context context) {
        if (densityUtil == null) {
            densityUtil = new DensityUtil(context);
        }
        return densityUtil;
    }

    public static void releaseInstance() {
        if (densityUtil != null) {
            densityUtil = null;
        }
    }

    public int getScreenWidth() {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public float getDensity() {
        return context.getResources().getDisplayMetrics().density;
    }

    public int getDensityDpi() {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public int dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dp(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public int getNavigationBarStatus() {
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("navigation_bar_height", "dimen", "android"));
        } else {
            return 0;
        }
    }
}