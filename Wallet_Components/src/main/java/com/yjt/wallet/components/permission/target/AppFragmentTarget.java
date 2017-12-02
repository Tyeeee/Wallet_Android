package com.yjt.wallet.components.permission.target;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import com.yjt.wallet.components.permission.listener.Target;

public class AppFragmentTarget implements Target {

    private Fragment fragment;

    public AppFragmentTarget(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public Context getContext() {
        return fragment.getActivity();
    }

    @Override
    public boolean shouldShowRationalePermissions(@NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        for (String permission : permissions) {
            if (fragment.shouldShowRequestPermissionRationale(permission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void startActivity(Intent intent) {
        fragment.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        fragment.startActivityForResult(intent, requestCode);
    }
}
