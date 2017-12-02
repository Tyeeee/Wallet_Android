package com.yjt.wallet.components.permission.listener;

import android.support.annotation.NonNull;

public interface Request<T extends Request> {

    @NonNull
    T permission(String... permissions);

    @NonNull
    T requestCode(int requestCode);

    T callback(Object callback);

    @Deprecated
    void send();

    void start();

}
