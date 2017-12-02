package com.yjt.wallet.base.view;

import android.support.annotation.NonNull;

import com.yjt.wallet.base.dialog.listener.OnDialogNegativeListener;
import com.yjt.wallet.base.dialog.listener.OnDialogPositiveListener;

public interface BaseView<T> extends OnDialogPositiveListener, OnDialogNegativeListener {

    void setPresenter(@NonNull T presenter);

    BaseView getBaseView();

    boolean isActivityFinish();

    void showNetWorkPromptDialog();

    void showPermissionPromptDialog();

    void showLoadingPromptDialog(int resoutId, int requestCode);

    void hideLoadingPromptDialog();

    void showPromptDialog(int resoutId, boolean cancelable, boolean cancelableOnTouchOutside, int requestCode);

    void showPromptDialog(String prompt, boolean cancelable, boolean cancelableOnTouchOutside, int requestCode);

    void startPermissionSettingActivity();

    void refusePermissionSetting();
}
