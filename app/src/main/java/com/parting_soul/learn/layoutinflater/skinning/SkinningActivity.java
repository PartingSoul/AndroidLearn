package com.parting_soul.learn.layoutinflater.skinning;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.parting_soul.base.AbstractActivity;
import com.parting_soul.learn.R;
import com.parting_soul.support.utils.FileUtils;
import com.parting_soul.support.utils.SPUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 换肤
 *
 * @author parting_soul
 * @date 2019-10-11
 */
public class SkinningActivity extends AbstractActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SkinManager.getInstance().attach(this);
        super.onCreate(savedInstanceState);
        cacheSkinResourceOnDisk();

        Toolbar toolbar = findViewById(R.id.mToolBar);
        setSupportActionBar(toolbar);

    }

    private void cacheSkinResourceOnDisk() {
        String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        outPut("darkSkin.skin", path + "/darkSkin.skin");
        outPut("redSkin.skin", path + "/redSkin.skin");
    }

    public void outPut(String name, String outputPath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open(name);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            FileOutputStream fos = new FileOutputStream(outputPath);
            fos.write(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeQuietly(baos);
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_skinning;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        setSwipeBackEnable(false);
        String skinPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/darkSkin.skin";
        isNightMode = TextUtils.equals(SPUtil.getString(SkinManager.KEY_SKIN_PATH), skinPath);
    }

    boolean isNightMode;

    public void changeSkin(View v) {
        isNightMode = !isNightMode;
        SkinManager.getInstance().changeNightMode(this, isNightMode);
    }

}
