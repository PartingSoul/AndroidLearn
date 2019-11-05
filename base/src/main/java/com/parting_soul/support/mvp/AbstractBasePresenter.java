package com.parting_soul.support.mvp;


import com.parting_soul.support.rxjava.RxManager;

/**
 * @author parting_soul
 * @date 18-1-2
 * @description 要创建一个Presenter的抽象基类, 该类中实现两个方法，用于绑定V层与解绑V
 * c层,由于对应V层可以不同，所以用BaseView限定类型，只要实现了BaseView接口的View层都可以
 * <p>
 * 继承该抽象基类的子类Presenter必须指定V层的类型并且子类要实现对应P层的接口
 */

public abstract class AbstractBasePresenter<V extends BaseView> {
    protected RxManager mRxManager;
    protected V mView;

    /**
     * 绑定V层
     *
     * @param view
     */
    private void attachView(V view) {
        this.mView = view;
    }

    /**
     * 解绑V层
     */
    private void detachView() {
        this.mView = null;
    }


    /**
     * 用于切断V层和P层的连接
     */
    public void onCreate(V view) {
        mRxManager = new RxManager();
        attachView(view);
    }


    /**
     * 用于切断V层和P层的连接
     */
    public void onDestroy() {
        //切断上下游之间的联系
        mRxManager.dispose();
        detachView();
    }

}
