package com.parting_soul.support.rxjava;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 用于管理单个Presenter的RxJava订阅管理
 *
 * @author parting_soul
 * @date 2019/4/18
 */
public class RxManager {

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /**
     * 添加
     */
    public void add(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    /**
     * 切断RxJava上下游的联系
     */
    public void dispose() {
        mCompositeDisposable.dispose();
        mCompositeDisposable = null;
    }

}
