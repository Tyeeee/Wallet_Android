package com.yjt.wallet.components.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Vibrator;
import android.text.TextUtils;

import com.yjt.wallet.components.constant.Regex;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

public class ApplicationUtil {

    private static ApplicationUtil mApplicationUtil;

    private ApplicationUtil() {
        // cannot be instantiated
    }

    public static synchronized ApplicationUtil getInstance() {
        if (mApplicationUtil == null) {
            mApplicationUtil = new ApplicationUtil();
        }
        return mApplicationUtil;
    }

    public static void releaseInstance() {
        if (mApplicationUtil != null) {
            mApplicationUtil = null;
        }
    }

    public String getPackageName(Context ctx) {
        return ctx.getPackageName();
    }

    public PackageInfo getPackageInfo(Context ctx) {
        PackageInfo info = null;
        try {
            info = ctx.getPackageManager().getPackageInfo(getPackageName(ctx), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    public String getVersionName(Context ctx) {
        return getPackageInfo(ctx).versionName;
    }

    public Object getMetaData(Context ctx, String key) {
        try {
            return ctx.getPackageManager().getApplicationInfo(getPackageName(ctx), PackageManager.GET_META_DATA).metaData.get(key);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getVersionCode(Context ctx) {
        return getPackageInfo(ctx).versionCode;
    }

    public boolean getInstallStatus(Context ctx, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        for (PackageInfo info : ctx.getPackageManager()
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES)) {
            if (info.packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    public boolean isPerceptible(Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        if (infos != null && infos.size() > 0) {
            for (RunningAppProcessInfo info : infos) {
                // LogUtil.print("info:" + info.processName + "-" +
                // info.importance);
//                if (info.processName.equals(ctx.getPackageName())
//                        && info.importance == RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
//                    return true;
//                }
                if (info.processName.equals(ctx.getPackageName())) {
                    if (info.importance == RunningAppProcessInfo.IMPORTANCE_VISIBLE || info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String getProcessName(Context ctx) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningAppProcessInfo appProcess : manager.getRunningAppProcesses()) {
            if (appProcess.pid == android.os.Process.myPid()) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public boolean isProcessRunning(Context context, String processName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for (RunningAppProcessInfo info : infos) {
            if (info.processName.equals(processName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isProcessRunningTop(Context context, String processName) {
        if (context == null || TextUtils.isEmpty(processName)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            LogUtil.getInstance().print("proessName:" + processName + ", cpn.getClassName():" + cpn.getClassName());
            if (processName.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAppRunningTop(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            LogUtil.getInstance().print("packageName:" + packageName + ", cpn.getPackageName():" + cpn.getPackageName());
            if (packageName.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isServiceRunning(Context ctx, String className) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (infos.size() != 0) {
            for (int i = 0; i < infos.size(); i++) {
                if (infos.get(i).service.getClassName().equals(className)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void coptyToClipBoard(Context ctx, String content) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", content);
            clipboard.setPrimaryClip(clip);
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(content);
        }
    }

    public static void vibrate(Context ctx, long duration) {
        Vibrator vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, duration};
        vibrator.vibrate(pattern, -1);
    }

    public static void goHome(Context ctx) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        ctx.startActivity(intent);
    }

    public boolean chmod(String permission, String path) {
        try {
            String command = (new StringBuilder(Regex.CHMOD.getRegext())).append(permission).append(Regex.SPACE.getRegext()).append(path).toString();
            Runtime runtime = Runtime.getRuntime();
            Process pr = runtime.exec(command);
            pr.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getSha1(Context ctx) {
        try {
            @SuppressLint("PackageManagerGetSignatures") 
            PackageInfo info = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest messageDigest = MessageDigest.getInstance(Regex.SHA1.getRegext());
            StringBuilder hexString = new StringBuilder();
            for (byte publicKey : messageDigest.digest(cert)) {
                String appendString = Integer.toHexString(0xFF & publicKey)
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append(Regex.ZERO.getRegext());
                hexString.append(appendString);
                hexString.append(Regex.COLON.getRegext());
            }
            return hexString.toString();
        } catch (NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
