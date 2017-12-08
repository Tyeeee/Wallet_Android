package com.yjt.wallet.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yjt.wallet.R;
import com.yjt.wallet.components.utils.ViewUtil;
import com.yjt.wallet.model.ECCTest;
import com.yjt.wallet.ui.activity.presenter.ECCTestPresenter;
import com.yjt.wallet.ui.contract.ECCTestContract;
import com.yjt.wallet.ui.contract.implement.ActivityViewImplement;

public class ECCTestActivity extends ActivityViewImplement<ECCTestContract.Presenter> implements ECCTestContract.View, View.OnClickListener {

    private ECCTestPresenter eccTestPresenter;

    private EditText etData;
    private Button btnTest;
    private TextView tvPrivateKey;
    private TextView tvPublicKey;
    private TextView tvAddress;
    private TextView tvSignature;
    private TextView tvSignatureResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecc_test);
        findViewById();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        etData = ViewUtil.getInstance().findView(this, R.id.etData);
        btnTest = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.btnTest, this);
        tvPrivateKey = ViewUtil.getInstance().findView(this, R.id.tvPrivateKey);
        tvPublicKey = ViewUtil.getInstance().findView(this, R.id.tvPublicKey);
        tvAddress = ViewUtil.getInstance().findView(this, R.id.tvAddress);
        tvSignature = ViewUtil.getInstance().findView(this, R.id.tvSignature);
        tvSignatureResult = ViewUtil.getInstance().findView(this, R.id.tvSignatureResult);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        eccTestPresenter = new ECCTestPresenter(this, this);
        eccTestPresenter.initialize();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTest:
                eccTestPresenter.doTest(etData.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        switch (requestCode) {
            default:
                break;
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        switch (requestCode) {
            default:
                break;
        }
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void showTestData(ECCTest eccTest) {
        tvPrivateKey.setText(String.format("Private Key: %s", eccTest.getPrivateKey()));
        tvPublicKey.setText(String.format("Public Key: %s", eccTest.getPublicKey()));
        tvAddress.setText(String.format("Address: %s", eccTest.getAddress()));
        tvSignature.setText(String.format("Signature: %s", eccTest.getSignature()));
        tvSignatureResult.setText(String.format("Signature Result: %s", eccTest.getSignatureResult()));
    }
}
