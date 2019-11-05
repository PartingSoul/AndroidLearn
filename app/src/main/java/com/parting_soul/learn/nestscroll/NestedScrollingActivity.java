package com.parting_soul.learn.nestscroll;

import com.parting_soul.base.AbstractActivity;
import com.parting_soul.learn.R;
import com.parting_soul.learn.nestscroll.view.LinearLayoutNormalChild;
import com.parting_soul.support.utils.LogUtils;

/**
 * @author parting_soul
 * @date 2019-10-25
 */
public class NestedScrollingActivity extends AbstractActivity {
    private LinearLayoutNormalChild mChild;

    @Override
    protected int getContentViewId() {
        return R.layout.act_nest_scroll;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        mChild = getView(R.id.ll_child);
    }


    @Override
    protected boolean isTransparentStatusBar() {
        return true;
    }
}
