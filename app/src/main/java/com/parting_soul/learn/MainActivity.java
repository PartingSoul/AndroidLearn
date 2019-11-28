package com.parting_soul.learn;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.parting_soul.annotation.Hello;
import com.parting_soul.annotation.Test;
import com.parting_soul.base.AbstractActivity;
import com.parting_soul.learn.apt.AptActivity;
import com.parting_soul.learn.bean.Item;
import com.parting_soul.learn.layoutinflater.redbook.RedBookSplashActivity;
import com.parting_soul.learn.layoutinflater.skinning.SkinManager;
import com.parting_soul.learn.layoutinflater.skinning.SkinningActivity;
import com.parting_soul.learn.nestscroll.NestedScrollingActivity;
import com.parting_soul.learn.viewdraw.customview.FlowLayoutActivity;
import com.parting_soul.learn.viewdraw.customview.FrameLayoutActivity;
import com.parting_soul.learn.viewdraw.customview.LinearLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Hello
@Test
public  class MainActivity extends AbstractActivity implements BaseSectionQuickAdapter.OnItemClickListener {

    @BindView(R.id.mRv)
    RecyclerView mRv;
    private List<Item> mLists;
    private BaseSectionQuickAdapter<Item, BaseViewHolder> mAdapter;
    private static final int COLUMN_NUM = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SkinManager.getInstance().attach(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_main;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        initRecyclerView();
        setSwipeBackEnable(false);
    }

    private void initRecyclerView() {
        initDataItem();
        GridLayoutManager manager = new GridLayoutManager(this, COLUMN_NUM);
        mAdapter = new BaseSectionQuickAdapter<Item, BaseViewHolder>(R.layout.adapter_rv_item,
                R.layout.adapter_rv_header, mLists) {

            @Override
            protected void convert(BaseViewHolder helper, Item item) {
                helper.setText(R.id.tv_name, item.t);
            }

            @Override
            protected void convertHead(BaseViewHolder helper, Item item) {
                helper.setText(R.id.tv_title, item.header);
            }
        };
        mRv.setLayoutManager(manager);
        mAdapter.setSpanSizeLookup((gridLayoutManager, position) -> {
            Item item = mAdapter.getItem(position);
            if (item == null || item.isHeader) {
                return COLUMN_NUM;
            }
            return 1;
        });
        mRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void initDataItem() {
        mLists = new ArrayList<>();
        mLists.add(new Item(true, "LayoutInflater"));
        mLists.add(new Item("仿小红书开屏", RedBookSplashActivity.class));
        mLists.add(new Item("换肤", SkinningActivity.class));
        mLists.add(new Item(true, "View绘制"));
        mLists.add(new Item("自定义FlowLayout", FlowLayoutActivity.class));
        mLists.add(new Item("自定义FrameLayout", FrameLayoutActivity.class));
        mLists.add(new Item("自定义LinearLayout", LinearLayoutActivity.class));
        mLists.add(new Item(true, "NestedScrolling"));
        mLists.add(new Item("基本使用", NestedScrollingActivity.class));
        mLists.add(new Item("APT", AptActivity.class));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Item item = mLists.get(position);
        if (item.isHeader || item.getaClass() == null) {
            return;
        }
        startActivity(this, item.getaClass());
    }

}
