package com.parting_soul.learn.viewdraw.customview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.parting_soul.learn.R;

/**
 * 自定义线性布局
 *
 * @author parting_soul
 * @date 2019-11-05
 */
public class LinearLayout extends ViewGroup {
    private Drawable mDivider;
    private int mDividerWidth;
    private int mDividerHeight;
    private int mShowDividers = SHOW_DIVIDER_NONE;
    private int mDividerPadding;
    private int mOrientation;

    /**
     * 水平排列
     */
    public static final int HORIZONTAL = 0;

    /**
     * 垂直排列
     */
    public static final int VERTICAL = 1;

    /**
     * 不显示分割线
     */
    public static final int SHOW_DIVIDER_NONE = 0;

    /**
     * 在开头显示分割线
     */
    public static final int SHOW_DIVIDER_BEGINNING = 1;

    /**
     * 在中间显示分割线
     */
    public static final int SHOW_DIVIDER_MIDDLE = 2;

    /**
     * 在结尾显示分割线
     */
    public static final int SHOW_DIVIDER_END = 4;

    public LinearLayout(Context context) {
        this(context, null);
    }

    public LinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LinearLayout, defStyleAttr, 0);
            mShowDividers = typedArray.getInt(R.styleable.LinearLayout_c_showDividers, SHOW_DIVIDER_NONE);
            mDivider = typedArray.getDrawable(R.styleable.LinearLayout_c_divider);
            mDividerPadding = (int) typedArray.getDimension(R.styleable.LinearLayout_c_dividerPadding, 0);
            mOrientation = typedArray.getInt(R.styleable.LinearLayout_c_orientation, HORIZONTAL);
            typedArray.recycle();

            mDividerWidth = mDivider != null ? mDivider.getIntrinsicWidth() : 0;
            mDividerHeight = mDivider != null ? mDivider.getIntrinsicHeight() : 0;
        }
        setWillNotDraw(mDivider == null);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOrientation == VERTICAL) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 测量垂直排列布局
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int totalHeight = 0;
        int maxWidth = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            if (hasDividerBeforeChildAt(i)) {
                //存在分割线，则加上分割线的高度
                totalHeight += mDividerHeight;
            }

            //测量子控件
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, totalHeight);

            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            totalHeight += child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + params.leftMargin + params.rightMargin);
        }

        if (hasDividerBeforeChildAt(childCount)) {
            //存在分割线，则加上分割线的高度
            totalHeight += mDividerHeight;
        }

        maxWidth = resolveSize(maxWidth, widthMeasureSpec);
        totalHeight = resolveSize(totalHeight, heightMeasureSpec);
        setMeasuredDimension(maxWidth, totalHeight);
    }

    /**
     * 在当前索引的View前是否存在分割线
     *
     * @param index
     * @return
     */
    private boolean hasDividerBeforeChildAt(int index) {
        if (index == getChildCount()) {
            return (mShowDividers & SHOW_DIVIDER_END) != 0;
        }
        boolean isAllViewGone = isAllViewGoneBeforeIndex(index);
        if (isAllViewGone) {
            return (mShowDividers & SHOW_DIVIDER_BEGINNING) != 0;
        }
        return (mShowDividers & SHOW_DIVIDER_MIDDLE) != 0;
    }

    /**
     * 当前索引前的View都隐藏
     *
     * @param index
     * @return
     */
    private boolean isAllViewGoneBeforeIndex(int index) {
        for (int i = index; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mOrientation == VERTICAL) {
            layoutVertical(changed, left, top, right, bottom);
        }
    }

    /**
     * 垂直排列布局
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void layoutVertical(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int parentLeft = getPaddingLeft();
        int parentTop = getPaddingTop();

        int l = 0, t = parentTop;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            if (hasDividerBeforeChildAt(i)) {
                //存在分割线，则加上分割线的高度
                t += mDividerHeight;
            }


            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            l = parentLeft + params.leftMargin;
            t += params.topMargin;
            child.layout(l, t, l + child.getMeasuredWidth(), t + child.getMeasuredHeight());
            t += child.getMeasuredHeight() + params.bottomMargin;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDivider == null) {
            return;
        }

        if (mOrientation == VERTICAL) {
            drawVertical(canvas);
        } else {
            drawHoriztontal(canvas);
        }
    }


    /**
     * 绘制水平排列布局
     *
     * @param canvas
     */
    private void drawHoriztontal(Canvas canvas) {
    }

    /**
     * 绘制垂直排列布局
     *
     * @param canvas
     */
    private void drawVertical(Canvas canvas) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            if (hasDividerBeforeChildAt(i)) {
                int top = child.getTop() - mDividerHeight - params.topMargin;
                drawHorizontalDivider(canvas, top);
            }
        }

        //末尾的分割线
        if (hasDividerBeforeChildAt(childCount)) {
            View child = getLastVisibleChild();
            if (child != null) {
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                drawHorizontalDivider(canvas, top);
            }
        }
    }


    /**
     * 绘制水平分割线
     *
     * @param canvas
     */
    private void drawHorizontalDivider(Canvas canvas, int top) {
        mDivider.setBounds(getPaddingLeft() + mDividerPadding, top,
                getWidth() - getPaddingRight() - mDividerPadding, top + mDividerHeight);
        mDivider.draw(canvas);
    }

    /**
     * 获取最后一个可见项
     *
     * @return
     */
    private View getLastVisibleChild() {
        int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.VISIBLE) {
                return child;
            }
        }
        return null;
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new android.widget.LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return p;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new android.widget.LinearLayout.LayoutParams(getContext(), attrs);
    }
}
