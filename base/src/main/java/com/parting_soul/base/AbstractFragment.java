package com.parting_soul.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parting_soul.support.utils.ImmersionUtils;
import com.parting_soul.support.widget.dialog.LoadingDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.Unbinder;

import static butterknife.ButterKnife.bind;

/**
 * @author parting_soul
 * @date 18-1-3
 */

public abstract class AbstractFragment extends Fragment {
    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;
    protected boolean isFirstVisible = true;
    protected View mRootView;

    private Unbinder mUnBinder;
    protected LoadingDialog mLoadingDialog;
    protected Activity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        mLoadingDialog = new LoadingDialog(activity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentView(), container, false);
        isFirstVisible = true;
        isInit = true;
        mUnBinder = bind(this, mRootView);
        addStatusBarHeightPaddingToRootView();
        initView();
        /**初始化的时候去加载数据**/
        isCanLoadData();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint() && isFirstVisible) {
            loadData();
            isLoad = true;
            isFirstVisible = false;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    /**
     * 停止加载数据
     */
    protected void stopLoad() {

    }

    protected abstract int getContentView();

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 懒加载数据
     */
    protected abstract void loadData();

    /**
     * 根据id获取View
     *
     * @param id
     * @param <V>
     * @return
     */
    protected <V extends View> V getView(int id) {
        return mRootView.findViewById(id);
    }


    protected void showLoading() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    protected void dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public boolean isAddStatusBarHeightPaddingToRootView() {
        return false;
    }

    protected void addStatusBarHeightPaddingToRootView() {
        if (isAddStatusBarHeightPaddingToRootView()) {
            int statusBarHeight = ImmersionUtils.getStatusBarHeightWithTranslucentStatus(activity);
            mRootView.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    /**
     * 启动Activity
     *
     * @param context
     * @param clazz
     */
    public static void startActivity(Context context, Class<?> clazz) {
        Intent intent = new Intent();
        intent.setClass(context, clazz);
        context.startActivity(intent);
    }

    /**
     * 启动Activity
     *
     * @param context
     * @param bundle
     * @param clazz
     */
    public static void startActivity(Context context, Bundle bundle, Class<?> clazz) {
        Intent intent = new Intent();
        intent.setClass(context, clazz);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 启动Activity
     *
     * @param activity
     * @param clazz
     * @param requestCode
     */
    public static void startActivity(Activity activity, Bundle bundle, Class<?> clazz, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动Activity
     *
     * @param activity
     * @param clazz
     * @param requestCode
     */
    public static void startActivity(Activity activity, Class<?> clazz, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(activity, clazz);
        activity.startActivityForResult(intent, requestCode);
    }

}
