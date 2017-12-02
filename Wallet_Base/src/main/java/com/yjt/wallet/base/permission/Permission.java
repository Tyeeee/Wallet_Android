package com.yjt.wallet.base.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.yjt.wallet.components.permission.listener.RationaleRequest;
import com.yjt.wallet.components.permission.listener.Request;
import com.yjt.wallet.components.permission.target.AppActivityTarget;
import com.yjt.wallet.components.permission.target.AppFragmentTarget;
import com.yjt.wallet.components.permission.target.ContextTarget;
import com.yjt.wallet.components.permission.target.SupportFragmentTarget;

import java.util.List;

public class Permission {

    private static Permission permission;

    private Permission() {
        // cannot be instantiated
    }

    public static synchronized Permission getInstance() {
        if (permission == null) {
            permission = new Permission();
        }
        return permission;
    }

    public static void releaseInstance() {
        if (permission != null) {
            permission = null;
        }
    }

    public boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        } else {
            for (String permission : permissions) {
//                LogUtil.getInstance().print("permission:" + permission);
                String option = AppOpsManagerCompat.permissionToOp(permission);
//                LogUtil.getInstance().print("option:" + option);
                if (!TextUtils.isEmpty(option)) {
                    int result = AppOpsManagerCompat.noteProxyOp(context, option, context.getPackageName());
//                    LogUtil.getInstance().print("result:" + result);
                    if (result == AppOpsManagerCompat.MODE_IGNORED) {
                        return false;
                    }
                    result = ContextCompat.checkSelfPermission(context, permission);
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean hasAlwaysDeniedPermission(@NonNull Activity activity, @NonNull List<String> deniedPermissions) {
        return !new AppActivityTarget(activity).shouldShowRationalePermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]));
    }

    public boolean hasAlwaysDeniedPermission(@NonNull android.support.v4.app.Fragment fragment, @NonNull List<String> deniedPermissions) {
        return !new SupportFragmentTarget(fragment).shouldShowRationalePermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]));
    }

    public boolean hasAlwaysDeniedPermission(@NonNull android.app.Fragment fragment, @NonNull List<String> deniedPermissions) {
        return !new AppFragmentTarget(fragment).shouldShowRationalePermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]));
    }

//    public static
//    @NonNull
//    RationaleDialog rationaleDialog(@NonNull Context context, Rationale rationale) {
//        return new RationaleDialog(context, rationale);
//    }
//
//    public static
//    @NonNull
//    SettingDialog defaultSettingDialog(@NonNull Activity activity, int requestCode) {
//        return new SettingDialog(activity, new SettingExecutor(new AppActivityTarget(activity), requestCode));
//    }
//
//    public static
//    @NonNull
//    SettingDialog defaultSettingDialog(@NonNull android.support.v4.app.Fragment fragment, int requestCode) {
//        return new SettingDialog(fragment.getActivity(), new SettingExecutor(new SupportFragmentTarget(fragment), requestCode));
//    }
//
//    public static
//    @NonNull
//    SettingDialog defaultSettingDialog(@NonNull android.app.Fragment fragment, int requestCode) {
//        return new SettingDialog(fragment.getActivity(), new SettingExecutor(new AppFragmentTarget(fragment), requestCode));
//    }
//
//    public static
//    @NonNull
//    SettingDialog defaultSettingDialog(@NonNull Context context) {
//        return new SettingDialog(context, new SettingExecutor(new ContextTarget(context), 0));
//    }
//
//    public static
//    @NonNull
//    SettingService defineSettingDialog(@NonNull Activity activity, int requestCode) {
//        return new SettingExecutor(new AppActivityTarget(activity), requestCode);
//    }
//
//    public static
//    @NonNull
//    SettingService defineSettingDialog(@NonNull android.support.v4.app.Fragment fragment, int requestCode) {
//        return new SettingExecutor(new SupportFragmentTarget(fragment), requestCode);
//    }
//
//    public static
//    @NonNull
//    SettingService defineSettingDialog(@NonNull android.app.Fragment fragment, int requestCode) {
//        return new SettingExecutor(new AppFragmentTarget(fragment), requestCode);
//    }
//
//    public static
//    @NonNull
//    SettingService defineSettingDialog(@NonNull Context context) {
//        return new SettingExecutor(new ContextTarget(context), 0);
//    }

    public
    @NonNull
    RationaleRequest with(@NonNull Activity activity) {
        return new PermissionRequest(new AppActivityTarget(activity));
    }

    public
    @NonNull
    RationaleRequest with(@NonNull android.support.v4.app.Fragment fragment) {
        return new PermissionRequest(new SupportFragmentTarget(fragment));
    }

    public
    @NonNull
    RationaleRequest with(@NonNull android.app.Fragment fragment) {
        return new PermissionRequest(new AppFragmentTarget(fragment));
    }

    public
    @NonNull
    Request with(@NonNull Context context) {
        return new PermissionRequest(new ContextTarget(context));
    }
}
