package com.yjt.wallet.components.permission.target;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import com.yjt.wallet.components.permission.listener.Target;

public class AppActivityTarget implements Target {

    private Activity activity;

    public AppActivityTarget(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Context getContext() {
        return activity;
    }

    @Override
    public boolean shouldShowRationalePermissions(@NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return false;
        }
        for (String permission : permissions) {
            if (activity.shouldShowRequestPermissionRationale(permission)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void startActivity(Intent intent) {
        activity.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }
}
