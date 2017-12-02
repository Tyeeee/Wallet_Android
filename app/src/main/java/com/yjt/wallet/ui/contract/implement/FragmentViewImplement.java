package com.yjt.wallet.ui.contract.implement;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.yjt.wallet.base.fragment.BaseFragment;
import com.yjt.wallet.base.view.BaseView;
import com.yjt.wallet.components.utils.LogUtil;

public abstract class FragmentViewImplement<T> extends BaseFragment implements BaseView<T> {

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
        if (this.getActivity() != null) {
            return (BaseView) this.getActivity();
        } else {
            return null;
        }
    }

    @Override
    public boolean isActivityFinish() {
        if (this.getActivity() != null) {
            return this.getActivity().isFinishing();
        } else {
            return true;
        }
    }

    @Override
    public void showNetWorkPromptDialog() {
        LogUtil.getInstance().print("showNetWorkPromptDialog");
    }

    @Override
    public void showPermissionPromptDialog() {
        LogUtil.getInstance().print("showPermissionPromptDialog");
    }

    @Override
    public void showLoadingPromptDialog(int resoutId, int requestCode) {
        LogUtil.getInstance().print("showLoadingPromptDialog");
    }

    @Override
    public void hideLoadingPromptDialog() {
        LogUtil.getInstance().print("hideLoadingPromptDialog");
    }

    @Override
    public synchronized void showPromptDialog(int resoutId, boolean cancelable, boolean cancelableOnTouchOutside, int requestCode) {
        LogUtil.getInstance().print("showPromptDialog");
    }

    @Override
    public synchronized void showPromptDialog(String prompt, boolean cancelable, boolean cancelableOnTouchOutside, int requestCode) {
        LogUtil.getInstance().print("showPromptDialog");
    }

    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void setSavedInstanceState(Bundle savedInstanceState) {
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
    }


    @Override
    public void startPermissionSettingActivity() {

    }

    @Override
    public void refusePermissionSetting() {

    }
}
