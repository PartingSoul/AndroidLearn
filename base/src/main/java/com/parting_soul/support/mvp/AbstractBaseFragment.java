package com.parting_soul.support.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.parting_soul.base.AbstractFragment;
import com.parting_soul.support.utils.ToastUtil;


/**
 * @author parting_soul
 * @date 18-1-2
 * @description 创建一个View层的抽象基类, 该抽象类主要为在V层Fragment创建时调用P层方法绑定P层与销毁时解绑P层
 * 由于要调用P层的绑定与解绑方法,所以要限定P层必须是AbstractBasePresenter的子类,AbstractBasePresenter对绑定解绑的View
 * 有类型限定,所以要指定对应的类型为BaseView的子类型
 * <p>
 * 继承该类的子类必须指定V层类型以及继承AbstractBasePresenter的Presenter类型，同时要实现View层对应的接口
 */

public abstract class AbstractBaseFragment<V extends BaseView, P extends AbstractBasePresenter<V>>
        extends AbstractFragment implements BaseView {
    protected P mPresenter;

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mPresenter == null) {
            mPresenter = createPresenter();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mPresenter != null) {
            //Presenter绑定View
            mPresenter.onCreate((V) this);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        super.onDestroyView();
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
