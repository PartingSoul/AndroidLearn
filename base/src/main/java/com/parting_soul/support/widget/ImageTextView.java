package com.parting_soul.support.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.parting_soul.base.R;
import com.parting_soul.support.utils.ImageLoader;


/**
 * @author : ciba
 * @date : 2018/9/13
 * @description : replace your description
 */

public class ImageTextView extends LinearLayout {
    private final static int LEFT = 0;
    private final static int TOP = 1;
    private final static int RIGHT = 2;
    private final static int BOTTOM = 3;
    private static final int DEFAULT_TEXT_COLOR = 0xff5c5c5c;
    private static final int DEFAULT_TEXT_CHECK_COLOR = 0;
    private static final float DEFAULT_TEXT_SIZE = 14;

    private int imageOrientation = LEFT;
    private int imageRes;
    private int imageCheckRes;
    private int textColor = DEFAULT_TEXT_COLOR;
    private int textCheckColor = DEFAULT_TEXT_CHECK_COLOR;
    private float textSize;
    private ImageView imageView;
    private TextView textView;
    private boolean checked;
    private String text;
    private String textCheck;

    public ImageTextView(Context context) {
        this(context, null);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        int defaultWidthHeight = (int) (getResources().getDisplayMetrics().density * 20);
        int defaultPadding = (int) (getResources().getDisplayMetrics().density * 8);
        int imageWidth = defaultWidthHeight;
        int imageHeight = defaultWidthHeight;
        int imageTextPadding = defaultPadding;

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ImageTextView);
        if (array != null) {
            imageOrientation = array.getInt(R.styleable.ImageTextView_imageOrientation, LEFT);
            imageRes = array.getResourceId(R.styleable.ImageTextView_imageRes, 0);
            imageCheckRes = array.getResourceId(R.styleable.ImageTextView_imageCheckRes, 0);
            imageWidth = (int) array.getDimension(R.styleable.ImageTextView_imageWidth, defaultWidthHeight);
            imageHeight = (int) array.getDimension(R.styleable.ImageTextView_imageHeight, defaultWidthHeight);

            textColor = array.getColor(R.styleable.ImageTextView_textNormalColor, DEFAULT_TEXT_COLOR);
            textCheckColor = array.getColor(R.styleable.ImageTextView_textCheckColor, DEFAULT_TEXT_CHECK_COLOR);
            textSize = array.getFloat(R.styleable.ImageTextView_textNormalSize, DEFAULT_TEXT_SIZE);
            text = array.getString(R.styleable.ImageTextView_textNormal);
            textCheck = array.getString(R.styleable.ImageTextView_textCheck);
            imageTextPadding = (int) array.getDimension(R.styleable.ImageTextView_imageTextPadding, defaultPadding);
            array.recycle();
        }

        imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setImageRes(false);
        LayoutParams imageParams = new LayoutParams(imageWidth, imageHeight);

        textView = new TextView(getContext());
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setIncludeFontPadding(false);
        LinearLayout.LayoutParams textParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textParams);
        setText(false);
        textView.setTextSize(textSize);


        if (LEFT == imageOrientation) {
            setOrientation(HORIZONTAL);
            imageParams.setMargins(0, 0, imageTextPadding, 0);
            imageView.setLayoutParams(imageParams);
            addView(imageView);
            addView(textView);
        } else if (TOP == imageOrientation) {
            setOrientation(VERTICAL);
            imageParams.setMargins(0, 0, 0, imageTextPadding);
            imageView.setLayoutParams(imageParams);
            addView(imageView);
            addView(textView);
        } else if (RIGHT == imageOrientation) {
            setOrientation(HORIZONTAL);
            imageParams.setMargins(imageTextPadding, 0, 0, 0);
            imageView.setLayoutParams(imageParams);
            addView(textView);
            addView(imageView);
        } else {
            setOrientation(VERTICAL);
            imageParams.setMargins(0, imageTextPadding, 0, 0);
            imageView.setLayoutParams(imageParams);
            addView(textView);
            addView(imageView);
        }

        if (imageRes == 0 && imageCheckRes == 0) {
            imageView.setVisibility(View.GONE);
        }
    }

    public void setCheck(boolean checked) {
        this.checked = checked;
        setText(checked);
        setImageRes(checked);
    }

    public void setCheck(boolean checked, boolean isImageViewGone) {
        this.checked = checked;
        setText(checked);
        imageView.setVisibility(isImageViewGone ? View.GONE : View.VISIBLE);
    }

    public boolean isChecked() {
        return checked;
    }

    private void setText(boolean checked) {
        if (textCheckColor == 0) {
            textCheckColor = textColor;
        }
        if (TextUtils.isEmpty(textCheck)) {
            textCheck = text;
        }
        textView.setTextColor(checked ? textCheckColor : textColor);
        textView.setText(checked ? textCheck : text);
    }

    public void setText(String text) {
        this.textCheck = text;
        this.text = text;
        textView.setText(text);
    }

    private void setImageRes(boolean checked) {
        if (checked && imageCheckRes != 0) {
            ImageLoader.load(getContext(), imageCheckRes, imageView);
        } else if (imageRes != 0) {
            ImageLoader.load(getContext(), imageRes, imageView);
        }
    }

    public void setImageCheckRes(int checkRes) {
        this.imageCheckRes = checkRes;
    }

    public void setImageRes(int res) {
        this.imageRes = res;
    }
}
