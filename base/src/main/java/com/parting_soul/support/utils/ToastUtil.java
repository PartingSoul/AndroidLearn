package com.parting_soul.support.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.parting_soul.base.BaseApplication;

import androidx.annotation.StringRes;


/**
 * Toast统一管理类
 *
 * @author parting_soul
 */
public class ToastUtil {
    private static Toast toast = null;

    /**
     * 弹出一个Toast
     *
     * @param resId
     */
    public static void show(@StringRes int resId) {
        show(BaseApplication.getAppContext().getString(resId));
    }


    /**
     * 弹出一个Toast
     *
     * @param msg
     */
    public static void show(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    /**
     * 弹出一个长时间的Toast
     *
     * @param msg
     */
    public static void showLong(String msg) {
        show(msg, Toast.LENGTH_LONG);
    }

    private static void show(String msg, int duration) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (toast != null) {
            toast.cancel();
        }
        // 创建一个Toast提示消息
        toast = Toast.makeText(BaseApplication.getAppContext(), msg, duration);
        // 设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.BOTTOM, 0, 240);
        // 显示消息
        toast.show();
    }

}