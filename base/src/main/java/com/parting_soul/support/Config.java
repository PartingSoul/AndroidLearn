package com.parting_soul.support;


import com.parting_soul.base.BaseApplication;
import com.parting_soul.base.BuildConfig;
import com.parting_soul.support.utils.AppUtils;

/**
 * @author parting_soul
 * @date 2018/1/28
 */

public class Config {

    /**
     * FileProvider授权信息
     */
    public static final String AUTHORITY_FILE_PROVIDER = AppUtils.getAppPackageName() + ".fileProvider";

    public interface NetWorkConfig {
        /**
         * 默认超时时间
         */
        long DEFAULT_TIMEOUT = 15000;

    }

    /**
     * 网络请求状态响应码
     */
    public interface NetWorkResponseCode {
        /**
         * 请求成功
         */
        String RESPONSE_CODE_SUCCESS = "200";
    }

    public static class FilePath {
        public static String FILE_CACHE_PATH = BaseApplication.getAppContext().getExternalCacheDir().getAbsolutePath();
    }

}
