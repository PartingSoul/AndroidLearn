package com.parting_soul.learn.viewdraw.customview;

import android.view.ViewGroup;
import android.widget.TextView;

import com.parting_soul.base.AbstractActivity;
import com.parting_soul.learn.R;
import com.parting_soul.learn.viewdraw.customview.view.FlowLayout;

import java.util.Random;

/**
 * View  测量
 *
 * @author parting_soul
 * @date 2019-10-29
 */
public class FlowLayoutActivity extends AbstractActivity {
    private FlowLayout mFlowLayout;

    String[] TEXT = {
            "a", "b", "d", "e", "f", "g",
            "FlowLayoutActivity", "AbstractActivity", "getContentViewId", "initView", "initData",
            "mFlowLayout = getView(R.id.mFlowLayout);", "MarginLayoutParams",
            "shape_tag_border_style_one",
            "Android开发艺术探索", "Java编程思想", "码农有道", "Android群英传", "Flutter入门与实战", "小灰算法", "C++从入门到放弃"
    };

    @Override
    protected int getContentViewId() {
        return R.layout.act_flowlayout;
    }

    @Override
    protected void initData() {

    }

    int[] BORDER_BG = {R.drawable.shape_tag_border_style_one, R.drawable.shape_tag_border_style_two, R.drawable.shape_tag_border_style_three};

    @Override
    protected void initView() {
        Random random = new Random();
        mFlowLayout = getView(R.id.mFlowLayout);
        for (int i = 0; i < TEXT.length; i++) {
            TextView textView = new TextView(this);
            textView.setBackgroundResource(BORDER_BG[i % BORDER_BG.length]);
            textView.setText(TEXT[random.nextInt(1001) % TEXT.length]);
            textView.setPadding(20, 15, 20, 15);
            ViewGroup.MarginLayoutParams params;
            if (i == 0) {
                params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            } else {
                params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            }
            params.leftMargin = 16;
            params.topMargin = 16;
            params.bottomMargin = 16;
            params.rightMargin = 16;
            mFlowLayout.addView(textView, params);
        }
    }

}
