package com.yjt.wallet.ui.activity.presenter;

import android.content.Context;

import com.yjt.wallet.ui.contract.MainContract;
import com.yjt.wallet.ui.contract.implement.BasePresenterImplement;

public class MainPresenter extends BasePresenterImplement implements MainContract.Presenter {

    private MainContract.View view;

    public MainPresenter(Context context, MainContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void initialize() {
        super.initialize();
    }
}
