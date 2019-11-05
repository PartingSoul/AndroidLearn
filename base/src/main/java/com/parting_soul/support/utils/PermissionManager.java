package com.parting_soul.support.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.parting_soul.base.R;
import com.parting_soul.support.widget.dialog.PermissionTipDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author parting_soul
 * @date 2018/11/2
 */
public class PermissionManager implements PermissionTipDialog.OnResultCallback {
    private RxPermissions mRxPermissions;
    private Activity mActivity;
    private CompositeDisposable mCompositeDisposable;
    private PermissionTipDialog mRequestAgainDialog;
    private PermissionTipDialog mGoSettingDialog;
    private OnResultCallback mResultCallback;
    private Permission mPermission;
    private String[] mRequestPermissions;

    public PermissionManager(FragmentActivity activity) {
        mRxPermissions = new RxPermissions(activity);
        this.mActivity = activity;
        init();
    }

    public PermissionManager(Fragment fragment) {
        mRxPermissions = new RxPermissions(fragment);
        this.mActivity = fragment.getActivity();
        init();
    }

    private void init() {
        mCompositeDisposable = new CompositeDisposable();
    }

    public PermissionManager setRequestAgainDialog(PermissionTipDialog dialog) {
        this.mRequestAgainDialog = dialog;
        this.mRequestAgainDialog.setOnResultCallback(this);
        return this;
    }

    public PermissionManager setGoSettingDialog(PermissionTipDialog dialog) {
        this.mGoSettingDialog = dialog;
        this.mGoSettingDialog.setOnResultCallback(this);
        return this;
    }

    public PermissionManager setGoSettingDialogTitle(String title) {
        getGoSettingDialog().setTitle(title);
        return this;
    }

    public PermissionManager setGoSettingDialog(String message) {
        getGoSettingDialog().setMessage(message);
        return this;
    }

    public PermissionManager setRequestAgainDialogTitle(String title) {
        getRequestAgainDialog().setTitle(title);
        return this;
    }

    public PermissionManager setRequestAgainDialogMessage(String message) {
        getRequestAgainDialog().setMessage(message);
        return this;
    }

    public PermissionManager setDialogTitle(String title) {
        getRequestAgainDialog().setTitle(title);
        getGoSettingDialog().setTitle(title);
        return this;
    }

    public PermissionManager setDialogMessage(String message) {
        getRequestAgainDialog().setMessage(message);
        getGoSettingDialog().setMessage(message);
        return this;
    }

    private PermissionTipDialog getRequestAgainDialog() {
        if (mRequestAgainDialog == null) {
            mRequestAgainDialog = new PermissionTipDialog(mActivity);
            mRequestAgainDialog.setOnResultCallback(this);
        }
        return mRequestAgainDialog;
    }

    private PermissionTipDialog getGoSettingDialog() {
        if (mGoSettingDialog == null) {
            mGoSettingDialog = new PermissionTipDialog(mActivity);
            mGoSettingDialog.setConfirmText(mActivity.getString(R.string.go_to_settings));
            mGoSettingDialog.setOnResultCallback(this);
        }
        return mGoSettingDialog;
    }

    public void requestPermission(final String... permissions) {
        this.mRequestPermissions = permissions;
        mRxPermissions.requestEachCombined(permissions)
                .subscribe(new Observer<Permission>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        mCompositeDisposable.add(disposable);
                    }

                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {
                            if (mResultCallback != null) {
                                mResultCallback.onGrant();
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            getRequestAgainDialog().show();
                        } else {
                            //用户点击了不再询问
                            getGoSettingDialog().show();
                        }
                        mPermission = permission;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.show(R.string.permission_request_failed);
                        LogUtils.e(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public PermissionManager setResultCallback(OnResultCallback callback) {
        this.mResultCallback = callback;
        return this;
    }

    public void destroy() {
        mCompositeDisposable.dispose();
    }

    @Override
    public void onConfirm() {
        if (mPermission == null) {
            ToastUtil.show(R.string.permission_request_failed);
            return;
        }

        if (mPermission.shouldShowRequestPermissionRationale) {
            //用户点击了拒绝授予权限，再次请求权限
            requestPermission(mRequestPermissions);
        } else {
            //用户点击了不再询问
            jumpToSettingActivity();
        }
    }

    @Override
    public void onCancel() {

    }

    public interface OnResultCallback {
        void onGrant();
    }


    /**
     * 去设置界面
     */
    private void jumpToSettingActivity() {
        try {
            Uri packageURI = Uri.parse("package:" + mActivity.getPackageName());
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
            mActivity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断权限
     *
     * @param permissions
     * @return
     */
    public boolean isHavePermission(String... permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(mActivity, permissions[i]) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断权限
     *
     * @param permissions
     * @return
     */
    public static boolean isHavePermission(Activity activity, String... permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(activity, permissions[i]) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

}
