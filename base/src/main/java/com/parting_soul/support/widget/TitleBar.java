package com.parting_soul.support.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parting_soul.base.R;
import com.parting_soul.support.utils.DensityUtil;

/**
 * @author parting_soul
 * @date 2019/4/25
 */
public class TitleBar extends RelativeLayout {
    private TextView mTvTitle;
    private TextView mTvRight;
    private ImageView mIvBack;
    private View mBottomLine;

    private int mTitleTextColor;
    private float mTitleTextSize;
    private String mTitle;

    private String mRightText;
    private int mRightTextColor;
    private float mRightTextSize;
    private boolean isEnableRightText;
    private boolean isEnableBack;
    private Typeface mTitleTypeface;
    private boolean showBottomLine = true;
    private int leftImageRes = R.mipmap.back;
    private OnChildClickListener mOnChildClickListener;
    private OnBackClickListener mOnBackClickListener;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTitleTextColor = Color.parseColor("#FF333333");
        mTitleTextSize = DensityUtil.sp2px(context, 17);

        mRightTextColor = Color.parseColor("#FF7D7D7D");
        mRightTextSize = DensityUtil.sp2px(context, 13);
        isEnableRightText = false;


        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.TitleBar);
        if (array != null) {
            mTitleTextColor = array.getColor(R.styleable.TitleBar_titleColor, mTitleTextColor);
            mTitleTextSize = array.getDimension(R.styleable.TitleBar_titleSize, mTitleTextSize);
            mTitle = array.getString(R.styleable.TitleBar_title);

            mRightTextColor = array.getColor(R.styleable.TitleBar_rightTextColor, mRightTextColor);
            mRightTextSize = array.getDimension(R.styleable.TitleBar_rightTextSize, mRightTextSize);
            mRightText = array.getString(R.styleable.TitleBar_rightText);
            isEnableRightText = array.getBoolean(R.styleable.TitleBar_enableRightText, false);
            isEnableBack = array.getBoolean(R.styleable.TitleBar_enableBack, true);
            showBottomLine = array.getBoolean(R.styleable.TitleBar_showBottomLine, true);
            leftImageRes = array.getResourceId(R.styleable.TitleBar_leftImageRes, leftImageRes);

            array.recycle();
        }
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.titlebar, this, true);
        mTvTitle = findViewById(R.id.tv_title);
        mTvRight = findViewById(R.id.tv_right);
        mIvBack = findViewById(R.id.iv_back);
        mBottomLine = findViewById(R.id.bottom_line);

        mIvBack.setImageResource(leftImageRes);
        mTvTitle.setText(mTitle);
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleTextSize);
        mTvTitle.setTextColor(mTitleTextColor);
        mTvTitle.setTypeface(mTitleTypeface);
        mBottomLine.setVisibility(showBottomLine ? View.VISIBLE : View.GONE);

        mTvRight.setText(mRightText);
        mTvRight.setTextColor(mRightTextColor);
        mTvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
        enableRightText(isEnableRightText);
        mTvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnChildClickListener != null) {
                    mOnChildClickListener.onRightViewClick();
                }
            }
        });


        mIvBack = findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBackClickListener != null) {
                    mOnBackClickListener.onClickBack();
                } else {
                    if (getContext() instanceof Activity) {
                        ((Activity) getContext()).finish();
                    }
                }

            }
        });
        enableBack(isEnableBack);

    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setTitleViewAlpha(float alpha) {
        mTvTitle.setAlpha(alpha);
    }

    public void setTitleTypeFace(Typeface typeFace) {
        mTvTitle.setTypeface(typeFace);
    }

    public void setRightText(String text) {
        mTvRight.setText(text);
    }

    public void setTitleColor(int color) {
        mTvTitle.setTextColor(color);
    }

    public ImageView getIvBack() {
        return mIvBack;
    }

    public void enableRightText(boolean isEnable) {
        mTvRight.setVisibility(isEnable ? View.VISIBLE : View.GONE);
    }

    public void enableBack(boolean isEnable) {
        mIvBack.setVisibility(isEnable ? View.VISIBLE : View.GONE);
    }

    public interface OnChildClickListener {
        void onRightViewClick();
    }

    public void setOnChildClickListener(OnChildClickListener click) {
        this.mOnChildClickListener = click;
    }

    public interface OnBackClickListener {
        void onClickBack();
    }

    public void setOnBackClickListener(OnBackClickListener mOnBackClickListener) {
        this.mOnBackClickListener = mOnBackClickListener;
    }
}
