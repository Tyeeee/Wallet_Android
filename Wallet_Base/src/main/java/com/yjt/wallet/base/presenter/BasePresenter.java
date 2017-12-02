package com.yjt.wallet.base.presenter;


import com.yjt.wallet.base.view.BaseView;
import com.yjt.wallet.components.permission.listener.PermissionCallback;

public interface BasePresenter {

    void initialize();

    void checkPermission(PermissionCallback permissionCallback, String... permissions);

    boolean checkGesturePassword(BaseView view);
}
