package com.parting_soul.learn.layoutinflater.redbook;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.parting_soul.learn.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author parting_soul
 * @date 2019-10-10
 */
public class SplashView extends FrameLayout implements ViewPager.OnPageChangeListener {
    ViewPager mViewPager;
    private ImageView mIvWomen;
    private List<View> mParallaxViews;
    private AnimationDrawable mWomenRunAnim;

    public SplashView(@NonNull Context context) {
        this(context, null);
    }

    public SplashView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplashView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mViewPager = new ViewPager(getContext());
        mViewPager.setId(R.id.splash_viewpager);
        mViewPager.setOnPageChangeListener(this);
        this.addView(mViewPager, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mIvWomen = new ImageView(getContext());
        mWomenRunAnim = (AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.animation_women_run);
        mIvWomen.setImageDrawable(mWomenRunAnim);
    }

    public void setUp(int... layoutIds) {
        if (layoutIds == null) {
            return;
        }
        mParallaxViews = new ArrayList<>();
        //将所有布局加入FrameLayout中
        RedBookLayoutInflater inflater = new RedBookLayoutInflater(getContext(), mParallaxViews);
        for (int i = 0; i < layoutIds.length; i++) {
            inflater.inflate(layoutIds[i], this);
            Log.e("tag", "---------------");
        }
        attachParallaxViewPageIndex();

        for (int i = 0; i < mParallaxViews.size(); i++) {
            SplashAttributesTag tag = (SplashAttributesTag) (mParallaxViews.get(i).getTag(R.id.splash_attr_tag_id));
            if (tag != null) {
                Log.e("aaa", "setUp: " + tag.index + " " + i + " " + Thread.currentThread());
            }
        }

        mViewPager.setAdapter(new PagerAdapter() {

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View page = new View(getContext());
                container.addView(page);
                return page;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {
                return layoutIds.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }
        });

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        addView(mIvWomen, params);
    }

    /**
     * 关联视差控件的页索引
     */
    private void attachParallaxViewPageIndex() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            childAttachParallaxViewPageIndex(getChildAt(i), i - 1);
        }
    }

    private void childAttachParallaxViewPageIndex(View root, int index) {
        if (root instanceof ViewGroup) {
            ViewGroup rg = (ViewGroup) root;
            int childCount = rg.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = rg.getChildAt(i);
                SplashAttributesTag tag = (SplashAttributesTag) child.getTag(R.id.splash_attr_tag_id);
                if (tag != null) {
                    tag.index = index;
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (position == 4) {
            mIvWomen.setTranslationX(-positionOffsetPixels);
        }

        for (View view : mParallaxViews) {
            SplashAttributesTag tag = (SplashAttributesTag) view.getTag(R.id.splash_attr_tag_id);
            if (tag != null) {
                Log.e("tagindex", "onPageScrolled: " + tag.index);
                if (tag.index == position) {
                    view.setVisibility(View.VISIBLE);
                    //手指往左划移出屏幕的页中的控件
                    view.setTranslationX(positionOffsetPixels * tag.xOut);
                    view.setTranslationY(positionOffsetPixels * tag.yOut);
                    //设置了透明度变化的速率
                    view.setAlpha(1 - positionOffset);
                } else if (tag.index == position + 1) {
                    view.setVisibility(View.VISIBLE);
                    //手指往左划进入屏幕的页中的控件
                    int translateOffset = getWidth() - positionOffsetPixels;
                    view.setTranslationX(translateOffset * tag.xIn);
                    view.setTranslationY(-translateOffset * tag.yIn);
                    if (!tag.notSetData(tag.aOut)) {
                        view.setAlpha(positionOffset);
                    }
                } else {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                mWomenRunAnim.start();
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                mWomenRunAnim.stop();
                break;
            default:
                break;
        }
    }
}
