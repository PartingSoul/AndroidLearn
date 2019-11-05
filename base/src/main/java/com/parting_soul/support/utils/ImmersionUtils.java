package com.parting_soul.support.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * @author parting_soul
 * @date 2018/3/18
 * 沉浸式工具类
 */

public class ImmersionUtils {
    private static final String STATUS_BAR_BG = "STATUS_BAR_BG";
    public static final int DEFAULT_HALF_TRANSLUCENT_COLOR = 0x30000000;
    public static final int DEFAULT_TRANSLUCENT_COLOR = 0x00000000;
    private static int statusBarHeight;

    /**
     * 设置透明状态栏
     *
     * @param activity
     */
    public static void setTransparentStatusBar(Activity activity) {
        //设置全屏，也就是将状态栏隐藏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            //设置根布局全屏显示,让应用的主体内容占用系统状态栏的空间和底部虚拟导航栏,注意SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN和
            // SYSTEM_UI_FLAG_LAYOUT_STABLE必须要结合在一起使用
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            //设置状态栏为透明
            window.setStatusBarColor(Color.TRANSPARENT);
//            //设置导航栏为透明
//            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            // 透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置沉浸式模式
     */
    public static void setImmersionMode(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    public static int getStatusBarHeightWithTranslucentStatus(Context context) {
        if (statusBarHeight <= 0) {
            if (Build.VERSION.SDK_INT >= 19) {
                return getStatusBarHeight(context);
            } else {
                return 0;
            }
        } else {
            return statusBarHeight;
        }
    }

    public static int getStatusBarHeight(Context context) {
        //获取status_bar_height资源的ID
        if (context == null) {
            return 0;
        }
        statusBarHeight = (int) (context.getResources().getDisplayMetrics().density * 28);
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public static void setTranslucentStatusBarBackground(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            int statusBarHeightWithTranslucentStatus = getStatusBarHeightWithTranslucentStatus(activity);
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null && decorView instanceof FrameLayout) {
                FrameLayout parent = (FrameLayout) decorView;
                View statusBarBackgroundView = parent.findViewWithTag(STATUS_BAR_BG);
                if (statusBarBackgroundView == null) {
                    statusBarBackgroundView = new View(activity);
                    statusBarBackgroundView.setTag(STATUS_BAR_BG);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeightWithTranslucentStatus);
                    statusBarBackgroundView.setLayoutParams(params);
                    parent.addView(statusBarBackgroundView);
                }
                statusBarBackgroundView.setBackgroundColor(color);
            }
        }

    }

}
