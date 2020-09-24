package com.parting_soul.learn.viewdraw.customview;

import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.parting_soul.base.AbstractActivity;
import com.parting_soul.learn.R;
import com.parting_soul.learn.viewdraw.customview.view.HorizontalScrollView;
import com.parting_soul.support.utils.DensityUtil;

import java.util.ArrayList;

/**
 * @author parting_soul
 * @date 2020-09-11
 */
public class HorizontalScrollViewActivity extends AbstractActivity {

    HorizontalScrollView mHorizontalScrollView;
    HorizontalScrollView mHorizontalScrollView1;

    @Override
    protected int getContentViewId() {
        return R.layout.act_horizontal_scroll;
    }

    @Override
    protected void initData() {

    }

    int[] color = {Color.BLUE, Color.CYAN, Color.RED, Color.GREEN};

    @Override
    protected void initView() {
        setSwipeBackEnable(false);
        mHorizontalScrollView = getView(R.id.horizontalScrollview);
        mHorizontalScrollView1 = getView(R.id.hsv1);
        initHSV();
        initHSV1();
    }

    private void initHSV1() {
        int margin = DensityUtil.dip2px(this, 10);
        for (int i = 0; i < 10; i++) {
            RecyclerView rv = new RecyclerView(this);
            HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(HorizontalScrollView.LayoutParams.MATCH_PARENT,
                    HorizontalScrollView.LayoutParams.MATCH_PARENT);
            params.leftMargin = margin;
            params.rightMargin = margin;
            rv.setLayoutParams(params);
            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(new Adapter());
            rv.setBackgroundColor(Color.YELLOW);
            mHorizontalScrollView1.addView(rv);
        }
    }

    private void initHSV() {
        int margin = DensityUtil.dip2px(this, 5);
        int padding = DensityUtil.dip2px(this, 10);
        int height = DensityUtil.dip2px(this, 100);
        for (int i = 0; i < 19; i++) {
            TextView tv = new TextView(this);
            HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , height);
            params.topMargin = margin;
            params.leftMargin = margin;
            params.rightMargin = margin;
            params.bottomMargin = margin;
            tv.setPadding(padding, 0, padding, 0);
            tv.setGravity(Gravity.CENTER);
            tv.setText(String.valueOf(color[i % color.length]));
            tv.setBackgroundColor(color[i % color.length]);
            mHorizontalScrollView.addView(tv, params);
        }
    }

    static class Adapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public Adapter() {
            super(R.layout.adapter_simple_item);
            mData = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                mData.add("android " + i);
            }
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv_text, item);
        }
    }

}
