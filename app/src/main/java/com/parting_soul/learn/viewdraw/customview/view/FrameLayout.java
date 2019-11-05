package com.parting_soul.learn.viewdraw.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义FrameLayout
 *
 * @author parting_soul
 * @date 2019-11-04
 */
public class FrameLayout extends ViewGroup {
    private List<View> mMeasureParentViews = new ArrayList<>();

    public FrameLayout(Context context) {
        super(context);
    }

    public FrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0;
        int maxHeight = 0;

        boolean isMeasureMatchParentChild = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY
                || MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                //找出最大的宽高
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + params.leftMargin + params.rightMargin);
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + params.topMargin + params.bottomMargin);
                if (params.width == LayoutParams.MATCH_PARENT || params.height == LayoutParams.MATCH_PARENT) {
                    mMeasureParentViews.add(child);
                }
            }
        }

        //调整，保存宽高
        maxWidth = resolveSize(maxWidth, widthMeasureSpec);
        maxHeight = resolveSize(maxHeight, heightMeasureSpec);
        setMeasuredDimension(maxWidth, maxHeight);

        int newWidthMeasureSpec = widthMeasureSpec;
        int newHeightMeasureSpec = heightMeasureSpec;
        //当前容器为wrap_content时重新测量match_parent的子View,使得设置match_parent的子View的边长与最大边长一致
        if (isMeasureMatchParentChild) {
            for (int i = 0; i < mMeasureParentViews.size(); i++) {
                View child = mMeasureParentViews.get(i);
                LayoutParams lp = child.getLayoutParams();
                if (lp.width == LayoutParams.MATCH_PARENT) {
                    newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
                }
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
                }
                measureChildWithMargins(child, newWidthMeasureSpec, 0, newHeightMeasureSpec, 0);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //计算可以放置子View的范围
        int parentLeft = getPaddingLeft();
        int parentTop = getPaddingTop();
        int parentRight = right - left - getPaddingRight();
        int parentBottom = bottom - top - getPaddingBottom();

        int l = 0;
        int t = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) child.getLayoutParams();

                int gravity = params.gravity;
                if (gravity == -1) {
                    gravity = Gravity.TOP | Gravity.START;
                }
                int layoutDirection = getLayoutDirection();
                int horizontalGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

                //水平布局
                switch (horizontalGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.CENTER_HORIZONTAL:
                        l = parentLeft + (parentRight - parentLeft - child.getMeasuredWidth()) / 2 + params.leftMargin - params.rightMargin;
                        break;
                    case Gravity.RIGHT:
                        l = parentRight - child.getMeasuredWidth() - params.rightMargin;
                        break;
                    case Gravity.LEFT:
                    default:
                        l = parentLeft + params.leftMargin;
                        break;
                }

                //垂直方向布局
                switch (verticalGravity) {
                    case Gravity.CENTER_VERTICAL:
                        t = parentTop + (parentBottom - parentTop - child.getMeasuredHeight()) / 2 + params.topMargin - params.bottomMargin;
                        break;
                    case Gravity.BOTTOM:
                        t = parentBottom - child.getMeasuredHeight() - params.bottomMargin;
                        break;
                    case Gravity.TOP:
                    default:
                        t = parentTop + params.topMargin;
                        break;
                }

                child.layout(l, t, l + child.getMeasuredWidth(), t + child.getMeasuredHeight());
            }
        }
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new android.widget.FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return p;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new android.widget.FrameLayout.LayoutParams(getContext(), attrs);
    }

}
