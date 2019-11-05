package com.parting_soul.support.utils;

import android.app.Activity;
import android.webkit.SslErrorHandler;

import com.parting_soul.base.BuildConfig;
import com.parting_soul.base.R;
import com.parting_soul.support.widget.dialog.PromptDialog;

import androidx.fragment.app.Fragment;

/**
 * ssl证书授权失败弹框管理
 *
 * @author parting_soul
 * @date 2019/3/27
 */
public class SslErrorManager {
    private PromptDialog mPromptDialog;
    private Activity mActivity;
    private Fragment mFragment;

    public SslErrorManager(Activity activity) {
        this.mActivity = activity;
        initPromptDialog(activity);
    }

    public SslErrorManager(Fragment fragment) {
        this(fragment.getActivity());
        this.mFragment = fragment;
    }

    private void initPromptDialog(Activity activity) {
        if (mPromptDialog == null) {
            mPromptDialog = new PromptDialog(activity);
            mPromptDialog.setPositiveText("继续");
            mPromptDialog.setNegativeText("取消");
            mPromptDialog.setContent("ssl证书验证失败，是否继续前往?");
        }
    }

    private void show(final SslErrorHandler handler) {
        if (mFragment != null && mFragment.isDetached()) {
            return;
        }
        if (mActivity == null || mActivity.isDestroyed()) {
            return;
        }
        mPromptDialog.setOnResultCallback(new PromptDialog.OnResultCallback() {
            @Override
            public void onConfirm() {
                handler.proceed();
            }

            @Override
            public void onCancel() {
                handler.cancel();
            }
        });
        mPromptDialog.show();
    }

    public void process(SslErrorHandler handler) {
        // 是否要上google play
        handler.proceed();
//        boolean isGoogle = false;
//        if (!isGoogle) {
//            return;
//        }
        // show(handler);
    }

}
