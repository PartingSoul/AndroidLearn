package com.parting_soul.support.widget.tab;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parting_soul.base.R;
import com.parting_soul.support.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 底部导航栏
 *
 * @author parting_soul
 * @date 2018/7/13
 */
public class BottomTabLayout extends LinearLayout implements View.OnClickListener {

    private List<ViewHolder> mHolderLists;

    private OnItemSelectedListener mItemSelectedListener;

    /**
     * 之前选中的下标
     */
    private int mOldSelectedIndex = -1;

    /**
     * 之前选中的ViewHolder
     */
    private ViewHolder mOldViewHolder;


    public BottomTabLayout(Context context) {
        this(context, null);
    }

    public BottomTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        mHolderLists = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置高度为50dp
        ViewGroup.LayoutParams mParams = getLayoutParams();
        mParams.height = DensityUtil.dip2px(getContext(), 50);
        setLayoutParams(mParams);
    }

    /**
     * 添加tab
     *
     * @param tab
     */
    public BottomTabLayout addTab(BottomTab tab) {
        return addTab(tab, -1);
    }

    /**
     * 添加tab
     *
     * @param tab
     * @param tabViewId
     */
    public BottomTabLayout addTab(BottomTab tab, @LayoutRes int tabViewId) {
        View tabView;

        //设置weight属性
        if (tabViewId == -1) {
            //加载默认布局
            tabView = LayoutInflater.from(getContext()).inflate(R.layout.tab_item, this, false);
        } else {
            tabView = LayoutInflater.from(getContext()).inflate(tabViewId, this, false);
        }

        //设置标题以及默认状态
        ViewHolder holder = new ViewHolder(tabView, tab);
        holder.tvTitle.setText(tab.getTitle());
        holder.tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.color_bottom_tab_unselected));
        holder.ivIcon.setImageResource(tab.getUnSelectIcon());
        holder.item.setOnClickListener(this);
        holder.item.setId(mHolderLists.size());
        mHolderLists.add(holder);
        addView(tabView);

        return this;
    }

    /**
     * 设置选中的tab
     *
     * @param index
     */
    public void setSelected(int index) {
        if (index < 0 || index >= mHolderLists.size()) {
            index = 0;
        }

        boolean isChangeTabState = true;
        if (mItemSelectedListener != null) {
            //通知当前显示页
            isChangeTabState = mItemSelectedListener.onItemSelected(index, mOldSelectedIndex,
                    mHolderLists.get(index), mOldViewHolder);
        }

        if (isChangeTabState) {
            clearAllSelectState();
            ViewHolder holder = mHolderLists.get(index);
            holder.ivIcon.setImageResource(holder.tab.getSelectIcon());
            holder.tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.color_bottom_tab_selected));
            holder.isSelected = true;
        }
    }

    /**
     * 清除所有的选中状态
     */
    private void clearAllSelectState() {
        if (mHolderLists.size() <= 0) return;
        ViewHolder holder;
        for (int i = 0; i < mHolderLists.size(); i++) {
            holder = mHolderLists.get(i);
            if (holder.isSelected) {
                mOldSelectedIndex = i;
                mOldViewHolder = holder;
                holder.ivIcon.setImageResource(holder.tab.getUnSelectIcon());
                holder.tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.color_bottom_tab_unselected));
                holder.isSelected = false;
            }
        }
    }

    @Override
    public void onClick(View view) {
        setSelected(view.getId());
    }

    public static class ViewHolder {

        public ViewHolder(View item, BottomTab tab) {
            this.tvTitle = item.findViewById(R.id.tv_tab_title);
            this.ivIcon = item.findViewById(R.id.iv_tab_icon);
            this.item = item;
            this.tab = tab;
        }

        TextView tvTitle;
        ImageView ivIcon;
        View item;
        BottomTab tab;
        boolean isSelected;

        public BottomTab getTab() {
            return tab;
        }
    }

    public interface OnItemSelectedListener {

        /**
         * tab被选中则回调
         *
         * @param currentIndex  当前选中的下标
         * @param oldIndex      之前选中的下标
         * @param currentHolder 当前ViewHolder
         * @param oldViewHolder 之前选中的ViewHolder
         * @return 返回true表示切换tab 状态
         */
        boolean onItemSelected(int currentIndex, int oldIndex, ViewHolder currentHolder, ViewHolder oldViewHolder);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mItemSelectedListener = listener;
    }

}
