package com.yjt.wallet.base.handler;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.SoftReference;

public abstract class ActivityHandler<T extends Activity> extends Handler {

    private final SoftReference<T> activities;

    public ActivityHandler(T activity) {
        activities = new SoftReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        T activity = activities.get();
        if (activity == null) {
            return;
        }
        handleMessage(activity, msg);
    }

    protected abstract void handleMessage(T activity, Message msg);
}
