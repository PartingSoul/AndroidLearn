package com.parting_soul.learn.layoutinflater.skinning;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.parting_soul.support.utils.LogUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author parting_soul
 * @date 2019-10-11
 */
public class SkinningLayoutInflater extends LayoutInflater {


    protected SkinningLayoutInflater(Context context) {
        super(context);
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return null;
    }

    public static final String[] PREFIXS = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    public static class Factory2 implements LayoutInflater.Factory2 {
        private List<SkinView> mSkinAttrView = new ArrayList<>();

        public static Set<String> SKIN_ATTRS = new HashSet<>();

        static {
            SKIN_ATTRS.add("textColor");
            SKIN_ATTRS.add("background");
            SKIN_ATTRS.add("src");
        }

        public List<SkinView> getSkinAttrView() {
            return mSkinAttrView;
        }

        @Nullable
        @Override
        public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
            View view = null;
            if (name.contains(".")) {
                //自定义控件或者兼容包的控件(全类名)
                view = createView(name, context, attrs);
            } else {
                //省略前缀的类
                for (String prefix : PREFIXS) {
                    view = createView(prefix + name, context, attrs);
                    if (view != null) {
                        break;
                    }
                }
            }

            if (view != null) {
                List<SkinAttr> skinAttrs = new ArrayList<>();
                for (int i = 0; i < attrs.getAttributeCount(); i++) {
                    String attrName = attrs.getAttributeName(i);
                    String attrValue = attrs.getAttributeValue(i);
                    if (isSkinAttr(attrName) && attrValue.startsWith("@")) {
                        int resId = Integer.parseInt(attrValue.substring(1));
                        String resourceName = context.getResources().getResourceName(resId);
                        String resourceTypeName = context.getResources().getResourceTypeName(resId);
                        String resourceEntryName = context.getResources().getResourceEntryName(resId);
//                        LogUtils.d("resourceName = " + resourceName + " resourceTypeName = " + resourceTypeName + " resourceEntryName =  " + resourceEntryName);
                        SkinAttr attr = new SkinAttr(attrName, resourceEntryName, resourceTypeName, resId);
                        skinAttrs.add(attr);
                    }
//                    LogUtils.e("attrName = " + attrName + " attrValue = " + attrValue);
                }
                if (skinAttrs.size() > 0) {
                    mSkinAttrView.add(new SkinView(view, skinAttrs));
                    LogUtils.d("" + mSkinAttrView.get(mSkinAttrView.size() - 1));
                }
            }
            return view;
        }

        private boolean isSkinAttr(String attrName) {
            return SKIN_ATTRS.contains(attrName);
        }

        private View createView(String name, Context context, AttributeSet attributeSet) {

            try {
                Class clazz = Class.forName(name);
                Constructor constructor = clazz.getConstructor(Context.class, AttributeSet.class);
                return (View) constructor.newInstance(context, attributeSet);
            } catch (Exception e) {
            }
            return null;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
            return null;
        }

    }

    public static class SkinAttr {
        String attrName;
        String attrType;
        String resName;
        int resId;

        public SkinAttr(String attrName, String resName, String attrType, int resId) {
            this.attrName = attrName;
            this.attrType = attrType;
            this.resId = resId;
            this.resName = resName;
        }

        @Override
        public String toString() {
            return "SkinAttr{" +
                    "attrName='" + attrName + '\'' +
                    ", attrType='" + attrType + '\'' +
                    ", resId=" + resId +
                    '}';
        }
    }

    public static class SkinView {
        View view;
        List<SkinAttr> skinAttrs;

        public SkinView(View view, List<SkinAttr> skinAttrs) {
            this.view = view;
            this.skinAttrs = skinAttrs;
        }

        @Override
        public String toString() {
            return "SkinView{" +
                    "view=" + view +
                    ", skinAttrs=" + skinAttrs +
                    '}';
        }
    }
}
