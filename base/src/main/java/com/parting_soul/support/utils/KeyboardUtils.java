package com.parting_soul.support.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.parting_soul.base.BaseApplication;


/**
 * @author parting_soul
 * @date 2018/8/2
 */
public class KeyboardUtils {
    /**
     * 显示隐藏键盘
     *
     * @param activity
     * @param editText
     * @param isShow
     */
    public static void showHideSoftInputFromWindow(Activity activity, EditText editText, boolean isShow) {
        if (editText != null) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
        }
        int softInputMode = isShow ? WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE :
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        activity.getWindow().setSoftInputMode(softInputMode);
    }

    /**
     * 隐藏键盘
     *
     * @param activity
     */
    public static void hideSoftInputFromWindow(Activity activity) {
        int softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
        activity.getWindow().setSoftInputMode(softInputMode);
    }

    public static void showKeyboard(EditText et) {
        InputMethodManager imm = (InputMethodManager) BaseApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            et.requestFocus();
            imm.showSoftInput(et, 0);
        }
    }

    public static void hiddenKeyboard(EditText et) {
        InputMethodManager imm = (InputMethodManager)  BaseApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }


    public static void hideKeyboard(MotionEvent event, View view,
                                    Activity activity) {
        try {
            if (view != null && view instanceof EditText) {
                int[] location = {0, 0};
                view.getLocationInWindow(location);
                int left = location[0], top = location[1], right = left
                        + view.getWidth(), bootom = top + view.getHeight();
                // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
                if (event.getRawX() < left || event.getRawX() > right
                        || event.getY() < top || event.getRawY() > bootom) {
                    // 隐藏键盘
                    IBinder token = view.getWindowToken();
                    InputMethodManager inputMethodManager = (InputMethodManager) activity
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(token,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
