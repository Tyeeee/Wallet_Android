package com.yjt.wallet.components.utils;

import android.os.Build;
import android.os.StrictMode;

import com.yjt.wallet.components.BuildConfig;

public class StrictModeUtil {

    private static StrictModeUtil strictModeUtil;

    private StrictModeUtil() {
        // cannot be instantiated
    }

    public static synchronized StrictModeUtil getInstance() {
        if (strictModeUtil == null) {
            strictModeUtil = new StrictModeUtil();
        }
        return strictModeUtil;
    }

    public static void releaseInstance() {
        if (strictModeUtil != null) {
            strictModeUtil = null;
        }
    }

    public void initialize() {
        if (BuildConfig.DEBUG) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            builder.detectAll();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                builder.detectLeakedClosableObjects();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    builder.detectLeakedRegistrationObjects();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        builder.detectFileUriExposure();
                    }
                }
            }
            builder.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().penaltyDropBox();
            StrictMode.setVmPolicy(builder.build());
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                                               .detectNetwork()
                                               .penaltyLog()
                                               .penaltyDropBox()
                                               .penaltyDialog()
                                               .build());
        }
    }
}
