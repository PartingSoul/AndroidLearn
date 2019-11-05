package com.parting_soul.learn.layoutinflater.redbook;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.parting_soul.learn.R;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 小红书布局填充器
 *
 * @author parting_soul
 * @date 2019-10-10
 */
public class RedBookLayoutInflater extends LayoutInflater {
    private List<View> mCustomAttrView;

    protected RedBookLayoutInflater(Context context, List<View> customAttrViews) {
        super(context);
        this.mCustomAttrView = customAttrViews;
        setFactory2(new RedBookLayoutInflater.RedBookFactory2());
    }

    protected RedBookLayoutInflater(Context context) {
        super(context);
    }

    protected RedBookLayoutInflater(LayoutInflater original, Context newContext) {
        super(original, newContext);
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new RedBookLayoutInflater(newContext);
    }

    public static final String[] PREFIXS = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    class RedBookFactory2 implements LayoutInflater.Factory2 {

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
                //提取自定义属性
                SplashAttributesTag splashAttributesTag = new SplashAttributesTag();
                TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SplashView);
                splashAttributesTag.aIn = array.getFloat(R.styleable.SplashView_a_in, 0f);
                splashAttributesTag.aOut = array.getFloat(R.styleable.SplashView_a_out, 0f);
                splashAttributesTag.xIn = array.getFloat(R.styleable.SplashView_x_in, 0f);
                splashAttributesTag.xOut = array.getFloat(R.styleable.SplashView_x_out, 0f);
                splashAttributesTag.yIn = array.getFloat(R.styleable.SplashView_y_in, 0f);
                splashAttributesTag.yOut = array.getFloat(R.styleable.SplashView_y_out, 0f);
                array.recycle();
                view.setTag(R.id.splash_attr_tag_id, splashAttributesTag);
                if (!splashAttributesTag.isEmpty()) {
                    Log.e("tag", "onCreateView: " + splashAttributesTag);
                    mCustomAttrView.add(view);
                }
            }
            return view;
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
}
