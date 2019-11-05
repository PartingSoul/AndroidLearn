package com.parting_soul.learn.layoutinflater.skinning;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

/**
 * @author parting_soul
 * @date 2019-10-12
 */
public class ResourceWrapper {
    private Resources mResource;
    private Context mCurrentAppContext;
    private String skinPackageName;
    private String mCurrentSkinPath;

    public ResourceWrapper(Context currentAppContext, Resources resources, String skinPackageName, String skinPath) {
        this.mResource = resources;
        this.mCurrentAppContext = currentAppContext;
        this.skinPackageName = skinPackageName;
        this.mCurrentSkinPath = skinPath;
    }

    public int getColor(String name, String defType, int defaultColor) {
        int resId = mResource.getIdentifier(name, defType, skinPackageName);
        int color = 0;
        if (resId != 0) {
            color = mResource.getColor(resId);
        } else {
            color = ContextCompat.getColor(mCurrentAppContext, defaultColor);
        }
        return color;
    }

    public Drawable getDrawable(String name, String defType, int defaultDrawableRes) {
        int resId = mResource.getIdentifier(name, defType, skinPackageName);
        Drawable drawable = null;
        if (resId != 0) {
            drawable = mResource.getDrawable(resId);
        } else {
            drawable = ContextCompat.getDrawable(mCurrentAppContext, defaultDrawableRes);
        }
        return drawable;
    }

    public boolean isSameSkin(String skinPath) {
        return TextUtils.equals(skinPath, mCurrentSkinPath);
    }

}
