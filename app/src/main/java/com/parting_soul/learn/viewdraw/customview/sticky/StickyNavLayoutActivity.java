package com.parting_soul.learn.viewdraw.customview.sticky;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.parting_soul.base.AbstractActivity;
import com.parting_soul.learn.R;
import com.parting_soul.learn.viewdraw.customview.view.StickyNavLayout;

/**
 * @author parting_soul
 * @date 2020-09-24
 */
public class StickyNavLayoutActivity extends AbstractActivity {
    private StickyNavLayout mStickyNavLayout;
    private SlidingTabLayout mTabLayout;
    private ViewPager mViewPager;
    private String[] TITLE = {"详情", "评论", "精选"};

    @Override
    protected int getContentViewId() {
        return R.layout.act_sticky_nav_layout;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        mTabLayout = findViewById(R.id.sticky_nav_tab_layout);
        mViewPager = findViewById(R.id.sticky_nav_vp);
        initViewPager();
    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(TITLE.length);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new StickyFragment();
            }

            @Override
            public int getCount() {
                return TITLE.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return TITLE[position];
            }
        });
        mTabLayout.setViewPager(mViewPager, TITLE);
    }

}
