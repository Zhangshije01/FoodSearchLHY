package com.yang.foodsearch.rx;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import rx.Observable;

/**
 * @author wangwei on 2016/10/13
 *         wangwei@jiandaola.com
 */

public final class RxActionBarDrawerToggle {

    public static Observable<Boolean> drawerOpen(@NonNull Activity activity, @NonNull DrawerLayout drawerLayout, @NonNull Toolbar toolbar) {
        return Observable.create(new ActionBarDrawerToggleOnSubscribe(activity, drawerLayout, toolbar));
    }
}
