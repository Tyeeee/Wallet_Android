package com.yjt.wallet.ui.contract;

import com.yjt.wallet.base.presenter.BasePresenter;
import com.yjt.wallet.base.view.BaseView;
import com.yjt.wallet.model.ECCTest;

public interface ECCTestContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();

        void showTestData(ECCTest eccTest);
    }

    interface Presenter extends BasePresenter {

        void getSecurityProviders();
        
        void doTest(String data);
    }
}
