package com.parting_soul.learn.nestscroll.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

import com.parting_soul.support.utils.LogUtils;

/**
 * @author parting_soul
 * @date 2019-10-25
 */
public class LinearLayoutNormalChild extends ScrollView implements NestedScrollingChild {
    private NestedScrollingChildHelper mChildHelper;
    private int mOldY;
    private int[] mConsumed = new int[2];
    private int[] mOffsetInWindow = new int[2];
    private View mParentView;

    public LinearLayoutNormalChild(Context context) {
        this(context, null);
    }

    public LinearLayoutNormalChild(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayoutNormalChild(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        LogUtils.e("");
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        LogUtils.e("");
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        LogUtils.e("");
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        LogUtils.e("");
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        LogUtils.e("");
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        LogUtils.e("");
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        LogUtils.e("");
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        LogUtils.e("");
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        LogUtils.e("");
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.d("");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                mOldY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //计算滑动的偏移，offsetY的符号与scrollY的符号一致
                int offsetY = (int) (mOldY - event.getRawY());
                if (dispatchNestedPreScroll(0, offsetY, mConsumed, mOffsetInWindow)) {
                    //在当前View处理滑动前先将处理权交给父容器
                    offsetY -= mConsumed[1];
                }
                //剩下的滑动交给当前View
                if (getParent() instanceof ViewGroup) {
                    mParentView = (View) getParent();
                    if (mParentView.getScrollY() >= 0) {
                        int currentScrollY = getScrollY();
                        int remainY = 0;
                        if (offsetY < 0) {
                            //该控件手指往下滑动下限
                            remainY = currentScrollY + offsetY;
                            if (remainY < 0) {
                                offsetY = -currentScrollY;
                            }
                        } else {
                            //控件手指往上滑动上限
                            if (getScrollY() > 1000) {
                                offsetY = 0;
                            }
                            LogUtils.d("s ===> Y " + offsetY);
                        }
//                        scrollBy(0, offsetY);
                        overScrollBy(0, offsetY, getScrollX(), getScrollY(), 0, 0, 0, 0, true);
                    }
                }
                mOldY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                stopNestedScroll();
                break;
            default:
        }
        return true;
    }

}
