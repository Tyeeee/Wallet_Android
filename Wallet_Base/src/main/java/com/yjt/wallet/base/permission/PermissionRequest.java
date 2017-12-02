package com.yjt.wallet.base.permission;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;

import com.yjt.wallet.base.constant.Temp;
import com.yjt.wallet.components.permission.annotation.PermissioDenied;
import com.yjt.wallet.components.permission.annotation.PermissionGrant;
import com.yjt.wallet.components.permission.listener.PermissionCallback;
import com.yjt.wallet.components.permission.listener.PermissionListener;
import com.yjt.wallet.components.permission.listener.Rationale;
import com.yjt.wallet.components.permission.listener.RationaleListener;
import com.yjt.wallet.components.permission.listener.RationaleRequest;
import com.yjt.wallet.components.permission.listener.Request;
import com.yjt.wallet.components.permission.listener.Target;
import com.yjt.wallet.components.utils.LogUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class PermissionRequest implements Request<RationaleRequest>, RationaleRequest, PermissionListener, Rationale {

    private Target target;
    private int requestCode;
    private String[] permissions;
    private Object callback;
    private RationaleListener rationaleListener;
    private String[] deniedPermissions;

    PermissionRequest(Target target) {
        if (target == null) {
            throw new IllegalArgumentException("The target can not be null.");
        }
        this.target = target;
    }

    @NonNull
    @Override
    public RationaleRequest permission(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    @NonNull
    @Override
    public RationaleRequest requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    @Override
    public RationaleRequest callback(Object callback) {
        this.callback = callback;
        return this;
    }

    @NonNull
    @Override
    public RationaleRequest rationale(RationaleListener listener) {
        this.rationaleListener = listener;
        return this;
    }

    @Deprecated
    @Override
    public void send() {
        start();
    }

    @Override
    public void start() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callbackSuccess();
        } else {
            deniedPermissions = getDeniedPermissions(target.getContext(), permissions);
            if (deniedPermissions.length > 0) {
                if (target.shouldShowRationalePermissions(deniedPermissions) && rationaleListener != null) {
                    rationaleListener.showRequestPermissionRationale(requestCode, this);
                } else {
                    resume();
                }
            } else {
                callbackSuccess();
            }
        }
    }

    private String[] getDeniedPermissions(Context context, @NonNull String... permissions) {
        List<String> deniedList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedList.add(permission);
            }
        }
        return deniedList.toArray(new String[deniedList.size()]);
    }

    @Override
    public void cancel() {
        int[] results = new int[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            results[i] = ContextCompat.checkSelfPermission(target.getContext(), permissions[i]);
        }
        onRequestPermissionsResult(permissions, results);
    }

    @Override
    public void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantPermissions) {
        LogUtil.getInstance().print("onRequestPermissionsResult");
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantPermissions[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (deniedPermissions.isEmpty()) {
            callbackSuccess();
        } else {
            callbackFailed(deniedPermissions);
        }
    }

    private void callbackSuccess() {
        if (callback != null) {
            if (callback instanceof PermissionCallback)
                ((PermissionCallback) callback).onSuccess(requestCode, Arrays.asList(permissions));
            else {
                callbackAnnotation(callback, requestCode, PermissionGrant.class, Arrays.asList(permissions));
            }
        }
    }

    private void callbackFailed(List<String> deniedPermissions) {
        if (callback != null) {
            if (callback instanceof PermissionCallback)
                ((PermissionCallback) callback).onFailed(requestCode, deniedPermissions);
            else {
                callbackAnnotation(callback, requestCode, PermissioDenied.class, deniedPermissions);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void callbackAnnotation(Object callback, int requestCode, Class<? extends Annotation> annotationClass, List<String> permissions) {
        Method[] methods = findMethodForRequestCode(callback.getClass(), annotationClass, requestCode);
        if (methods.length == 0) {
            LogUtil.getInstance().print("Do you forget @PermissionGrant or @PermissioDenied for callback method ?");
        } else
            try {
                for (Method method : methods) {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    method.invoke(callback, permissions);
//                    method.invoke(callback, new Object[]{permissions});
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
    }

    private static Method[] findMethodForRequestCode(@NonNull Class<?> source, @NonNull Class<? extends Annotation> annotation, int requestCode) {
        List<Method> methods = new ArrayList<>(1);
        for (Method method : source.getDeclaredMethods()) {
            if (method.getAnnotation(annotation) != null) {
                if (isSameRequestCode(method, annotation, requestCode)) {
                    methods.add(method);
                }
            }
        }
        return methods.toArray(new Method[methods.size()]);
    }

    private static boolean isSameRequestCode(@NonNull Method method, @NonNull Class<? extends Annotation> annotation, int requestCode) {
        if (PermissionGrant.class.equals(annotation)) {
            return method.getAnnotation(PermissionGrant.class).value() == requestCode;
        } else if (PermissioDenied.class.equals(annotation)) {
            return method.getAnnotation(PermissioDenied.class).value() == requestCode;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void resume() {
        LogUtil.getInstance().print("PermissionRequest resume()");
        PermissionActivity.permissionListener = this;
        Intent intent = new Intent(target.getContext(), PermissionActivity.class);
        intent.putExtra(Temp.PERMISSIONS.getContent(), deniedPermissions);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        target.startActivity(intent);
    }
}