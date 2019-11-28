package com.parting_soul.learn.apt;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parting_soul.annotation.Test;
import com.parting_soul.learn.R;

/**
 * @author parting_soul
 * @date 2019-11-18
 */
@Test
public class AptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apt);
    }

}
