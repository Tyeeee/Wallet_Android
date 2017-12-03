package com.yjt.wallet.ui.activity.presenter;

import android.content.Context;

import com.yjt.wallet.ui.contract.ECCTestContract;
import com.yjt.wallet.ui.contract.MainContract;
import com.yjt.wallet.ui.contract.implement.BasePresenterImplement;

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
}
