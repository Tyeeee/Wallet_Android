package com.yjt.wallet.components.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;

import com.yjt.wallet.components.constant.Constant;

public class InputUtil {

    private static InputMethodManager inputMethodManager;
    private static long lastClickTime;

    private static InputUtil inputUtil;

    private InputUtil() {
        // cannot be instantiated
    }

    public static synchronized InputUtil getInstance() {
        if (inputUtil == null) {
            inputUtil = new InputUtil();
        }
        return inputUtil;
    }

    public static void releaseInstance() {
        if (inputUtil != null) {
            inputUtil = null;
        }
    }


    public void hideKeyBoard(MotionEvent event, Activity activity) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (inputMethodManager == null)
                inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                if (activity.getCurrentFocus().getWindowToken() != null) {
                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    public void hideKeyBoard(final Context context, ScrollView scrollView) {
        scrollView.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                View view = (((Activity) context).getCurrentFocus());
                if (view != null)
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
    }

    public void closeKeyBoard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void hideKeyBoard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyBoard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    public static boolean isActiveSoftInput(Context context) {
        return ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).isActive();
    }

    public boolean isDoubleClick() {
        long timeS = System.currentTimeMillis();
        long timeE = lastClickTime - timeS;
        if (timeE > Constant.View.CLICK_PERIOD) {
            return true;
        }
        lastClickTime = timeS;
        return false;
    }
}
