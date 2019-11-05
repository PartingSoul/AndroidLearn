package com.parting_soul.learn.nestscroll.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;

import com.parting_soul.learn.R;
import com.parting_soul.support.utils.LogUtils;

/**
 * @author parting_soul
 * @date 2019-10-25
 */
public class LinearLayoutNormalParent extends RelativeLayout implements NestedScrollingParent {
    private NestedScrollingParentHelper mParentHelper;
    private int mScrollY;
    private int MAX_SCROLL_Y;

    public LinearLayoutNormalParent(Context context) {
        this(context, null);
    }

    public LinearLayoutNormalParent(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayoutNormalParent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        LogUtils.e("nestedScrollAxes = " + nestedScrollAxes);
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        LogUtils.e("nestedScrollAxes = " + nestedScrollAxes);
        mParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(View target) {
        LogUtils.e("");
        mParentHelper.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        LogUtils.e("dxConsumed = " + dxConsumed + " dyConsumed = " + dyConsumed + " dxUnconsumed " + dxUnconsumed + " dyUnconsumed " + dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        consumed[1] = 0;
        float translationY = mTitleView.getTranslationY();

        int dis = MAX_SCROLL_Y - mScrollY;
        int offsetY = 0;
        if (dy > 0) {
            //手指往上滑动
            if (dy <= dis) {
                //还未滑动完
                offsetY = dy;
            } else {
                //已经滑动完毕获取滑动部分小于dy
                offsetY = dis;
            }
        } else {
            //手指往下滑动
            if (mNestScrollView.getScrollY() <= 0) {
                //内部的NestChildView已经滑动到了顶部
                dis = mScrollY >= 0 ? mScrollY : 0;
                dy = Math.abs(dy);
                if (dis >= dy) {
                    offsetY = -dy;
                } else {
                    offsetY = -dis;
                }
            }
        }
        mScrollY += offsetY;
        scrollBy(0, offsetY);
        consumed[1] = offsetY;
        if (offsetY != 0) {
            onScrollChanged(0, offsetY);
        }
        //是的顶部的titleBar始终固定在顶部
        mTitleView.setTranslationY(translationY + offsetY);
    }

    private void onScrollChanged(int scrollX, int scrollY) {
        LogUtils.e("scrollY" + scrollY);
        float alpha = mScrollY * 1.0f / MAX_SCROLL_Y;
        mTitleView.setAlpha(alpha);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        LogUtils.e("");
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        LogUtils.e("");
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        LogUtils.e("");
        return mParentHelper.getNestedScrollAxes();
    }


    ImageView mIvTop;
    View mNestScrollView;
    View mFloatView;
    View mTitleView;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIvTop = (ImageView) getChildAt(0);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof NestedScrollingChild) {
                mNestScrollView = view;
            } else if (view instanceof LinearLayout && view.getId() == R.id.ll_title_bar) {
                mTitleView = view;
                mTitleView.setAlpha(0);
            } else if (view instanceof TextView) {
                mFloatView = view;
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) mIvTop.getLayoutParams();
        MAX_SCROLL_Y = mIvTop.getMeasuredHeight() + params.topMargin + params.bottomMargin - mTitleView.getMeasuredHeight();
        LogUtils.e("" + MAX_SCROLL_Y);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mIvTop.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mNestScrollView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int totalHeight = MeasureSpec.getSize(heightMeasureSpec) + mNestScrollView.getMeasuredHeight();
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(totalHeight, mode));
    }

}
