package com.parting_soul.support.widget.dialog;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;

import com.parting_soul.base.R;

/**
 * @author parting_soul
 * @date 2018/3/5
 * 加载对话框
 */

public class LoadingDialog extends BaseDialog {

    public LoadingDialog(@NonNull Context context) {
        super(context);

        setView(R.layout.dialog_loading)
                .gravity(Gravity.CENTER)
                .width(ViewGroup.LayoutParams.WRAP_CONTENT)
                .height(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void initView() {

    }
}
