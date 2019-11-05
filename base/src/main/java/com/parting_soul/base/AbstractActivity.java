package com.parting_soul.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parting_soul.support.utils.ImmersionUtils;
import com.parting_soul.support.utils.LogUtils;
import com.parting_soul.support.utils.ScreenAdaptationUtils;
import com.parting_soul.support.widget.dialog.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * @author parting_soul
 * @date 18-1-1
 */
public abstract class AbstractActivity extends AppCompatActivity implements SwipeBackActivityBase {
    protected LoadingDialog mLoadingDialog;
    /**
     * 滑动返回帮助类
     */
    private SwipeBackActivityHelper mHelper;
    ViewGroup mRootLayout;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //屏幕适配
        ScreenAdaptationUtils.setCustomDensity(this, getApplication());
        if (getContentViewId() != 0) {
            setContentView(getContentViewId());
        }
        mRootLayout = findViewById(android.R.id.content);

        mUnbinder = ButterKnife.bind(this);

        //初始化滑动返回帮助类
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        mLoadingDialog = new LoadingDialog(this);

        //初始化View
        this.initView();

        //初始化数据
        this.initData();
    }

    /**
     * 布局id
     *
     * @return
     */
    protected abstract int getContentViewId();

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
        if (isTransparentStatusBar()) {
            ImmersionUtils.setTransparentStatusBar(this);
        }

        //半透明
        if (mRootLayout != null && isSetHalfTransparentStatusBar()) {
            ImmersionUtils.setTransparentStatusBar(this);
            ImmersionUtils.setTranslucentStatusBarBackground(this, ImmersionUtils.DEFAULT_HALF_TRANSLUCENT_COLOR);
            int statusBarHeightWithTranslucentStatus = ImmersionUtils.getStatusBarHeightWithTranslucentStatus(this);
            ViewGroup.LayoutParams layoutParams = mRootLayout.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = statusBarHeightWithTranslucentStatus;
            }
        }
    }

    /**
     * 半透明状态栏
     *
     * @return
     */
    protected boolean isSetHalfTransparentStatusBar() {
        return false;
    }

    /**
     * 透明状态栏
     *
     * @return
     */
    protected boolean isTransparentStatusBar() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
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

    /**
     * 初始化数据
     */
    protected abstract void initData();


    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 根据id获取View
     *
     * @param id
     * @param <V>
     * @return
     */
    protected <V extends View> V getView(int id) {
        return findViewById(id);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public <V extends View> V findViewById(int id) {
        V v = super.findViewById(id);
        if (v == null && mHelper != null)
            return (V) mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(-1, R.anim.anim_activity_close_exit);
    }

}
