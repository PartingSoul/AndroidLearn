package com.parting_soul.support.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author parting_soul
 * @date 2019/4/22
 */
public abstract class SimpleTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
