package com.parting_soul.learn.viewdraw.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.parting_soul.support.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 流布局
 *
 * @author parting_soul
 * @date 2019-10-30
 */
public class FlowLayout extends ViewGroup {
    private List<View> mMatchParentChildren = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 1. 首先测量出所有子View的宽高，测量时可利用的宽高高减去对应的margin
     * 2. 所有子View测量完成后，根据容器对应的布局方式，放置View来计算当前容器的宽高；当前容器为流布局，即一行后若标签能放下，则放置
     * 标签，否则换行放置。
     * 3. 对计算出的宽高依据父容器给的约束进行调整，保存当前容器的宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMatchParentChildren.clear();

        boolean isMeasureMatchParentChild = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY
                || MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;

        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int lineWidth = 0;
        int lineHeight = 0;
        int canUseWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight;
        int canUseHeight = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom;
        int maxWidth = 0;
        int maxHeight = 0;
        int totalHeight = paddingTop + paddingBottom;

        //测量子View
        final int childrenCount = getChildCount();
        for (int i = 0; i < childrenCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                if (lp.width == LayoutParams.MATCH_PARENT || lp.height == LayoutParams.MATCH_PARENT) {
                    mMatchParentChildren.add(child);
                }
            }
        }

        //重新测量match_parent的View
        int newWidthMeasureSpec = widthMeasureSpec;
        int newHeightMeasureSpec = heightMeasureSpec;
        if (isMeasureMatchParentChild) {
            for (int i = 0; i < mMatchParentChildren.size(); i++) {
                View child = mMatchParentChildren.get(i);
                LayoutParams lp = child.getLayoutParams();
                if (lp.width == LayoutParams.MATCH_PARENT) {
                    newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
                }
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
                }
                measureChildWithMargins(child, newWidthMeasureSpec, 0, newHeightMeasureSpec, 0);
            }
            canUseWidth = maxWidth;
            maxWidth = 0;
        }

        for (int i = 0; i < childrenCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            if (child.getVisibility() != View.GONE) {
                int childMeasureWidth = child.getMeasuredWidth();
                int childMeasureHeight = child.getMeasuredHeight();
                if (lineWidth + childMeasureWidth + params.leftMargin + params.rightMargin > canUseWidth) {
                    //另起一行
                    totalHeight += lineHeight;
                    maxWidth = Math.max(maxWidth, lineWidth);
                    lineWidth = 0;
                    lineHeight = 0;
                }

                if (totalHeight + params.topMargin + params.bottomMargin + child.getMeasuredHeight() - paddingTop - paddingBottom > canUseHeight) {
                    //所需的行高已经超过了可用行高，不再摆放
                    break;
                }

                lineWidth += childMeasureWidth + params.leftMargin + params.rightMargin;
                lineHeight = Math.max(lineHeight, childMeasureHeight + params.topMargin + params.bottomMargin);
            }
        }
        //最后一行
        maxWidth = Math.max(maxWidth, lineWidth);
        totalHeight = Math.max(totalHeight + lineHeight, totalHeight);
        //根据计算出的尺寸金额父容器的约束给出一个建议尺寸
        maxWidth = resolveSize(maxWidth, widthMeasureSpec);
        totalHeight = resolveSize(totalHeight, heightMeasureSpec);
        setMeasuredDimension(maxWidth, totalHeight);
    }

    /**
     * 1. 将传入的左上右下的值转化为相对当前容器的值
     * 2. 根据布局方式放置布局
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        int lineWidth = 0;
        int maxLineHeight = 0;
        int parentLeft = paddingLeft;
        int parentRight = right - left - paddingRight;
        int parentTop = paddingTop;
        int canUseWidth = parentRight - parentLeft;
        int canUseHeight = bottom - top - parentTop - getPaddingBottom();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                if (lineWidth + child.getMeasuredWidth() + params.leftMargin + params.rightMargin > canUseWidth) {
                    //换行
                    parentTop += maxLineHeight;
                    lineWidth = 0;
                    maxLineHeight = 0;
                }
                int l = lineWidth + params.leftMargin;
                int t = parentTop + params.topMargin;
                int r = l + child.getMeasuredWidth();
                int b = t + child.getMeasuredHeight();
                if (parentTop + params.topMargin + params.bottomMargin + child.getMeasuredHeight() - getPaddingTop() - getPaddingBottom() > canUseHeight) {
                    //所需的行高已经超过了可用行高，不再摆放
                    break;
                }
                child.layout(l, t, r, b);
                lineWidth += child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
                maxLineHeight = Math.max(maxLineHeight, child.getMeasuredHeight() + params.topMargin + params.bottomMargin);
            }
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return p;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
