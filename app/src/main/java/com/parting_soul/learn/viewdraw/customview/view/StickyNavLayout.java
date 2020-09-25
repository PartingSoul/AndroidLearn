package com.parting_soul.learn.viewdraw.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.parting_soul.learn.R;

/**
 * @author parting_soul
 * @date 2020-09-24
 */
public class StickyNavLayout extends android.widget.LinearLayout implements ViewPager.OnPageChangeListener {
    private View mStickyTopView;
    private View mStickyTabLayout;
    private int mTouchSlop;
    private int mStickyTopViewHeight;
    private ViewPager mStickyViewPager;
    private RecyclerView mVpScrollView;
    private int mPageSelectPos;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private final int mMaximumVelocity;
    private final int mMinimumVelocity;

    public StickyNavLayout(Context context) {
        this(context, null);
    }

    public StickyNavLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickyNavLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();

        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 原来的测量保持不变
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 计算tobView隐藏时ViewPager的高度
        int vpHeight = getMeasuredHeight() - mStickyTabLayout.getMeasuredHeight();
        // 重新测量，由于测量模式为EXACTLY且ViewPager为MATCH_PARENT,因此会ViewPager会直接使用该vpHeight
        mStickyViewPager.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(vpHeight,
                MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mStickyTopViewHeight = mStickyTopView.getHeight();
    }

    private int mLastY;
    private boolean mIsBeingDragged;

    private int mDownY;
    private int mDownX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    // 若该Down事件会被子控件消费，down事件就不会回溯到该View的onTouchEvent方法
                    mScroller.abortAnimation();
                    mIsBeingDragged = false;
                }
                mDownY = (int) ev.getY();
                mDownX = (int) ev.getX();
                mLastY = mDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                int delaX = (int) Math.abs(ev.getX() - mDownX);
                int delaY = (int) Math.abs(ev.getY() - mDownY);

                if (delaY > delaX && getScrollY() < mStickyTopViewHeight) {
                    // 竖直方向滚动距离大于水平方向上的距离并且当前顶部布局可见
                    return true;
                }

                boolean isSwipeDown = ev.getY() - mDownY > 0;
                if (isSwipeDown && getScrollY() == mStickyTopViewHeight) {
                    // 向下滑动且顶部完全处于隐藏状态
                    mVpScrollView = getViewPagerRecyclerView(mPageSelectPos);
                    if (mVpScrollView != null) {
                        boolean canScrollVertically = mVpScrollView.canScrollVertically(-1);
                        if (!canScrollVertically) {
                            return true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
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
                mLastY = (int) event.getY();
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int delaY = (int) (mLastY - event.getY());
                if (Math.abs(delaY) >= mTouchSlop) {
                    mIsBeingDragged = true;
                }
                if (mIsBeingDragged) {
                    scrollBy(0, delaY);
                    mLastY = (int) event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    // 惯性滑动
                    mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    if (Math.abs(mVelocityTracker.getYVelocity()) >= mMinimumVelocity) {
                        mScroller.fling(
                                0,
                                getScrollY(),
                                0,
                                (int) -mVelocityTracker.getYVelocity(),
                                0,
                                0,
                                0,
                                mStickyTopViewHeight
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
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mStickyTopView = findViewById(R.id.sticky_nav_top);
        mStickyTabLayout = findViewById(R.id.sticky_nav_tab_layout);
        mStickyViewPager = findViewById(R.id.sticky_nav_vp);
        mStickyViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void scrollTo(int x, int y) {

        if (y >= mStickyTopViewHeight) {
            y = mStickyTopViewHeight;
        }

        if (y < 0) {
            y = 0;
        }

        super.scrollTo(x, y);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mStickyViewPager.removeOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mPageSelectPos = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private RecyclerView getViewPagerRecyclerView(int position) {
        View child = mStickyViewPager.getChildAt(position);
        if (child instanceof RecyclerView) {
            return (RecyclerView) child;
        }
        return null;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

}
