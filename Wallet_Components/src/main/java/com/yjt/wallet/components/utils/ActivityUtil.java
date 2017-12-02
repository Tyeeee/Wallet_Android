package com.yjt.wallet.components.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Process;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.common.collect.Lists;

import java.lang.ref.WeakReference;
import java.util.List;

public class ActivityUtil {

    private ActivityUtil() {
        // cannot be instantiated
    }

    private static List<WeakReference<Activity>> activities = Lists.newArrayList();

    @NonNull
    public static Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        throw new IllegalStateException("View " + view + " is not attached to an Activity");
    }

    public static void add(Activity activity) {
//        if (!activities.contains(activity)) {
        activities.add(new WeakReference<>(activity));
//        }
    }

    public static void print() {
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).get() != null) {
                LogUtil.getInstance().print(activities.get(i).get().getLocalClassName() + " ---- " + ((Activity) activities.get(i).get()).getClass().getName());
            }
        }
    }

    public static void remove(Activity activity) {
        LogUtil.getInstance().print(activity.getLocalClassName() + " ---- " + activity.getClass().getName());
        activities.remove(activity);
//        for (int i = 0; i < activities.size(); i++) {
//            if (activities.get(i).get() != null && TextUtils.equals(((Activity) activities.get(i).get()).getClass().getName(), activity.getClass().getName())) {
//                LogUtil.getInstance().print(activity.getLocalClassName() + " ---- " + activity.getClass().getName());
//                activities.get(i).get().finish();
//                activities.remove(activity);
//            }
//        }
    }

    public static void removeAll() {
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i).get() != null) {
                activities.get(i).get().finish();
            }
        }
        activities.clear();
        Process.killProcess(Process.myPid());
    }

    public static boolean contains(Activity activity) {
        return activities.contains(activity);
    }

    public static int size() {
        return activities.size();
    }
}
