package com.parting_soul.support.widget.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.parting_soul.base.R;
import com.parting_soul.support.utils.ScreenUtils;


/**
 * 提示对话框
 *
 * @author parting_soul
 * @date 2018/10/23
 */
public class PromptDialog extends BaseDialog {
    private TextView mTvPositive;
    private TextView mTvNegative;
    private TextView mTvTitle;
    private OnResultCallback mCallback;

    public PromptDialog(@NonNull Context context) {
        super(context);

        setView(R.layout.dialog_prompt)
                .gravity(Gravity.CENTER)
                .width((int) (ScreenUtils.getScreenWidth(context) * 0.72))
                .height(ViewGroup.LayoutParams.WRAP_CONTENT);

        mTvPositive = mContentView.findViewById(R.id.tv_confirm);
        mTvNegative = mContentView.findViewById(R.id.tv_cancel);
        mTvTitle = mContentView.findViewById(R.id.tv_title);
    }

    public void setPositiveText(String positiveText) {
        this.mTvPositive.setText(positiveText);
    }

    public void setNegativeText(String negativeText) {
        this.mTvNegative.setText(negativeText);
    }

    public void setContent(CharSequence title) {
        this.mTvTitle.setText(title);
    }

    public void setContentTextSize(int textSize) {
        mTvTitle.setTextSize(textSize);
    }

    public void setPositiveTextColor(int color) {
        this.mTvPositive.setTextColor(color);
    }

    public void setNegativeTextColor(int color) {
        this.mTvNegative.setTextColor(color);
    }

    @Override
    protected void initView() {

        //确定；
        getView(R.id.tv_confirm)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        if (mCallback != null) {
                            mCallback.onConfirm();
                        }
                    }
                });

        //取消；
        getView(R.id.tv_cancel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        if (mCallback != null) {
                            mCallback.onCancel();
                        }
                    }
                });

    }

    public void setOnResultCallback(OnResultCallback callback) {
        this.mCallback = callback;
    }

    public interface OnResultCallback {
        void onConfirm();

        void onCancel();
    }

}
