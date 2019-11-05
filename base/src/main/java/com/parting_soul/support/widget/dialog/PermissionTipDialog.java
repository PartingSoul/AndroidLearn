package com.parting_soul.support.widget.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parting_soul.base.R;
import com.parting_soul.support.utils.ScreenUtils;

import androidx.annotation.NonNull;


/**
 * @author parting_soul
 * @date 2018/11/1
 */
public class PermissionTipDialog extends BaseDialog {

    private TextView mTvCancel;
    private TextView mTvConfirm;
    private TextView mTvContent;
    private TextView mTvTitle;
    private OnResultCallback mCallback;


    public PermissionTipDialog(@NonNull Context context) {
        super(context);

        setView(R.layout.dialog_permission_tip)
                .gravity(Gravity.CENTER)
                .width((int) (ScreenUtils.getScreenWidth(getContext()) * 0.8))
                .height(ViewGroup.LayoutParams.WRAP_CONTENT);

        mTvConfirm = mContentView.findViewById(R.id.tv_confirm);
        mTvCancel = mContentView.findViewById(R.id.tv_cancel);
        mTvTitle = mContentView.findViewById(R.id.tv_title);
        mTvContent = mContentView.findViewById(R.id.tv_content);
    }

    @Override
    protected void initView() {

        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mCallback != null) {
                    mCallback.onConfirm();
                }
            }
        });

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mCallback != null) {
                    mCallback.onCancel();
                }
            }
        });
    }

    public void setConfirmText(String positiveText) {
        this.mTvConfirm.setText(positiveText);
    }

    public void setCancelText(String negativeText) {
        this.mTvCancel.setText(negativeText);
    }

    public void setMessage(String msg) {
        this.mTvContent.setText(msg);
    }

    public void setTitle(String title) {
        this.mTvTitle.setText(title);
    }

    public void setOnResultCallback(OnResultCallback callback) {
        this.mCallback = callback;
    }

    public interface OnResultCallback {
        void onConfirm();

        void onCancel();
    }
}
