package com.parting_soul.support.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.FloatRange;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.parting_soul.base.R;
import com.parting_soul.support.utils.ScreenUtils;


/**
 * @author parting_soul
 * @date 2018/9/25
 * 基类Dialog
 */

public abstract class BaseDialog extends Dialog {
    protected View mContentView;
    protected int width = WindowManager.LayoutParams.MATCH_PARENT;
    protected int height = WindowManager.LayoutParams.WRAP_CONTENT;
    protected boolean isCancelable = true;
    protected boolean isCanceledOnTouchOutside = true;
    protected int animResId = -1;
    protected int gravity = Gravity.CENTER;
    protected Context mContext;

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.NormalDialogStyle);
        this.mContext = context;
    }

    private BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //一定要先设置Dialog的ContentView在设置Windows宽高才有效
        setContentView(mContentView);
        initView();
        //配置对话框参数
        initConfig();
    }


    public <V extends View> V getView(@IdRes int id) {
        return  mContentView.findViewById(id);
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = width;
        lp.height = height;
        lp.gravity = gravity;
        dialogWindow.setAttributes(lp);

        if (animResId != -1) {
            //设置对话框动画
            dialogWindow.setWindowAnimations(animResId);
        }

        //按返回键是否可以取消
        setCancelable(isCancelable);
        //点击外部是否可以取消
        setCanceledOnTouchOutside(isCanceledOnTouchOutside);

    }

    /**
     * 初始化View
     */
    protected abstract void initView();

    public BaseDialog width(@FloatRange(from = 0, to = 1) float ratio) {
        this.width = (int) (ScreenUtils.getScreenWidth(mContext) * ratio);
        return this;
    }

    public BaseDialog width(int width) {
        this.width = width;
        return this;
    }

    public BaseDialog height(@FloatRange(from = 0, to = 1) float ratio) {
        this.height = (int) (ScreenUtils.getScreenHeight(mContext) * ratio);
        return this;
    }

    public BaseDialog height(int height) {
        this.height = height;
        return this;
    }

    public BaseDialog gravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public BaseDialog anim(@StyleRes int animResId) {
        this.animResId = animResId;
        return this;
    }

    public BaseDialog setCanceled(boolean isCancelable) {
        this.isCancelable = isCancelable;
        return this;
    }


    public BaseDialog setView(@LayoutRes int layoutId) {
        this.mContentView = View.inflate(getContext(), layoutId, null);
        return this;
    }

    public BaseDialog setView(View contentView) {
        this.mContentView = contentView;
        return this;
    }

}
