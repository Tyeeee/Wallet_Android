package com.yjt.wallet.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.yjt.wallet.R;
import com.yjt.wallet.components.utils.LogUtil;
import com.yjt.wallet.components.utils.ViewUtil;
import com.yjt.wallet.core.ecc.BtcAddressGen;
import com.yjt.wallet.core.ecc.ECKeyPair;
import com.yjt.wallet.ui.activity.presenter.ECCTestPresenter;
import com.yjt.wallet.ui.contract.ECCTestContract;
import com.yjt.wallet.ui.contract.implement.ActivityViewImplement;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class ECCTestActivity extends ActivityViewImplement<ECCTestContract.Presenter> implements ECCTestContract.View {

    private ECCTestPresenter eccTestPresenter;

    private TextView tvPrivateKey;
    private TextView tvPublicKey;
    private TextView tvAddress;
    private TextView tvSignature;

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
        tvPrivateKey = ViewUtil.getInstance().findView(this, R.id.tvPrivateKey);
        tvPublicKey = ViewUtil.getInstance().findView(this, R.id.tvPublicKey);
        tvAddress = ViewUtil.getInstance().findView(this, R.id.tvAddress);
        tvSignature = ViewUtil.getInstance().findView(this, R.id.tvSignature);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        eccTestPresenter = new ECCTestPresenter(this, this);
//        eccTestPresenter.getSecurityProviders();
        try {
            ECKeyPair keyPair = ECKeyPair.createECKeyPair(new BigInteger("6a6361877474b0939abd686792012fa894cbe4177882d2c69ce758a27c87009e", 16), false);
//            ECKeyPair keyPair = ECKeyPair.createECKeyPair(false);
            LogUtil.getInstance().print(String.format("Private Key: %s", keyPair.getPrivateKey()));
            LogUtil.getInstance().print(String.format("Public Key: %s", keyPair.getPublicKey()));
            LogUtil.getInstance().print(String.format("Address: %s", BtcAddressGen.genBitcoinAddress(keyPair.getPublicKey())));
            tvPrivateKey.setText(String.format("Private Key: %s", keyPair.getPrivateKey()));
            tvPublicKey.setText(String.format("Public Key: %s", keyPair.getPublicKey()));
            tvAddress.setText(String.format("Address: %s", BtcAddressGen.genBitcoinAddress(keyPair.getPublicKey())));
        } catch (NoSuchProviderException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setListener() {

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
}
