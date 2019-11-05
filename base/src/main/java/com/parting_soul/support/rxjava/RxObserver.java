package com.parting_soul.support.rxjava;

import android.text.TextUtils;

import com.parting_soul.support.net.RequestException;
import com.parting_soul.support.utils.NetworkUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import retrofit2.HttpException;

/**
 * @author parting_soul
 * @date 2018/3/25
 * RxJava 观察者
 */

public abstract class RxObserver<T> implements Observer<T> {

    @Override
    public final void onError(Throwable e) {
        String code = "-1";
        String error = null;
        // 网络不可用
        if (!NetworkUtils.isNetworkAvailable()) {
            error = "网络不可用,请检查你的网络";
        }
        // 请求超时
        else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            error = "请求超时,请稍后再试";
        }

        // 服务器异常
        else if (e instanceof HttpException) {
            error = "服务器异常,请稍后再试";
        }
        // 数据报错
        else if (!TextUtils.isEmpty(e.getMessage()) && e.getMessage().contains(RxHelper.ERROR)) {
            if (e instanceof RequestException) {
                code = ((RequestException) e).getCode();
            }
            if (!TextUtils.isEmpty(e.getMessage())) {
                error = e.getMessage().replace(RxHelper.ERROR, "");
            }
        }
        // 其它
        else {
            error = "网络访问错误,请稍后再试";
        }
        onError(error);
        onError(code, error);
        onFinish();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public final void onNext(T t) {
        onSuccess(t);
        onFinish();
    }

    /**
     * 成功
     *
     * @param t
     */
    protected abstract void onSuccess(T t);

    /**
     * 异常信息
     *
     * @param error
     */
    public abstract void onError(String error);

    public void onError(String code, String error) {
    }

    /**
     * 请求结束，成功和失败都会回调
     */
    public void onFinish() {

    }

}
