package com.yjt.wallet.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.yjt.wallet.R;
import com.yjt.wallet.components.utils.LogUtil;
import com.yjt.wallet.components.utils.ViewUtil;
import com.yjt.wallet.core.ecc.BtcAddressGen;
import com.yjt.wallet.core.ecc.ECKeyPair;
import com.yjt.wallet.core.ecc.ECSignature;
import com.yjt.wallet.ui.activity.presenter.ECCTestPresenter;
import com.yjt.wallet.ui.contract.ECCTestContract;
import com.yjt.wallet.ui.contract.implement.ActivityViewImplement;

import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

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
            String data = "Hello World...";
//            Sha256Hash sha256Hash = Sha256Hash.create(Hex.decode("986ee148d0906f9335f2fe790154e7b260fdaed34fe1e3916f6d26f93a95a0f1"));
            ECKeyPair keyPair = ECKeyPair.generateECKeyPair(new BigInteger("3ed73981e3fc455a161de8fe872d34342e4b5207c8fc28e1dd35add63e92277a", 16), false);
//            ECKeyPair keyPair = ECKeyPair.generateECKeyPair(false);
            LogUtil.getInstance().print(String.format("Private Key: %s", Hex.toHexString(keyPair.getPrivateKey().toByteArray())));
            LogUtil.getInstance().print(String.format("Public Key: %s", Hex.toHexString(keyPair.getPublicKey())));
            LogUtil.getInstance().print(String.format("Address: %s", BtcAddressGen.genBitcoinAddress(keyPair.getPublicKey())));
//            ECSignature ecSignature = ECSignature.generateSignature(keyPair.getPrivateKey(), Sha256Hash.create(data.getBytes()),false);
//            LogUtil.getInstance().print(String.format("Signature[r]: %s", Hex.toHexString(ecSignature.getR().toByteArray())));
//            LogUtil.getInstance().print(String.format("Signature[s]: %s", Hex.toHexString(ecSignature.getS().toByteArray())));
//            LogUtil.getInstance().print(String.format("Signature has pass verified: %s", ECSignature.verifySignature(Sha256Hash.create(data.getBytes()), ecSignature.getR(), ecSignature.getS(), keyPair.getPublicKey(), false)));
            String signature = ECSignature.generateSignature(keyPair.getPrivateKey(), keyPair.getPublicKey(), data, false, true);
            LogUtil.getInstance().print(String.format("Signature: %s", signature));
            LogUtil.getInstance().print(String.format("Signature verify: %s", ECSignature.verifySignature(data, signature, keyPair.getPublicKey())));
            tvPrivateKey.setText(String.format("Private Key: %s", Hex.toHexString(keyPair.getPrivateKey().toByteArray())));
            tvPublicKey.setText(String.format("Public Key: %s", Hex.toHexString(keyPair.getPublicKey())));
            tvAddress.setText(String.format("Address: %s", BtcAddressGen.genBitcoinAddress(keyPair.getPublicKey())));
//            tvSignature.setText(String.format("ECSignature: %s", Hex.toHexString(ecSignature.getR().toByteArray()) + Hex.toHexString(ecSignature.getR().toByteArray())));
            tvSignature.setText(String.format("ECSignature: %s", signature));
        } catch (NoSuchProviderException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | IOException | SignatureException e) {
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
