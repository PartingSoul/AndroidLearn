package com.parting_soul.support.widget.imgpick;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.parting_soul.support.Config;
import com.parting_soul.support.utils.AvoidResultManager;
import com.parting_soul.support.utils.ImageTools;

import java.io.File;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

/**
 * 系统原生拍照和选取照片
 *
 * @author parting_soul
 * @date 2018/11/21
 */
public class SystemNativeImagePickerStrategy implements IImagePickerStrategy {
    private AvoidResultManager mAvoidResultManager;
    private FragmentActivity mActivity;

    public SystemNativeImagePickerStrategy(FragmentActivity activity) {
        this.mActivity = activity;
        mAvoidResultManager = new AvoidResultManager(mActivity);
    }

    @Override
    public void takePhotos(final PicturePickDialog.OnGetPictureCallback callback) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File outDir = new File(Config.FilePath.FILE_CACHE_PATH);
        if (!outDir.exists())
            outDir.mkdirs();
        final String strFileName = Config.FilePath.FILE_CACHE_PATH + "/upload" + System.currentTimeMillis() + ".jpg";
        File outFile = new File(strFileName);

        Uri uri = Uri.fromFile(outFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            uri = FileProvider.getUriForFile(mActivity, Config.AUTHORITY_FILE_PROVIDER, outFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);

        mAvoidResultManager.startForResult(intent, 0x113, new AvoidResultManager.OnResultCallback() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    //得到照片文件路径
                    if (callback != null) {
                        callback.onResult(strFileName);
                    }
                }
            }
        });
    }

    @Override
    public void obtainPictures(final PicturePickDialog.OnGetPictureCallback callback) {
        Intent intent = new Intent();
        /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //注意要添加 要返回值，不然为空
        intent.putExtra("return-data", true);
        /* 取得相片后返回本画面 */
        mAvoidResultManager.startForResult(intent, 0x112, new AvoidResultManager.OnResultCallback() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    //得到照片文件路径
                    String filePath = ImageTools.getFilePath(mActivity, uri);
                    if (callback != null) {
                        callback.onResult(filePath);
                    }
                }
            }
        });
    }

}
