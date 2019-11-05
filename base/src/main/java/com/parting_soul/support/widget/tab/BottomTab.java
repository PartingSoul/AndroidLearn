package com.parting_soul.support.widget.tab;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.parting_soul.base.BaseApplication;


/**
 * 底部Tab
 *
 * @author parting_soul
 * @date 2018/7/13
 */
public class BottomTab<T> {
    private final int selectIcon;
    private final int unSelectIcon;
    private final String title;
    private T data;

    public BottomTab(@DrawableRes int selectIcon, @DrawableRes int unSelectIcon, @StringRes int titleId, T data) {
        this(selectIcon, unSelectIcon, BaseApplication.getAppContext().getResources().getString(titleId), data);
    }

    public BottomTab(@DrawableRes int selectIcon, @DrawableRes int unSelectIcon, String title, T data) {
        this.selectIcon = selectIcon;
        this.unSelectIcon = unSelectIcon;
        this.title = title;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getSelectIcon() {
        return selectIcon;
    }

    public int getUnSelectIcon() {
        return unSelectIcon;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != obj.getClass()) return false;
        BottomTab tab = (BottomTab) obj;
        return super.equals(obj)
                || selectIcon == tab.selectIcon
                && unSelectIcon == tab.unSelectIcon
                && title.equals(tab.title);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + selectIcon;
        result = prime * result + unSelectIcon;
        result = prime * result + title.hashCode();
        return result;
    }

}
