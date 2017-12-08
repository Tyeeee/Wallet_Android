package com.yjt.wallet.ui.activity.presenter;

import android.content.Context;
import android.os.Message;
import android.widget.Toast;

import com.yjt.wallet.BuildConfig;
import com.yjt.wallet.base.handler.ActivityHandler;
import com.yjt.wallet.components.utils.LogUtil;
import com.yjt.wallet.components.utils.MessageUtil;
import com.yjt.wallet.components.utils.ThreadPoolUtil;
import com.yjt.wallet.components.utils.ToastUtil;
import com.yjt.wallet.constant.Constant;
import com.yjt.wallet.core.ecc.BtcAddressGen;
import com.yjt.wallet.core.ecc.ECKeyPair;
import com.yjt.wallet.core.ecc.ECSignature;
import com.yjt.wallet.model.ECCTest;
import com.yjt.wallet.ui.activity.ECCTestActivity;
import com.yjt.wallet.ui.contract.ECCTestContract;
import com.yjt.wallet.ui.contract.implement.BasePresenterImplement;

import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;
import java.security.SignatureException;

public class ECCTestPresenter extends BasePresenterImplement implements ECCTestContract.Presenter {

    private ECCTestContract.View view;
    private ECCTestHandler eccTestHandler;

    private class ECCTestHandler extends ActivityHandler<ECCTestActivity> {

        public ECCTestHandler(ECCTestActivity activity) {
            super(activity);
        }

        @Override
        protected void handleMessage(ECCTestActivity activity, Message msg) {
            if (activity != null) {
                switch (msg.what) {
                    case Constant.StateCode.TEST_SUCCESS:
                        ECCTest eccTest = (ECCTest) msg.obj;
                        LogUtil.getInstance().print(String.format("Private Key: %s", eccTest.getPrivateKey()));
                        LogUtil.getInstance().print(String.format("Public Key: %s", eccTest.getPublicKey()));
                        LogUtil.getInstance().print(String.format("Address: %s", eccTest.getAddress()));
//                        LogUtil.getInstance().print(String.format("Signature[r]: %s", eccTest.getSignatureR()));
//                        LogUtil.getInstance().print(String.format("Signature[s]: %s", eccTest.getSignatureS()));
                        LogUtil.getInstance().print(String.format("Signature: %s", eccTest.getSignature()));
                        LogUtil.getInstance().print(String.format("Signature Result: %s", eccTest.getSignatureResult()));
                        view.showTestData(eccTest);
                        break;
                    case Constant.StateCode.TEST_FAILED:
                        ToastUtil.getInstance().showToast(activity, msg.obj.toString(), Toast.LENGTH_SHORT);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public ECCTestPresenter(Context context, ECCTestContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
        eccTestHandler = new ECCTestHandler((ECCTestActivity) view);
    }

    @Override
    public void getSecurityProviders() {
        if (BuildConfig.DEBUG) {
            ThreadPoolUtil.execute(new Runnable() {
                @Override
                public void run() {
                    for (Provider provider : Security.getProviders()) {
                        LogUtil.getInstance().print(String.format("Provider: %s, Version: %s", provider.getName(), provider.getVersion()));
                        for (Provider.Service service : provider.getServices()) {
                            LogUtil.getInstance().print(String.format("Type: %-30s, Algorithm: %-30s", service.getType(), service.getAlgorithm()));
                        }
                    }
                }
            });
        }
    }

    @Override
    public void doTest(final String data) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //        eccTestPresenter.getSecurityProviders();
                    ECCTest eccTest = new ECCTest();
                    ECKeyPair keyPair = ECKeyPair.generateECKeyPair(new BigInteger("3ed73981e3fc455a161de8fe872d34342e4b5207c8fc28e1dd35add63e92277a", 16), false);
//            ECKeyPair keyPair = ECKeyPair.generateECKeyPair(false);
                    eccTest.setPrivateKey(Hex.toHexString(keyPair.getPrivateKey().toByteArray()));
                    eccTest.setPublicKey(Hex.toHexString(keyPair.getPublicKey()));
                    eccTest.setAddress(BtcAddressGen.genBitcoinAddress(keyPair.getPublicKey()));
                    String signature = Hex.toHexString(ECSignature.getInstance().generateSignature(keyPair.getPrivateKey(), keyPair.getPublicKey(), data, false, true));
                    eccTest.setSignature(signature);
                    eccTest.setSignatureResult(ECSignature.getInstance().verifySignature(data, signature, keyPair.getPublicKey()));
                    eccTestHandler.sendMessage(MessageUtil.getMessage(Constant.StateCode.TEST_SUCCESS, eccTest));
                } catch (NoSuchProviderException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | IOException | SignatureException e) {
                    eccTestHandler.sendMessage(MessageUtil.getMessage(Constant.StateCode.TEST_FAILED, e));
                    e.printStackTrace();
                }
            }
        });
    }
}
