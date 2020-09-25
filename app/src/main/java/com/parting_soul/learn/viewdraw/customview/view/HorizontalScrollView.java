package com.parting_soul.learn.viewdraw.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.parting_soul.learn.BuildConfig;

/**
 * @author parting_soul
 * @date 2020-09-11
 */
public class HorizontalScrollView extends ViewGroup {
    private final int mMaximumVelocity;
    private final int mMinimumVelocity;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mMaxScrollX;
    private int mTouchSlop;
    private boolean mIsBeingDragged;
    private int mLastX;

    public HorizontalScrollView(Context context) {
        this(context, null);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mVelocityTracker = VelocityTracker.obtain();
        mScroller = new Scroller(context);

        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    public void smoothScroll(int dx, int dy) {
        mScroller.startScroll(getScrollX(), getScrollY(), dx, dy);
        invalidate();

        if (BuildConfig.DEBUG) {
            Log.e("HorizontalScrollView", "smoothScroll dx = " + dx + " dy = " + dy);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);


        // 测量所有的子view
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0,
                        heightMeasureSpec, 0);
            }
        }

        // 测量自己
        int width = getPaddingLeft() + getPaddingRight();
        int height = 0;

        boolean shouldCalculateWidth = widthSpecMode == MeasureSpec.AT_MOST;
        boolean shouldCalculateHeight = heightSpecMode == MeasureSpec.AT_MOST;

        if (shouldCalculateHeight || shouldCalculateWidth) {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                if (child.getVisibility() != View.GONE) {

                    if (shouldCalculateWidth) {
                        // 当前容器宽度为wrap_content或者父容器宽度为wrap_content且当前容器宽度不为精确的数值
                        width += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                    }

                    if (shouldCalculateHeight) {
                        //当前容器高度为wrap_content或者父容器高度为wrap_content且当前容器高度度不为精确的数值
                        height = Math.max(height, child.getMeasuredHeight() + params.topMargin + params.bottomMargin);
                    }

                }
            }

            // 添加padding
            height += getPaddingTop() + getPaddingBottom();

        } else {
            width = widthSpecSize;
            height = heightSpecSize;
        }

        //根据父容器的measureSpec调整宽高
        width = resolveSize(width, widthMeasureSpec);
        height = resolveSize(height, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childSumWidth = 0;

        int childUseWidth = getPaddingLeft();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {

            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int left = childUseWidth + params.leftMargin;
            int top = params.topMargin + getPaddingTop();
            int right = left + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();

            child.layout(left, top, right, bottom);

            childUseWidth += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;
            childSumWidth += params.leftMargin + child.getWidth() + params.rightMargin;
        }

        mMaxScrollX = childSumWidth - (getWidth() - getPaddingLeft() - getPaddingRight());
    }

    private int mDownX;
    private int mDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                mLastX = (int) ev.getX();
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int delaX = (int) Math.abs(mDownX - ev.getX());
                int delaY = (int) Math.abs(mDownY - ev.getY());
                Log.e("HorizontalScrollView", " delaX = " + delaX + " delaY = " + delaY);
                if (delaX > delaY) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastX = (int) event.getX();
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (mLastX - event.getX());

                if (!mIsBeingDragged && Math.abs(dx) >= mTouchSlop) {
                    // 开始拖拽
                    mIsBeingDragged = true;
                }

                if (mIsBeingDragged) {
                    scrollBy(dx, 0);
                    mLastX = (int) event.getX();
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    if (Math.abs(mVelocityTracker.getXVelocity()) >= mMinimumVelocity) {
                        Log.e("onTouchEvent", " v = " + mMinimumVelocity);
                        mScroller.fling(getScrollX(),
                                0,
                                (int) -mVelocityTracker.getXVelocity(),
                                0,
                                0,
                                mMaxScrollX,
                                0,
                                0
                        );
                        invalidate();
                    }
                    mVelocityTracker.clear();
                    mIsBeingDragged = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mIsBeingDragged = false;
                break;
            default:
        }
        return true;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }


    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

    }

    @Override
    public void scrollTo(int x, int y) {
        if (x < 0) {
            x = 0;
        }
        if (x >= mMaxScrollX) {
            x = mMaxScrollX;
        }
        super.scrollTo(x, y);
    }

}
