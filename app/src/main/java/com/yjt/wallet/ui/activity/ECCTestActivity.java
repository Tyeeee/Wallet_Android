package com.yjt.wallet.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.yjt.wallet.R;
import com.yjt.wallet.components.utils.LogUtil;
import com.yjt.wallet.components.utils.ViewUtil;
import com.yjt.wallet.core.ecc.BtcAddressGen;
import com.yjt.wallet.core.ecc.ECKeyPair;
import com.yjt.wallet.core.ecc.ECSignature;
import com.yjt.wallet.core.utils.Sha256Hash;
import com.yjt.wallet.ui.activity.presenter.ECCTestPresenter;
import com.yjt.wallet.ui.contract.ECCTestContract;
import com.yjt.wallet.ui.contract.implement.ActivityViewImplement;

import org.spongycastle.util.encoders.Hex;

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
//            Sha256Hash sha256Hash = Sha256Hash.create(Hex.decode("986ee148d0906f9335f2fe790154e7b260fdaed34fe1e3916f6d26f93a95a0f1"));
            Sha256Hash sha256Hash = Sha256Hash.create("Hello World...".getBytes());
            ECKeyPair keyPair = ECKeyPair.createECKeyPair(new BigInteger("3ed73981e3fc455a161de8fe872d34342e4b5207c8fc28e1dd35add63e92277a", 16), false);
//            ECKeyPair keyPair = ECKeyPair.createECKeyPair(false);
            LogUtil.getInstance().print(String.format("Private Key: %s", Hex.toHexString(keyPair.getPrivateKey().toByteArray())));
            LogUtil.getInstance().print(String.format("Public Key: %s", Hex.toHexString(keyPair.getPublicKey())));
            LogUtil.getInstance().print(String.format("Address: %s", BtcAddressGen.genBitcoinAddress(keyPair.getPublicKey())));
            ECSignature ecSignature = ECSignature.createSignature(keyPair.getPrivateKey(), sha256Hash);
            LogUtil.getInstance().print(String.format("Signature r: %s", Hex.toHexString(ecSignature.getR().toByteArray())));
            LogUtil.getInstance().print(String.format("Signature s: %s", Hex.toHexString(ecSignature.getS().toByteArray())));
//            LogUtil.getInstance().print(String.format("Signature: %s", ECSignature.createSignature(keyPair.getPrivateKey(), keyPair.getPublicKey(), "Hello World...", false)));
            LogUtil.getInstance().print(String.format("Signature has pass verified: %s", ECSignature.verifySignature(sha256Hash, new BigInteger("4ab054f05b1952ef6b2f96fffdccfc11133149d313d342a9128221fe7b61b05f",16), new BigInteger("06935eb3aa88ed863a6da963d9d96ad6b6ffc6f836625d956f2f2da172c7260a",16), keyPair.getPublicKey())));
//            LogUtil.getInstance().print(String.format("Signature verify: %s", ECSignature.verifySignature(sha256Hash, ecSignature.getR(), ecSignature.getS(), keyPair.getPublicKey())));
            tvPrivateKey.setText(String.format("Private Key: %s", Hex.toHexString(keyPair.getPrivateKey().toByteArray())));
            tvPublicKey.setText(String.format("Public Key: %s", Hex.toHexString(keyPair.getPublicKey())));
            tvAddress.setText(String.format("Address: %s", BtcAddressGen.genBitcoinAddress(keyPair.getPublicKey())));
//            tvSignature.setText(String.format("ECSignature: %s", ECSignature.createSignature(keyPair.getPrivateKey(), keyPair.getPublicKey(), "Hello World...", false)));
            tvSignature.setText(String.format("ECSignature: %s", Hex.toHexString(ecSignature.getR().toByteArray()) + Hex.toHexString(ecSignature.getR().toByteArray())));
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
