package com.parting_soul.support.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.parting_soul.base.AbstractActivity;
import com.parting_soul.support.utils.ToastUtil;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;


/**
 * @author parting_soul
 * @date 18-1-1
 */
public abstract class AbstractBaseActivity<V extends BaseView, P extends AbstractBasePresenter<V>>
        extends AbstractActivity implements BaseView {
    protected P mPresenter;

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {

        if (mPresenter == null) {
            mPresenter = createPresenter();
        }

        if (mPresenter != null) {
            //Presenter绑定View
            mPresenter.onCreate((V) this);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }


    /**
     * 创建Presenter
     *
     * @return P
     */
    protected abstract P createPresenter();

    @Override
    public void showMessage(String error) {
        if (TextUtils.isEmpty(error)) {
            return;
        }
        ToastUtil.show(error);
    }

    @Override
    public void showLoadingView() {
        showLoading();
    }

    @Override
    public void dismissLoadingView() {
        dismissLoading();
    }

    @Override
    public void jumpTo(String jumpUrl) {
        Intent intent = new Intent();
        intent.setClassName("com.hongyanreader", "com.hongyanreader.support.scheme.SchemeFilterActivity");
        intent.putExtra("url", jumpUrl);
        startActivity(intent);
    }
}
