package com.parting_soul.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.parting_soul.support.net.RetrofitApi;
import com.parting_soul.support.utils.IUserManager;
import com.parting_soul.support.utils.ScreenUtils;

import androidx.multidex.MultiDex;


/**
 * @author parting_soul
 * @date 17-12-31
 */
public abstract class BaseApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!TextUtils.equals(getPackageName(), getCurrentProcessName())) {
            return;
        }

        sContext = this;
        //获取屏幕的宽高
        ScreenUtils.getScreenWidth(this);
        IUserManager userManager = getUserManager();
        //设置用户管理器
        RetrofitApi.setUserManager(userManager);

        //初始化调试工具
//        initPandora();
    }

    /**
     * 获取当前进程名
     */
    protected String getCurrentProcessName() {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService
                (Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    public static Context getAppContext() {
        return sContext;
    }
//
//    /**
//     * 初始化调试工具，用与查看数据库,SP
//     */
//    private void initPandora() {
//        Pandora.init(this)
//                .enableShakeOpen();
//        Pandora.get().open();
//    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public abstract IUserManager getUserManager();

}
