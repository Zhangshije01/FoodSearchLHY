package com.yang.foodsearch.util;

import android.text.TextUtils;
import android.widget.Toast;

import com.yang.foodsearch.application.FoodSearchApplication;

/**
 * Created by 蜗牛 on 2017-04-16.
 */
public class ToastUtils {
    private static Toast showingToast = null;
    public static void showToast(CharSequence text) {
        showToast(text, -1);
    }

    public static void showToast(final CharSequence text, final int gravity) {
        FoodSearchApplication.getInstance().getUIHandler().post(new Runnable() {
            public void run() {
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                if (null != showingToast) {
                    showingToast.cancel();
                }
                /*
                 * 鏍规嵁鏂囨湰闀跨煭鍐冲畾鏄剧ず鐨勬椂闂撮暱搴︺€?
                 */
                final int duration;
                if (text.length() <= 15) {
                    duration = Toast.LENGTH_SHORT;
                } else {
                    duration = Toast.LENGTH_LONG;
                }

                /*
                 * 濡傛灉瀛樺湪瀹炰緥鍒欐洿鏀规枃鏈户缁樉绀猴紝鍚﹀垯鍒涘缓鏂扮殑瀵硅薄銆?
                 */
                showingToast = Toast.makeText(FoodSearchApplication.getInstance(), text, duration);
                if (gravity > 0) {
                    showingToast.setGravity(gravity, 0, 0);
                }
                showingToast.show();
            }
        });
    }


    public static void showToast(int text) {
        if (0 == text) {
            showToast(null);
        } else {
            showToast(FoodSearchApplication.getInstance().getText(text));
        }
    }


}
