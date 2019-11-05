package com.parting_soul.support.net;


import android.os.Build;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.parting_soul.base.BaseApplication;
import com.parting_soul.base.BuildConfig;
import com.parting_soul.support.Config;
import com.parting_soul.support.utils.AppUtils;
import com.parting_soul.support.utils.IUserManager;
import com.parting_soul.support.utils.JsonUtils;
import com.parting_soul.support.utils.LogUtils;
import com.parting_soul.support.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author parting_soul
 * @date 2018/3/19
 * Retrofit全局配置类
 */

public class RetrofitApi {

    private Retrofit mRetrofit;
    private IUserManager mUserManager;

    private RetrofitApi() {
        final StringBuilder mMessage = new StringBuilder();
        //   日志拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
//                if (BuildConfig.DEBUG) {
                    // 请求或者响应开始
                    if (message.startsWith("--> POST") || message.startsWith("--> GET")) {
                        mMessage.setLength(0);
                    }
                    // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                    if ((message.startsWith("{") && message.endsWith("}"))
                            || (message.startsWith("[") && message.endsWith("]"))) {
                        //打印Json数据
                        LogUtils.e(message);
                        //Json数据格式化
                        message = JsonUtils.formatJson(JsonUtils.decodeUnicode(message));
                        mMessage.append('\n');
                    }
                    mMessage.append(message.concat("\n").concat("│ "));
                    // 响应结束，打印整条日志
                    if (message.startsWith("<-- END HTTP")) {
                        LogUtils.e(mMessage.toString());
                    }
//                }
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        File cacheFile = new File(BaseApplication.getAppContext().getCacheDir(), "cache");
        //100Mb
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(Config.NetWorkConfig.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(Config.NetWorkConfig.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(cache)
                .build();


        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://www.baidu.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 创建单例
     */
    private static class SingletonHolder {
        private static final RetrofitApi INSTANCE = new RetrofitApi();
    }

    /**
     * 用户管理
     *
     * @param manager
     */
    public static void setUserManager(IUserManager manager) {
        SingletonHolder.INSTANCE.mUserManager = manager;
    }


    /**
     * 创建对应的Service Api
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getServiceApi(Class<T> clazz) {
        return SingletonHolder.INSTANCE.mRetrofit.create(clazz);
    }

    class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtils.isConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                LogUtils.d("Okhttp", "no network");
            }

            Response originalResponse = chain.proceed(request);
            if (NetworkUtils.isConnected()) {
                //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }

}
