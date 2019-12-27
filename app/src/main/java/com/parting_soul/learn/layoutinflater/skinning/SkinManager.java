package com.parting_soul.learn.layoutinflater.skinning;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.parting_soul.learn.apt.AptActivity;
import com.parting_soul.support.utils.LogUtils;
import com.parting_soul.support.utils.SPUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 皮肤管理器
 *
 * @author parting_soul
 * @date 2019-10-12
 */
public class SkinManager {
    public static final String KEY_SKIN_PATH = "KEY_SKIN_PATH";
    private static SkinManager sSkinManager;
    private ResourceWrapper mResourceWrapper;
    private Context mContext;

    public static SkinManager getInstance() {
        if (sSkinManager == null) {
            synchronized (SkinManager.class) {
                if (sSkinManager == null) {
                    sSkinManager = new SkinManager();
                }
            }
        }
        return sSkinManager;
    }

    private SkinManager() {
    }

    int count = 0;
    boolean isAppFromBack;

    /**
     * 在Application 中初始化
     *
     * @param application
     */
    public void init(Application application) {
        mContext = application.getApplicationContext();
        String skinPath = SPUtil.getString(KEY_SKIN_PATH);
        mResourceWrapper = getResourceWrapper(skinPath);
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                LogUtils.e("ac " + activity);
                loadSkin(activity);
                count++;
                if (isAppFromBack) {
                    LogUtils.e("back");
//                    Intent intent = new Intent(activity, AptActivity.class);
//                    activity.startActivity(intent);
                }
                isAppFromBack = false;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                count--;
                if (count <= 0) {
                    isAppFromBack = true;
                    count = 0;
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    /**
     * 在Activity调用super.onCreate()方法前调用
     *
     * @param activity
     */
    public void attach(Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        inflater.setFactory2(new SkinningLayoutInflater.Factory2());
        loadSkin(activity);
    }

    /**
     * 加载皮肤
     *
     * @param activity
     */
    private void loadSkin(@NonNull Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        LayoutInflater.Factory2 factory2 = inflater.getFactory2();
        if (factory2 instanceof SkinningLayoutInflater.Factory2) {
            List<SkinningLayoutInflater.SkinView> lists = ((SkinningLayoutInflater.Factory2) factory2).getSkinAttrView();
            if (lists != null && lists.size() > 0) {
                for (SkinningLayoutInflater.SkinView skinView : lists) {
                    for (SkinningLayoutInflater.SkinAttr skinAttr : skinView.skinAttrs) {
                        if ("background".equals(skinAttr.attrName)) {
                            if ("color".equals(skinAttr.attrType)) {
                                LogUtils.d("" + skinAttr.resName);
                                int color = mResourceWrapper.getColor(skinAttr.resName, "color", skinAttr.resId);
                                skinView.view.setBackgroundColor(color);
                            } else if ("drawable".equals(skinAttr.attrType)) {
                                Drawable drawable = mResourceWrapper.getDrawable(skinAttr.resName, "drawable", skinAttr.resId);
                                skinView.view.setBackground(drawable);
                            }
                        } else if ("textColor".equals(skinAttr.attrName)) {
                            if (skinView.view instanceof TextView) {
                                int color = mResourceWrapper.getColor(skinAttr.resName, "color", skinAttr.resId);
                                ((TextView) skinView.view).setTextColor(color);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 获取加载资源文件的ResourceWrapper
     *
     * @param skinPath
     * @return
     */
    private ResourceWrapper getResourceWrapper(@NonNull String skinPath) {
        ResourceWrapper resourceWrapper = null;
        if (TextUtils.isEmpty(skinPath) || !new File(skinPath).exists()) {
            resourceWrapper = new ResourceWrapper(mContext, mContext.getResources(), mContext.getPackageName(), null);
        } else {
            String skinPackageName = null;
            try {
                PackageManager mPm = mContext.getPackageManager();
                PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPath, PackageManager
                        .GET_ACTIVITIES);
                skinPackageName = mInfo.packageName;

                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPathMethod = AssetManager.class.getMethod("addAssetPath", String.class);
                addAssetPathMethod.invoke(assetManager, skinPath);
                //添加加载路径
                Resources resources = new Resources(assetManager, mContext.getResources().getDisplayMetrics(),
                        mContext.getResources().getConfiguration());
                resourceWrapper = new ResourceWrapper(mContext, resources, skinPackageName, skinPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resourceWrapper;
    }


    /**
     * 切换日夜模式
     *
     * @param activity
     * @param isNightMode
     */
    public void changeNightMode(Activity activity, boolean isNightMode) {
        if (isNightMode) {
            String path = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/darkSkin.skin";
            File file = new File(path);
            if (!file.exists() || mResourceWrapper.isSameSkin(path)) {
                return;
            }
            mResourceWrapper = getResourceWrapper(path);
            SPUtil.putString(KEY_SKIN_PATH, path);
        } else {
            mResourceWrapper = getResourceWrapper(null);
            SPUtil.putString(KEY_SKIN_PATH, null);
        }
        loadSkin(activity);
    }
}
