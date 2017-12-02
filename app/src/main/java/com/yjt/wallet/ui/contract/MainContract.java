package com.yjt.wallet.ui.contract;

import com.yjt.wallet.base.presenter.BasePresenter;
import com.yjt.wallet.base.view.BaseView;

public interface MainContract {

    interface View extends BaseView<Presenter> {

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

    }
}
