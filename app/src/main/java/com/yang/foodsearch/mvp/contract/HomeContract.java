package com.yang.foodsearch.mvp.contract;


import android.app.ProgressDialog;
import android.content.Context;

import com.yang.foodsearch.mvp.precenter.BasePresenter;
import com.yang.foodsearch.mvp.view.BaseMvpView;

/**
 * Created by admin on 17/4/12.
 */

public interface HomeContract {
    interface Presenter extends BasePresenter {
        void showName();
        void commitRegistDetail(Context context, ProgressDialog progressDialog,String userName, String userPassword);

    }
    interface MvpView extends BaseMvpView<Presenter> {

    }
}
