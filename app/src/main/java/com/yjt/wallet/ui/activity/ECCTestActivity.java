package com.yjt.wallet.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yjt.wallet.R;
import com.yjt.wallet.components.utils.LogUtil;
import com.yjt.wallet.components.utils.ViewUtil;
import com.yjt.wallet.constant.Constant;
import com.yjt.wallet.ecc.BtcAddressGen;
import com.yjt.wallet.ecc.ECKeyPair;
import com.yjt.wallet.ecc.EthAddressGen;
import com.yjt.wallet.ui.activity.presenter.ECCTestPresenter;
import com.yjt.wallet.ui.activity.presenter.MainPresenter;
import com.yjt.wallet.ui.contract.ECCTestContract;
import com.yjt.wallet.ui.contract.MainContract;
import com.yjt.wallet.ui.contract.implement.ActivityViewImplement;

import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.util.PrivateKeyFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.ECPrivateKeySpec;
import java.util.Arrays;

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
        byte[]     privateKeyBytes = "8F72F6B29E6E225A36B68DFE333C7CE5E55D83249D3D2CD6332671FA445C4DD3".getBytes();
        BigInteger privateKey      = new BigInteger(1, Arrays.copyOfRange(privateKeyBytes, 1, privateKeyBytes.length));
        try {
            //Get secp256k1 pair - which we can use for both addresses
            ECKeyPair keyPair = ECKeyPair.createECKeyPair();
            EthAddressGen.genEthereumAddress(keyPair.getPublicKey());
            LogUtil.getInstance().print(String.format("Private Key: %", keyPair.getPrivateKey()));
            LogUtil.getInstance().print(String.format("Public Key: %", keyPair.getPublicKey()));
            tvPrivateKey.setText(String.format("Private Key: %", keyPair.getPrivateKey()));
            tvPrivateKey.setText(String.format("Public Key: %", keyPair.getPublicKey()));
        } catch (NoSuchProviderException | InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
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
