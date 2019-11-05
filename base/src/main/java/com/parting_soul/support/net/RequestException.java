package com.parting_soul.support.net;

/**
 * @author parting_soul
 * @date 2018/3/25
 * 网络请求异常
 */

public class RequestException extends Exception {
    private String code;

    public RequestException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
