package com.parting_soul.learn;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @author parting_soul
 * @date 2019-10-17
 */
public class ActivityTwo extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        dumpTaskAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("ACTIVITY_TAG", " ActivityOne onStart ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("ACTIVITY_TAG", "ActivityOne onRestart ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ACTIVITY_TAG", "ActivityOne onResume ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("ACTIVITY_TAG", "ActivityOne onPause ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("ACTIVITY_TAG", "ActivityOne onStop ");
    }

    @Override
    public void finish() {

        getIntent().putExtra("data", "message");
        setResult(Activity.RESULT_OK, getIntent());

        Log.e("ACTIVITY_TAG", "ActivityOne finish ");
        super.finish();
    }


    @Override
    public void onBackPressed() {
        Log.e("ACTIVITY_TAG", "ActivityOne onBackPressed ");
        super.onBackPressed();
    }


    protected void dumpTaskAffinity() {
        try {
            ActivityInfo info = this.getPackageManager()
                    .getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            Log.i("ACTIVITY_TAG TWO", "taskAffinity:" + info.taskAffinity + " " + getTaskId());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
