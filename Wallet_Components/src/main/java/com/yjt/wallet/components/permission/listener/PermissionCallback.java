package com.yjt.wallet.components.permission.listener;

import android.support.annotation.NonNull;

import java.util.List;

public interface PermissionCallback {

    void onSuccess(int requestCode, @NonNull List<String> grantPermissions);

    void onFailed(int requestCode, @NonNull List<String> deniedPermissions);
}
