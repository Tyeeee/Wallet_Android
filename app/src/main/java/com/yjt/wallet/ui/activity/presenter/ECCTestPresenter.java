package com.yjt.wallet.ui.activity.presenter;

import android.content.Context;

import com.yjt.wallet.BuildConfig;
import com.yjt.wallet.components.utils.LogUtil;
import com.yjt.wallet.components.utils.ThreadPoolUtil;
import com.yjt.wallet.ui.contract.ECCTestContract;
import com.yjt.wallet.ui.contract.implement.BasePresenterImplement;

import java.security.Provider;
import java.security.Security;

public class ECCTestPresenter extends BasePresenterImplement implements ECCTestContract.Presenter {

    private ECCTestContract.View view;

    public ECCTestPresenter(Context context, ECCTestContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
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
}
