package com.parting_soul.support.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author ciba
 * @date 2018/4/3
 * @description
 */

public class ScrollChangedScrollView extends ScrollView {
    private OnScrollChangedListener scrollChangedListener;

    public ScrollChangedScrollView(Context context) {
        super(context);
    }

    public ScrollChangedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollChangedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollChangedListener != null){
            scrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener scrollChangedListener) {
        this.scrollChangedListener = scrollChangedListener;
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
