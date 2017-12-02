package com.yjt.wallet.components.permission.target;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.yjt.wallet.components.permission.listener.Target;

public class ContextTarget implements Target {

    private Context context;

    public ContextTarget(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public boolean shouldShowRationalePermissions(@NonNull String... permissions) {
        return false;
    }

    @Override
    public void startActivity(Intent intent) {
        context.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        context.startActivity(intent);
    }
}
