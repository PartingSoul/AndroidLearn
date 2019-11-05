package com.parting_soul.support.mvp;

/**
 * @author parting_soul
 * @date 17-12-31
 * MVP View约束接口
 */
public interface BaseView {
    /**
     * 显示加载界面
     */
    void showLoadingView();

    /**
     * 隐藏加载界面
     */
    void dismissLoadingView();

    /**
     * 显示信息
     *
     * @param error
     */
    void showMessage(String error);

    /**
     * 跳转协议页面
     *
     * @param jumpUrl 跳转url
     */
    void jumpTo(String jumpUrl);
}
