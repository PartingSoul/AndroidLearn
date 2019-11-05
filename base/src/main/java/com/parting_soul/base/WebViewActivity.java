package com.parting_soul.base;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

import com.parting_soul.support.utils.WebViewDelegate;
import com.parting_soul.support.widget.TitleBar;

/**
 * @author parting_soul
 * @date 2019/4/30
 */
public class WebViewActivity extends AbstractActivity {
    private WebViewDelegate mWebViewDelegate = new WebViewDelegate();
    private TitleBar mTitleBar;
    private WebView mWebView;
    public static final String EXTRA_KEY_URL = "extra_key_url";
    public static final String EXTRA_KEY_TITLE = "extra_key_title";
    private String url;

    public static void start(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_KEY_URL, url);
        intent.putExtra(EXTRA_KEY_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_webview;
    }

    @Override
    protected void initData() {
        mWebViewDelegate.loadUrl(mWebView, url);
    }

    @Override
    protected void initView() {
        mTitleBar = findViewById(R.id.mTitleBar);
        mWebView = findViewById(R.id.mWebView);

        String title = getIntent().getStringExtra(EXTRA_KEY_TITLE);
        url = getIntent().getStringExtra(EXTRA_KEY_URL);
        mTitleBar.setTitle(title);
        mTitleBar.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebViewDelegate.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mWebViewDelegate.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

}
