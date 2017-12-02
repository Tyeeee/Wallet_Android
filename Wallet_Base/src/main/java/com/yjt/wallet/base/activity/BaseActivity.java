package com.yjt.wallet.base.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yjt.wallet.base.R;
import com.yjt.wallet.base.toolbar.listener.OnLeftIconEventListener;
import com.yjt.wallet.base.toolbar.listener.OnRightIconEventListener;
import com.yjt.wallet.base.toolbar.listener.OnRightTextEventListener;
import com.yjt.wallet.base.toolbar.listener.OnTitleEventListener;
import com.yjt.wallet.components.constant.Constant;
import com.yjt.wallet.components.utils.GlideUtil;
import com.yjt.wallet.components.utils.InputUtil;
import com.yjt.wallet.components.utils.LogUtil;
import com.yjt.wallet.components.utils.SnackBarUtil;
import com.yjt.wallet.components.utils.ViewUtil;

import java.io.Serializable;
import java.util.HashMap;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar inToolbar;
    protected ImageView ivLeftIconEvent;
    protected TextView tvTitle;
    protected TextView tvRightTextEvent;
    protected ImageView ivRightIconEvent;

    protected void initializeToolbar(int toolbarColorId, int titleColorId, boolean isTitleClickable, String title, OnTitleEventListener onTitleEventListener) {
        initializeToolbar(toolbarColorId, false, Constant.View.DEFAULT_RESOURCE, null,
                          titleColorId, isTitleClickable, title, onTitleEventListener, Constant.View.DEFAULT_RESOURCE, null);
    }

    protected void initializeToolbar(int toolbarColorId, boolean hasLeftIconEvent, int leftIconId, OnLeftIconEventListener onLeftIconEventListener, int titleColorId, String title) {
        initializeToolbar(toolbarColorId, hasLeftIconEvent, leftIconId, onLeftIconEventListener,
                true, titleColorId, false, title, null,
                false, Constant.View.DEFAULT_RESOURCE, null, null,
                false, Constant.View.DEFAULT_RESOURCE, null);
    }

    protected void initializeToolbar(int toolbarColorId, boolean hasLeftIconEvent, int leftIconId, OnLeftIconEventListener onLeftIconEventListener
            , int titleColorId, boolean isTitleClickable, String title, OnTitleEventListener onTitleEventListener
            , int rightTextColorId, String rightText, OnRightTextEventListener onRightTextEventListener) {
        initializeToolbar(toolbarColorId, hasLeftIconEvent, leftIconId, onLeftIconEventListener,
                true, titleColorId, isTitleClickable, title, onTitleEventListener,
                true, rightTextColorId, rightText, onRightTextEventListener,
                false, Constant.View.DEFAULT_RESOURCE, null);
    }

    protected void initializeToolbar(int toolbarColorId, boolean hasLeftIconEvent, int leftIconId, OnLeftIconEventListener onLeftIconEventListener
            , int titleColorId, boolean isTitleClickable, String title, OnTitleEventListener onTitleEventListener
            , int rightIconId, final OnRightIconEventListener onRightIconEventListener) {
        initializeToolbar(toolbarColorId, hasLeftIconEvent, leftIconId, onLeftIconEventListener,
                true, titleColorId, isTitleClickable, title, onTitleEventListener,
                false, Constant.View.DEFAULT_RESOURCE, null, null,
                true, rightIconId, onRightIconEventListener);
    }

    @SuppressLint("NewApi")
    private void initializeToolbar(int toolbarColorId, boolean hasLeftIconEvent, int leftIconId, final OnLeftIconEventListener onLeftIconEventListener
            , boolean isShowTitle, int titleColorId, boolean isTitleClickable, String title, final OnTitleEventListener onTitleEventListener
            , boolean hasRightTextEvent, int rightTextColorId, String rightText, final OnRightTextEventListener onRightTextEventListener
            , boolean hasRightIconEvent, int rightIconId, final OnRightIconEventListener onRightIconEventListener) {
        if (inToolbar != null) {
            if (toolbarColorId != Constant.View.DEFAULT_RESOURCE) {
                inToolbar.setBackgroundColor(getResources().getColor(toolbarColorId));
            }
            if (hasLeftIconEvent) {
                if (ivLeftIconEvent == null) {
                    ivLeftIconEvent = ViewUtil.getInstance().findViewAttachOnclick(inToolbar, R.id.ivLeftIconEvent, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onLeftIconEventListener.OnLeftIconEvent();
                        }
                    });
                }
                if (leftIconId != Constant.View.DEFAULT_RESOURCE) {
                    GlideUtil.getInstance().with(this, leftIconId, null, null, DiskCacheStrategy.NONE, ivLeftIconEvent);
                    ViewUtil.getInstance().setViewVisible(ivLeftIconEvent);
                }
            } else {
                if (ivLeftIconEvent != null) {
                    ViewUtil.getInstance().setViewInvisible(ivLeftIconEvent);
                }
            }
            if (isShowTitle) {
                if (isTitleClickable) {
                    if (tvTitle == null) {
                        tvTitle = ViewUtil.getInstance().findViewAttachOnclick(inToolbar, R.id.tvTitle, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onTitleEventListener.onTitletEvent();
                            }
                        });
                    }
                } else {
                    if (tvTitle == null) {
                        tvTitle = ViewUtil.getInstance().findView(inToolbar, R.id.tvTitle);
                    }
                }
                tvTitle.setText(title);
                if (titleColorId != Constant.View.DEFAULT_RESOURCE) {
                    tvTitle.setTextColor(getResources().getColor(titleColorId));
                }
                ViewUtil.getInstance().setViewVisible(tvTitle);
            } else {
                if (tvTitle != null) {
                    ViewUtil.getInstance().setViewInvisible(tvTitle);
                    tvTitle.setText(null);
                }
            }
            if (hasRightTextEvent) {
                if (tvRightTextEvent == null) {
                    tvRightTextEvent = ViewUtil.getInstance().findViewAttachOnclick(inToolbar, R.id.tvRightTextEvent, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onRightTextEventListener.OnRightTextEvent();
                        }
                    });
                }
                ViewUtil.getInstance().setViewVisible(tvRightTextEvent);
                tvRightTextEvent.setText(rightText);
                if (rightTextColorId != Constant.View.DEFAULT_RESOURCE) {
                    tvRightTextEvent.setTextColor(getResources().getColor(rightTextColorId));
                }
            } else {
                if (tvRightTextEvent != null) {
                    ViewUtil.getInstance().setViewInvisible(tvRightTextEvent);
                    tvRightTextEvent.setText(null);
                }
            }
            if (hasRightIconEvent) {
                if (ivRightIconEvent == null) {
                    ivRightIconEvent = ViewUtil.getInstance().findViewAttachOnclick(inToolbar, R.id.ivRightIconEvent, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onRightIconEventListener.OnRightIconEvent();
                        }
                    });
                }
                if (leftIconId != Constant.View.DEFAULT_RESOURCE) {
                    GlideUtil.getInstance().with(this, rightIconId, null, null, DiskCacheStrategy.NONE, ivRightIconEvent);
                    ViewUtil.getInstance().setViewVisible(ivRightIconEvent);
                }
            } else {
                if (ivRightIconEvent != null) {
                    ViewUtil.getInstance().setViewInvisible(ivRightIconEvent);
                }
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onWindowFocusChanged() invoked!!");
        if (SnackBarUtil.getInstance().isShown()) {
            ViewUtil.getInstance().setSystemUiVisibility(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActivityUtil.add(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onPostCreate() invoked!!");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onRestart() invoked!!");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onPostResume() invoked!!");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setSavedInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        getSavedInstanceState(savedInstanceState);
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onRestoreInstanceState() invoked!!");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onDetachedFromWindow() invoked!!");
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onActivityReenter() invoked!!");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onTouchEvent() invoked!!");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                InputUtil.getInstance().hideKeyBoard(event, this);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " onBackPressed() invoked!!");
//        super.onBackPressed();
        onFinish("onBackPressed");
    }

    public void onFinish(String message) {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        LogUtil.getInstance().print("onFinish is called: " + message);
    }

    protected abstract void findViewById();

    protected abstract void initialize(Bundle savedInstanceState);

    protected abstract void setListener();

    protected abstract void getSavedInstanceState(Bundle savedInstanceState);

    protected abstract void setSavedInstanceState(Bundle savedInstanceState);

    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    protected void startActivity(String act) {
        startActivity(act, null);
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivity(Class<?> cls, String bundleName, Bundle bundle) {
        startActivity(cls, bundleName, bundle, null);
    }

    public void startActivity(Class<?> cls, String bundleName, Bundle bundle, Class entity) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (!TextUtils.isEmpty(bundleName) && bundle != null) {
            intent.putExtra(bundleName, bundle);
            if (entity != null) {
                intent.setExtrasClassLoader(entity.getClassLoader());
            }
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivity(String act, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(act);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivity(Class<?> cls, int flag) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.setFlags(flag);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivity(String action, int flag, Uri data, String type) {
        Intent intent = new Intent(action);
        intent.addFlags(flag);
        intent.setDataAndType(data, type);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, requestCode, null);
    }

    protected void startActivityForResult(String act, int requestCode) {
        startActivityForResult(act, requestCode, null);
    }

    protected void startActivityForResult(String act, int requestCode, Bundle bundle) {
        startActivityForResult(act, null, null, requestCode, bundle);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResult(String act, Uri data, int requestCode) {
        startActivityForResult(act, data, null, requestCode, null);
    }

    protected void startActivityForResult(String act, String type, int requestCode, Bundle bundle) {
        startActivityForResult(act, null, type, requestCode, bundle);
    }

    protected void startActivityForResult(String act, Uri data, int flags, String type, int requestCode) {
        startActivityForResult(act, data, flags, type, requestCode, null);
    }

    protected void startActivityForResultWithParcelable(String act, HashMap<String, Parcelable> map, int requestCode) {
        startActivityForResultWithParcelable(act, Constant.Permission.FILEPROVIDER_DEFAULT_FLAG, map, requestCode);
    }

    protected void startActivityForResultWithParcelable(String act, int flags, HashMap<String, Parcelable> map, int requestCode) {
        startActivityForResultWithParcelable(act, null, flags, null, map, requestCode);
    }

    protected void startActivityForResultWithParcelable(String act, Uri data, int flags, String type, HashMap<String, Parcelable> map, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(act);

        if (flags != Constant.Permission.FILEPROVIDER_DEFAULT_FLAG) {
            intent.addFlags(flags);
        }

        if (map != null) {
            for (String key : map.keySet()) {
                intent.putExtra(key, map.get(key));
            }
        }
        if (data != null && !TextUtils.isEmpty(type)) {
            intent.setDataAndType(data, type);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResultWithSerializable(String act, HashMap<String, Serializable> map, int requestCode) {
        startActivityForResultWithSerializable(act, null, null, map, requestCode);
    }

    protected void startActivityForResultWithSerializable(String act, Uri data, String type, HashMap<String, Serializable> map, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(act);
        if (map != null) {
            for (String key : map.keySet()) {
                intent.putExtra(key, map.get(key));
            }
        }
        if (data != null && !TextUtils.isEmpty(type)) {
            intent.setDataAndType(data, type);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResult(String act, Uri data, String type, int requestCode, Bundle bundle) {
        startActivityForResult(act, data, Constant.Permission.FILEPROVIDER_DEFAULT_FLAG, type, requestCode, bundle);
    }

    protected void startActivityForResult(String act, Uri data, int flags, String type, int requestCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(act);
        if (flags != Constant.Permission.FILEPROVIDER_DEFAULT_FLAG) {
            intent.addFlags(flags);
        }
        if (data != null && !TextUtils.isEmpty(type)) {
            intent.setDataAndType(data, type);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode, String bundleName, Bundle bundle) {
        startActivityForResult(cls, requestCode, bundleName, bundle, null);
    }

    public void startActivityForResult(Class<?> cls, int requestCode, String bundleName, Bundle bundle, Class entity) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (!TextUtils.isEmpty(bundleName) && bundle != null) {
            intent.putExtra(bundleName, bundle);
            if (entity != null) {
                intent.setExtrasClassLoader(entity.getClassLoader());
            }
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
}

