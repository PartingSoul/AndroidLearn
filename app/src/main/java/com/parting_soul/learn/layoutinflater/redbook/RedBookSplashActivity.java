package com.parting_soul.learn.layoutinflater.redbook;

import com.parting_soul.base.AbstractActivity;
import com.parting_soul.learn.R;

import butterknife.BindView;

/**
 * 仿小红书开屏
 *
 * @author parting_soul
 * @date 2019-10-10
 */
public class RedBookSplashActivity extends AbstractActivity {
    @BindView(R.id.mSplashView)
    SplashView mSplashView;

    @Override
    protected int getContentViewId() {
        return R.layout.act_red_book_splash;
    }

    @Override
    protected void initData() {
        setSwipeBackEnable(false);
    }

    @Override
    protected void initView() {
        mSplashView.setUp(
                R.layout.view_intro_1,
                R.layout.view_intro_2,
                R.layout.view_intro_3,
                R.layout.view_intro_4,
                R.layout.view_intro_5,
                R.layout.view_login
        );
    }
}
