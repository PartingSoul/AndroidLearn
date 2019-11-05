package com.parting_soul.support.utils;

/**
 * @author parting_soul
 * @date 2019/3/30
 */
public interface OnPayResultCallback {

    /**
     * 支付成功
     */
    void onSuccess();

    /**
     * 支付异常
     *
     * @param code
     * @param error
     */
    void onFailed(int code, String error);
}
