package com.parting_soul.support.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.parting_soul.base.R;
import com.parting_soul.support.utils.DensityUtil;
import com.parting_soul.support.utils.ImmersionUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


/**
 * @author : ciba
 * @date : 2018/9/6
 * @description : 透明和白色的标题栏状态切换控件
 */

public class AlphaNormalStatusTitleBar extends FrameLayout implements View.OnClickListener {
    private final View alphaTitle;
    private final View normalTitle;
    private final View llAlphaShare;
    private final View llNormalShare;
    private final View statusBarBackgroundView;
    private final View mNormalShareMore;
    private final View mAlphaShareMore;
    private final View mNormalShareToWxMoment;
    private final View mAlphaShareToWxMoment;
    private final View mNormalShareToWxFriend;
    private final View mAlphaShareToWxFriend;
    private ShareClickListener shareClickListener;
    private int offY = 0;
    private boolean onlyAlphaStatus;
    private boolean onlyNormalStatus;
    private BackClickListener backClickListener;
    private ScrollChangedListener scrollChangedListener;
    private int titleBarPaddingBottom;

    public AlphaNormalStatusTitleBar(@NonNull Context context) {
        this(context, null);
    }

    public AlphaNormalStatusTitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaNormalStatusTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        titleBarPaddingBottom = DensityUtil.dip2px(context, 8);
        alphaTitle = LayoutInflater.from(getContext()).inflate(R.layout.layout_alpha_title_bar_with_share, null, false);
        llAlphaShare = alphaTitle.findViewById(R.id.llAlphaShare);
        mAlphaShareMore = alphaTitle.findViewById(R.id.ivAlphaMoreShare);
        mAlphaShareToWxMoment = alphaTitle.findViewById(R.id.ivAlphaWxMomentShare);
        mAlphaShareToWxFriend = alphaTitle.findViewById(R.id.ivAlphaWxFriendShare);


        alphaTitle.findViewById(R.id.ivAlphaBack).setOnClickListener(this);
        alphaTitle.findViewById(R.id.ivAlphaWxFriendShare).setOnClickListener(this);
        alphaTitle.findViewById(R.id.ivAlphaWxMomentShare).setOnClickListener(this);
        alphaTitle.findViewById(R.id.ivAlphaMoreShare).setOnClickListener(this);
        addView(alphaTitle);

