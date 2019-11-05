package com.parting_soul.learn;

import android.os.CountDownTimer;
import android.view.Window;
import android.widget.TextView;

import com.parting_soul.base.AbstractActivity;
import com.parting_soul.support.utils.LogUtils;

/**
 * @author parting_soul
 * @date 2019-09-30
 */
public class SplashActivity extends AbstractActivity {
    private TextView mTvSkip;

    private CountDownTimer mCountDownTimer = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mTvSkip.setText("跳过 " + (millisUntilFinished / 1000) + "s");
        }

        @Override
        public void onFinish() {
            mTvSkip.setText("跳过 0s");
            startActivity(SplashActivity.this, MainActivity.class);
            finish();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownTimer.cancel();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_splash;
    }

    @Override
    protected void initData() {
        setSwipeBackEnable(false);
        mTvSkip = findViewById(R.id.tv_skip);
        mCountDownTimer.start();

    }

    @Override
    protected void initView() {
        setSwipeBackEnable(false);
    }


    @Override
    protected boolean isSetHalfTransparentStatusBar() {
        return false;
    }

    @Override
    public void onBackPressed() {
    }

}
