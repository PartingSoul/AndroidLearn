package com.parting_soul.learn;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity难点
 *
 * @author parting_soul
 * @date 2019-12-25
 */
public class CommonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_common);

        startActivityForResult(new Intent(this, ActivityOne.class), 0x12);
//        startActivity(new Intent(this, ActivityOne.class));
        dumpTaskAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Log.e("ACTIVITY_TAG", "onActivityResult " + data.getStringExtra("data"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("ACTIVITY_TAG", "CommonActivity onStart ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("ACTIVITY_TAG", "CommonActivity onRestart ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ACTIVITY_TAG", "CommonActivity onResume ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("ACTIVITY_TAG", "CommonActivity onPause ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("ACTIVITY_TAG", "CommonActivity onStop ");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("data", "data");
        Log.e("ACTIVITY_TAG", "CommonActivity onSaveInstanceState " + outState);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("ACTIVITY_TAG", "CommonActivity onRestoreInstanceState ");
    }

    protected void dumpTaskAffinity() {
        try {
            ActivityInfo info = this.getPackageManager()
                    .getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            Log.i("ACTIVITY_TAG", "taskAffinity:" + info.taskAffinity + " " + getTaskId());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
