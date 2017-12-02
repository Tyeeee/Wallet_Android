package com.yjt.wallet.ui.contract.implement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.yjt.wallet.R;
import com.yjt.wallet.base.activity.BaseActivity;
import com.yjt.wallet.base.application.BaseApplication;
import com.yjt.wallet.base.view.BaseView;
import com.yjt.wallet.components.constant.Regex;
import com.yjt.wallet.components.utils.ActivityUtil;
import com.yjt.wallet.components.utils.LogUtil;
import com.yjt.wallet.components.utils.ViewUtil;
import com.yjt.wallet.constant.Constant;
import com.yjt.wallet.constant.Temp;
import com.yjt.wallet.ui.dialog.ProgressDialog;
import com.yjt.wallet.ui.dialog.PromptDialog;


public abstract class ActivityViewImplement<T> extends BaseActivity implements BaseView<T> {

    private DialogFragment dialogFragment;
    private BasePresenterImplement basePresenterImplement;

    @Override
    public void setPresenter(@NonNull T presenter) {
    }

    public BasePresenterImplement getBasePresenterImplement() {
        return basePresenterImplement;
    }

    public void setBasePresenterImplement(BasePresenterImplement basePresenterImplement) {
        this.basePresenterImplement = basePresenterImplement;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public boolean isActivityFinish() {
        return this.isFinishing();
    }

    @Override
    public void showNetWorkPromptDialog() {
        if (!isActivityFinish()) {
            if (dialogFragment != null) {
                ViewUtil.getInstance().hideDialog(dialogFragment);
            }
            dialogFragment = PromptDialog.createBuilder(getSupportFragmentManager())
                    .setTitle(getString(R.string.dialog_prompt))
                    .setPrompt(getString(R.string.dialog_prompt_net_work_error))
                    .setPositiveButtonText(this, R.string.dialog_prompt_setting)
                    .setNegativeButtonText(this, R.string.dialog_prompt_cancel)
                    .setRequestCode(Constant.RequestCode.DIALOG_PROMPT_SET_NET_WORK)
                    .showAllowingStateLoss(this);
        }
    }

    @Override
    public void showPermissionPromptDialog() {
        if (!isActivityFinish()) {
            PromptDialog.createBuilder(getSupportFragmentManager())
                    .setTitle(getString(R.string.dialog_prompt))
                    .setPrompt(getString(R.string.dialog_prompt_permission_error))
                    .setPositiveButtonText(this, R.string.dialog_prompt_setting)
                    .setNegativeButtonText(this, R.string.dialog_prompt_cancel)
                    .setRequestCode(Constant.RequestCode.DIALOG_PROMPT_SET_PERMISSION)
                    .setCancelableOnTouchOutside(false)
                    .setCancelable(false)
                    .showAllowingStateLoss(this);
        }
    }

    @Override
    public void showLoadingPromptDialog(int resoutId, int requestCode) {
        if (!isActivityFinish()) {
            if (dialogFragment != null) {
                ViewUtil.getInstance().hideDialog(dialogFragment);
            }
            dialogFragment = ProgressDialog.createBuilder(getSupportFragmentManager())
                    .setPrompt(getString(resoutId))
                    .setCancelableOnTouchOutside(false)
                    .setCancelable(false)
                    .setRequestCode(requestCode)
                    .showAllowingStateLoss(this);
        }
    }

    @Override
    public void hideLoadingPromptDialog() {
        if (!isActivityFinish()) {
            ViewUtil.getInstance().hideDialog(dialogFragment);
        }
    }

    @Override
    public synchronized void showPromptDialog(int resoutId, boolean cancelable, boolean cancelableOnTouchOutside, int requestCode) {
        if (!isActivityFinish()) {
            if (dialogFragment != null) {
                ViewUtil.getInstance().hideDialog(dialogFragment);
            }
            dialogFragment = PromptDialog.createBuilder(getSupportFragmentManager())
                    .setTitle(getString(R.string.dialog_prompt))
                    .setPrompt(getString(resoutId))
                    .setPositiveButtonText(this, R.string.dialog_prompt_known)
                    .setCancelable(cancelable)
                    .setCancelableOnTouchOutside(cancelableOnTouchOutside)
                    .setRequestCode(requestCode)
                    .showAllowingStateLoss(this);
        }
    }

    @Override
    public synchronized void showPromptDialog(String prompt, boolean cancelable, boolean cancelableOnTouchOutside, int requestCode) {
        if (!isActivityFinish()) {
            if (dialogFragment != null) {
                ViewUtil.getInstance().hideDialog(dialogFragment);
            }
            dialogFragment = PromptDialog.createBuilder(getSupportFragmentManager())
                    .setTitle(getString(R.string.dialog_prompt))
                    .setPrompt(prompt)
                    .setPositiveButtonText(this, R.string.dialog_prompt_known)
                    .setCancelable(cancelable)
                    .setCancelableOnTouchOutside(cancelableOnTouchOutside)
                    .setRequestCode(requestCode)
                    .showAllowingStateLoss(this);
        }
    }

    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " getSavedInstanceState() invoked!!");
        if (savedInstanceState != null) {
            BaseApplication.getInstance().setEncryptKey(savedInstanceState.getString(Temp.ENCRYPT_KEY.getContent()));
        }
    }

    @Override
    protected void setSavedInstanceState(Bundle savedInstanceState) {
        LogUtil.getInstance().print(this.getClass().getSimpleName() + " setSavedInstanceState() invoked!!");
        if (savedInstanceState != null) {
            savedInstanceState.putString(Temp.ENCRYPT_KEY.getContent(), BaseApplication.getInstance().getEncryptKey());
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
    }


    @Override
    public void startPermissionSettingActivity() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(Regex.PACKAGE.getRegext() + getPackageName()));
        startActivityForResult(intent, Constant.RequestCode.PREMISSION_SETTING);
    }

    @Override
    public void refusePermissionSetting() {
        BaseApplication.getInstance().releaseInstance();
        ActivityUtil.removeAll();
    }
}
