package com.yjt.wallet.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yjt.wallet.R;
import com.yjt.wallet.components.utils.ViewUtil;
import com.yjt.wallet.constant.Constant;
import com.yjt.wallet.ui.activity.presenter.MainPresenter;
import com.yjt.wallet.ui.contract.MainContract;
import com.yjt.wallet.ui.contract.implement.ActivityViewImplement;

public class MainActivity extends ActivityViewImplement<MainContract.Presenter> implements MainContract.View, View.OnClickListener {

    private MainPresenter mainPresenter;

    private Button btnRegist;
    private Button btnLogin;
    private Button generateWallet;
    private Button scanIntegral;
    private Button transferIntegral;
    private Button queryIntegral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        btnRegist = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.btnRegist, this);
        btnLogin = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.btnLogin, this);
        generateWallet = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.generateWallet, this);
        scanIntegral = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.scanIntegral, this);
        transferIntegral = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.transferIntegral, this);
        queryIntegral = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.queryIntegral, this);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        showPromptDialog(R.string.dialog_prompt_wallet, false, false, Constant.RequestCode.DIALOG_PROGRESS_WALLET);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_PROGRESS_WALLET:
                break;
            default:
                break;
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_PROGRESS_WALLET:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegist:
                break;
            case R.id.btnLogin:
                break;
            case R.id.generateWallet:
                break;
            case R.id.scanIntegral:
                break;
            case R.id.transferIntegral:
                break;
            case R.id.queryIntegral:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isActive() {
        return false;
    }
}
