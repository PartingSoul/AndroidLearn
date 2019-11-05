package com.parting_soul.support.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author parting_soul
 * @date 2018/2/24
 */

public class ScreenUtils {
    /**
     * 获取屏幕的宽
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕的高
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

}
