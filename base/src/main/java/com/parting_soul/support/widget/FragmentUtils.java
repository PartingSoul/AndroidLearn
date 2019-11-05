package com.parting_soul.support.widget;


import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

/**
 * @author parting_soul
 * @date 2018/9/3
 */
public class FragmentUtils {

    /**
     * 显示和隐藏Fragment
     *
     * @param fragmentManager
     * @param containerViewId
     * @param fragment
     */
    public static void showHideFragment(FragmentManager fragmentManager, @IdRes int containerViewId, Fragment fragment) {
        if (!fragment.isAdded()) {
            //若Fragment没有被添加
            fragmentManager
                    .beginTransaction()
                    .add(containerViewId, fragment)
                    .commit();
            fragmentManager.executePendingTransactions();
        }

        //已经添加则显示
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment f : fragments) {
            if (f == fragment) {
                transaction.show(f);
            } else {
                transaction.hide(f);
            }
        }
        transaction.commit();
    }

}