        normalTitle = LayoutInflater.from(getContext()).inflate(R.layout.layout_normal_title_bar, null, false);
        llNormalShare = normalTitle.findViewById(R.id.llNormalShare);
        mNormalShareMore = normalTitle.findViewById(R.id.ivNormalMoreShare);
        mNormalShareToWxMoment = normalTitle.findViewById(R.id.ivNormalWxMomentShare);
        mNormalShareToWxFriend = normalTitle.findViewById(R.id.ivNormalWxFriendShare);
        statusBarBackgroundView = normalTitle.findViewById(R.id.statusBarBackgroundView);
        if (Build.VERSION.SDK_INT >= 21) {
            int height = ImmersionUtils.getStatusBarHeightWithTranslucentStatus(context);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) statusBarBackgroundView.getLayoutParams();
            params.height = height;
            normalTitle.setPadding(0, 0, 0, titleBarPaddingBottom);
            alphaTitle.setPadding(0, height, 0, titleBarPaddingBottom);
        }

        normalTitle.findViewById(R.id.ivNormalBack).setOnClickListener(this);
        normalTitle.findViewById(R.id.ivNormalWxFriendShare).setOnClickListener(this);
        normalTitle.findViewById(R.id.ivNormalWxMomentShare).setOnClickListener(this);
        normalTitle.findViewById(R.id.ivNormalMoreShare).setOnClickListener(this);
        normalTitle.setVisibility(GONE);
        addView(normalTitle);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ivAlphaBack || i == R.id.ivNormalBack) {
            backClick();
        } else if (i == R.id.ivAlphaWxFriendShare || i == R.id.ivNormalWxFriendShare) {
            wxFriendShareClick();
        } else if (i == R.id.ivAlphaWxMomentShare || i == R.id.ivNormalWxMomentShare) {
            wxMomentShare();
        } else if (i == R.id.ivAlphaMoreShare || i == R.id.ivNormalMoreShare) {
            moreShareClick();
        } else {
        }
    }

    /**
     * 更多分享点击事件
     */
    private void moreShareClick() {
        if (shareClickListener != null) {
            shareClickListener.onMoreShareClick();
        }
    }

    /**
     * 微信朋友圈分享点击事件
     */
    private void wxMomentShare() {
        if (shareClickListener != null) {
            shareClickListener.onWxMomentShareClick();
        }
    }

    /**
     * 微信好友分享点击事件
     */
    private void wxFriendShareClick() {
        if (shareClickListener != null) {
            shareClickListener.onWxFriendShareClick();
        }
    }

    /**
     * 返回按钮点击事件
     */
    private void backClick() {
        if (backClickListener != null) {
            backClickListener.onBackClick();
        } else {
            Context context = getContext();
            if (context != null && context instanceof Activity) {
                ((Activity) context).finish();
            }
        }
    }

    public void showHideOtherWithoutBack(boolean show) {
        llNormalShare.setVisibility(show ? VISIBLE : GONE);
        llAlphaShare.setVisibility(show ? VISIBLE : GONE);
        statusBarBackgroundView.setVisibility(show ? VISIBLE : GONE);
    }

    public void setStatusChanged(RecyclerView recyclerView, final int changedHeight) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                offY = offY + dy;
                changedStatus(changedHeight);
            }
        });
    }

    public void setStatusChanged(ScrollChangedScrollView scrollView, final int changedHeight) {
        scrollView.setOnScrollChangedListener(new ScrollChangedScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                offY = t;
                changedStatus(changedHeight);
            }
        });
    }

    private void changedStatus(int changedHeight) {
        if (scrollChangedListener != null) {
            scrollChangedListener.onScrollChanged(offY);
        }
        if (onlyAlphaStatus) {
            alphaTitle.setVisibility(VISIBLE);
            normalTitle.setVisibility(GONE);
        } else if (onlyNormalStatus) {
            alphaTitle.setVisibility(GONE);
            normalTitle.setVisibility(VISIBLE);
        } else {
            alphaTitle.setVisibility(offY > changedHeight ? GONE : VISIBLE);
            normalTitle.setVisibility(offY <= changedHeight ? GONE : VISIBLE);
        }
    }

    public void setShareClickListener(ShareClickListener shareClickListener) {
        this.shareClickListener = shareClickListener;
    }

    public void setOnlyAlphaStatus() {
        onlyAlphaStatus = true;
        alphaTitle.setVisibility(VISIBLE);
        normalTitle.setVisibility(GONE);
    }

    public void setOnlyNormalStatus() {
        onlyNormalStatus = true;
        alphaTitle.setVisibility(GONE);
        normalTitle.setVisibility(VISIBLE);
    }

    public void setBackClickListener(BackClickListener backClickListener) {
        this.backClickListener = backClickListener;
    }

    public void setScrollChangedListener(ScrollChangedListener scrollChangedListener) {
        this.scrollChangedListener = scrollChangedListener;
    }

    public void setEnableShareMore(boolean isEnable) {
        mNormalShareMore.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        mAlphaShareMore.setVisibility(isEnable ? View.VISIBLE : View.GONE);
    }

    public void setEnableShareToWxMoment(boolean isEnable) {
        mNormalShareToWxMoment.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        mAlphaShareToWxMoment.setVisibility(isEnable ? View.VISIBLE : View.GONE);
    }

    public void setEnableShareToWxFriend(boolean isEnable) {
        mNormalShareToWxFriend.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        mAlphaShareToWxFriend.setVisibility(isEnable ? View.VISIBLE : View.GONE);
    }

    public interface BackClickListener {
        void onBackClick();
    }

    public interface ShareClickListener {
        void onWxFriendShareClick();

        void onWxMomentShareClick();

        void onMoreShareClick();
    }

    public interface ScrollChangedListener {
        void onScrollChanged(int offY);
    }
}
