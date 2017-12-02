package com.yjt.wallet.components.utils;

import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yjt.wallet.components.R;
import com.yjt.wallet.components.constant.Constant;

public class SnackBarUtil {

    private Snackbar snackbar;

    private static SnackBarUtil snackBarUtil;

    private SnackBarUtil() {
        // cannot be instantiated
    }

    public static synchronized SnackBarUtil getInstance() {
        if (snackBarUtil == null) {
            snackBarUtil = new SnackBarUtil();
        }
        return snackBarUtil;
    }

    public static void releaseInstance() {
        if (snackBarUtil != null) {
            snackBarUtil = null;
        }
    }

    public void showSnackBar(FragmentActivity activity, CharSequence message, int length) {
        showSnackBar(activity, message, length, null, null, Constant.View.DEFAULT_SIZE, Constant.View.DEFAULT_COLOR);
    }

    public void showSnackBar(FragmentActivity activity, CharSequence message, int length, float textSize) {
        showSnackBar(activity, message, length, null, null, textSize, Constant.View.DEFAULT_COLOR);
    }

    public void showSnackBar(FragmentActivity activity, CharSequence message, int length, int color) {
        showSnackBar(activity, message, length, null, null, Constant.View.DEFAULT_SIZE, color);
    }

    public void showSnackBar(FragmentActivity activity, CharSequence message, int length, float textSize, int color) {
        showSnackBar(activity, message, length, null, null, textSize, color);
    }

    public void showSnackBar(FragmentActivity activity, CharSequence message1, int length, CharSequence message2, View.OnClickListener listener) {
        showSnackBar(activity, message1, length, message2, listener, Constant.View.DEFAULT_SIZE, Constant.View.DEFAULT_COLOR);
    }


    public void showSnackBar(FragmentActivity activity, CharSequence message1, int length, CharSequence message2, View.OnClickListener listener, float textSize, int color) {
        if (activity != null) {
            if (snackbar == null) {
                snackbar = Snackbar.make(activity.getWindow().getDecorView(), message1, length);
            } else {
                snackbar.setText(message1);
            }

            if (listener != null) {
                if (!TextUtils.isEmpty(message2)) {
                    snackbar.setAction(message2, listener);
                }
            }

            if (textSize != Constant.View.DEFAULT_SIZE) {
                ((TextView) ViewUtil.getInstance().findView(snackbar.getView(), R.id.snackbar_text)).setTextSize(textSize);
            }
            if (color != Constant.View.DEFAULT_COLOR) {
                ((TextView) ViewUtil.getInstance().findView(snackbar.getView(), R.id.snackbar_text)).setTextColor(color);
            }
            snackbar.show();
        }
    }

    public void hideSnackBar() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    public boolean isShown() {
        return snackbar != null && snackbar.isShownOrQueued();
    }
}
