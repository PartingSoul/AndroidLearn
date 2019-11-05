package com.parting_soul.learn.bean;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * @author parting_soul
 * @date 2019/1/8
 */
public class Item extends SectionEntity<String> {
    private Class<?> aClass;

    public Item(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public Item(String message, Class<?> clazz) {
        super(message);
        this.aClass = clazz;
    }

    public Class<?> getaClass() {
        return aClass;
    }
}
