package com.yjt.wallet.ui.contract.implement;

import android.content.Context;

import com.yjt.wallet.base.permission.Permission;
import com.yjt.wallet.base.presenter.BasePresenter;
import com.yjt.wallet.base.view.BaseView;
import com.yjt.wallet.components.permission.listener.PermissionCallback;
import com.yjt.wallet.components.utils.LogUtil;
import com.yjt.wallet.constant.Constant;

import java.util.Arrays;

public abstract class BasePresenterImplement implements BasePresenter {

    protected Context context;

    @Override
    public void initialize() {
    }

    @Override
    public void checkPermission(PermissionCallback permissionCallback, String... permissions) {
        LogUtil.getInstance().print("checkPermission");
        if (context != null) {
            if (permissions == null || permissions.length == 0) {
                permissions = Constant.PERMISSIONS;
            }
            if (!Permission.getInstance().hasPermission(context, permissions)) {
                Permission.getInstance().with(context)
                        .requestCode(com.yjt.wallet.base.constant.Constant.RequestCode.PERMISSION)
                        .permission(permissions)
                        .callback(permissionCallback)
                        .start();
            } else {
                permissionCallback.onSuccess(com.yjt.wallet.base.constant.Constant.RequestCode.PERMISSION, Arrays.asList(permissions));
            }
        }
    }

    @Override
    public boolean checkGesturePassword(BaseView view) {
        return false;
    }
}
