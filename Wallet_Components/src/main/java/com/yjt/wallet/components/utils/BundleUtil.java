package com.yjt.wallet.components.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.yjt.wallet.components.constant.Constant;

import java.io.Serializable;
import java.util.Collection;

public class BundleUtil {

    private static BundleUtil bundleUtil;

    private BundleUtil() {
        // cannot be instantiated
    }

    public static synchronized BundleUtil getInstance() {
        if (bundleUtil == null) {
            bundleUtil = new BundleUtil();
        }
        return bundleUtil;
    }

    public static void releaseInstance() {
        if (bundleUtil != null) {
            bundleUtil = null;
        }
    }

    public boolean hasIntentExtraValue(Activity activity, String extraKey) {
        return activity.getIntent() != null && activity.getIntent().hasExtra(extraKey);
    }

    public boolean hasBundleExtraValue(Activity activity, String extraKey) {
        return activity.getIntent().getExtras() != null && activity.getIntent().hasExtra(extraKey);
    }

    public int getIntData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            if (activity.getIntent().hasExtra(key)) {
                return activity.getIntent().getExtras().getInt(key);
            }
        }
        return Constant.Extra.DEFAULT_VALUE;
    }

    public int getIntData(Bundle bundle, String key) {
        if (bundle != null) {
            return bundle.getInt(key);
        }
        return Constant.Extra.DEFAULT_VALUE;
    }

    public int getIntData(Intent intent, String key) {
        if (intent != null) {
            return intent.getIntExtra(key, Constant.Extra.DEFAULT_VALUE);
        }
        return Constant.Extra.DEFAULT_VALUE;
    }

    public float getFloatData(Bundle bundle, String key) {
        if (bundle != null) {
            return bundle.getFloat(key);
        }
        return Constant.Extra.DEFAULT_VALUE;
    }

    public float getFloatData(Intent intent, String key) {
        if (intent != null) {
            return intent.getFloatExtra(key, Constant.Extra.DEFAULT_VALUE);
        }
        return Constant.Extra.DEFAULT_VALUE;
    }

    public double getDoubleData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            return activity.getIntent().getExtras().getDouble(key);
        }
        return Constant.Extra.DEFAULT_VALUE;
    }


    public long getLongData(Bundle bundle, String key, long defaultValue) {
        if (bundle != null) {
            return bundle.getLong(key, defaultValue);
        }
        return Constant.Extra.DEFAULT_VALUE;
    }


    public CharSequence getCharSequenceData(Bundle bundle, String key) {
        if (bundle != null) {
            return bundle.getCharSequence(key);
        }
        return null;
    }

    public CharSequence[] getCharSequenceArrayData(Bundle bundle, String key) {
        if (bundle != null) {
            return bundle.getCharSequenceArray(key);
        }
        return null;
    }

    public String getStringData(Bundle bundle, String key) {
        if (bundle != null) {
            return bundle.getString(key);
        }
        return null;
    }

    public String getStringData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            if (activity.getIntent().hasExtra(key)) {
                return activity.getIntent().getExtras().getString(key);
            }
        }
        return null;
    }

    public boolean getBooleanData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            if (activity.getIntent().hasExtra(key)) {
                return activity.getIntent().getExtras().getBoolean(key);
            }
        }
        return false;
    }

    public boolean getBooleanData(Bundle bundle, String key) {
        return bundle != null && bundle.getBoolean(key);
    }

    public Bundle getBundleData(Intent intent, String key) {
        if (intent != null) {
            return intent.getBundleExtra(key);
        }
        return null;
    }

    public <T extends Serializable> T getSerializableData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            return (T) activity.getIntent().getExtras().getSerializable(key);
        }
        return null;
    }

    public <T extends Serializable> T getSerializableData(Bundle bundle, String key) {
        if (bundle != null) {
            return (T) bundle.getSerializable(key);
        }
        return null;
    }

    public <T extends Parcelable> T getParcelableData(Activity activity, String key) {
        if (activity.getIntent() != null) {
            if (activity.getIntent().hasExtra(key)) {
                return (T) activity.getIntent().getExtras().getParcelable(key);
            }
        }
        return null;
    }

    public <T extends Parcelable> T getParcelableData(Bundle bundle, String key) {
        if (bundle != null) {
            return (T) bundle.getParcelable(key);
        }
        return null;
    }

    public <T extends Collection<? extends Parcelable>> T getParcelableArrayListData(Bundle bundle, String key) {
        if (bundle != null) {
            return (T) bundle.getParcelableArrayList(key);
        }
        return null;
    }
}
