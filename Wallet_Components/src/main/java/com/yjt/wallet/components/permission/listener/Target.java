package com.yjt.wallet.components.permission.listener;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * <p>Request target.</p>
 * Created by Yan Zhenjie on 2017/5/1.
 */
public interface Target {

    Context getContext();

    boolean shouldShowRationalePermissions(@NonNull String... permissions);

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

}
