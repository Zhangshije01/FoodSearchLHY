package com.yang.foodsearch.rx;

import android.app.Activity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.yang.foodsearch.R;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

/**
 * @author wangwei on 2016/10/13
 *         wangwei@jiandaola.com
 */

public class ActionBarDrawerToggleOnSubscribe implements Observable.OnSubscribe<Boolean> {

    private final DrawerLayout mDrawerLayout;
    private final Toolbar mToolbar;
    private Activity mActivity = null;


    public ActionBarDrawerToggleOnSubscribe(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        mToolbar = toolbar;
        mActivity = activity;
    }

    @Override
    public void call(final Subscriber<? super Boolean> subscriber) {
        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(mActivity,
                mDrawerLayout, mToolbar, R.string.nav_open, R.string.nav_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(true);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(false);
                }
            }
        };
        drawerToggle.syncState();
        mDrawerLayout.addDrawerListener(drawerToggle);
        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                mDrawerLayout.setDrawerListener(null);
                mDrawerLayout.removeDrawerListener(drawerToggle);
                mActivity = null;
            }
        });

        // Emit initial value.
        subscriber.onNext(mDrawerLayout.isDrawerOpen(GravityCompat.START));
    }
}
