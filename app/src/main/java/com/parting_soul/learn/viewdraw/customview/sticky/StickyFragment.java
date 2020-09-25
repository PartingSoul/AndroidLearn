package com.parting_soul.learn.viewdraw.customview.sticky;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.parting_soul.base.AbstractFragment;
import com.parting_soul.learn.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author parting_soul
 * @date 2020-09-24
 */
public class StickyFragment extends AbstractFragment {
    private RecyclerView mRv;
    private BaseQuickAdapter<String, BaseViewHolder> mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.frg_sticky;
    }

    @Override
    protected void initView() {
        mRv = getView(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_simple_item) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_text, item);
            }
        };
        mRv.setAdapter(mAdapter);
    }

    @Override
    protected void loadData() {
        List<String> lists = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            lists.add("android " + i);
        }
        mAdapter.setNewData(lists);
    }

}
