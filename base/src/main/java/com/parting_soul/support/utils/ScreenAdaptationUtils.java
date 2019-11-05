package com.parting_soul.support.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * 屏幕适配工具类
 * 使用UI设计图设计的宽度实现不同分辨率屏幕的自适应
 *
 * @author parting_soul
 * @date 2018/7/12
 */
public class ScreenAdaptationUtils {

    /**
     * 原始的字体和像素密度
     */
    private static float sOriginalDensity;
    private static float sOriginalScaledDensity;

    /**
     * 设计图宽度(单位dp)，默认宽度为360dp
     */
    private static int sDesignSize = 360;

    /**
     * 根据设计图(宽度)设置屏幕密度
     * PX = DP * DENSITY
     * DENSITY = sqrt(width^2+height^2)/(屏幕尺寸 * 160 dpi)
     *
     * @param application
     */
    public static void setCustomDensity(Activity activity, final Application application) {
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (sOriginalDensity == 0) {
            sOriginalDensity = appDisplayMetrics.density;
            sOriginalScaledDensity = appDisplayMetrics.scaledDensity;
            //当改变系统字体回调该方法重新设置原始的字体密度
            activity.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sOriginalScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }


        float density = appDisplayMetrics.widthPixels * 1.0f / sDesignSize;
        //字体的密度根据原始字体密度和原始像素密度的比例来计算
        float scaleDensity = density * (sOriginalScaledDensity / sOriginalDensity);
        int densityDpi = (int) (density * 160);

        appDisplayMetrics.densityDpi = densityDpi;
        appDisplayMetrics.density = density;
        appDisplayMetrics.scaledDensity = scaleDensity;

        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.densityDpi = densityDpi;
        activityDisplayMetrics.density = density;
        activityDisplayMetrics.scaledDensity = scaleDensity;
    }

    /**
     * 设置设计图设计的屏幕宽度(单位为dp)
     *
     * @param designSize
     */
    public void setDesignSize(int designSize) {
        sDesignSize = designSize;
    }

    /**
     * 打印DisplayMetrics信息
     */
    public static void logDisplayMetrics(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        LogUtils.d("--------------------------打印开始-------------------------------------");
        LogUtils.d("density = " + metrics.density);
        LogUtils.d("densityDpi = " + metrics.densityDpi);
        LogUtils.d("scaledDensity = " + metrics.scaledDensity);
        LogUtils.d("heightPixels = " + metrics.heightPixels);
        LogUtils.d("widthPixels = " + metrics.widthPixels);
        LogUtils.d("xdpi = " + metrics.xdpi);
        LogUtils.d("ydpi = " + metrics.ydpi);
        LogUtils.d("--------------------------打印结束-------------------------------------");
    }

}
