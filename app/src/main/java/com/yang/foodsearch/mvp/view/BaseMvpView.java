package com.yang.foodsearch.mvp.view;

import android.content.Context;

/**
 * Created by admin on 17/4/12.
 */

public interface BaseMvpView<T> {
    void attachPresenter(T presenter);
    Context getContext();
}
