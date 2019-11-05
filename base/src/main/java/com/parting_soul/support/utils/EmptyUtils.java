package com.parting_soul.support.utils;

import java.util.Collection;

import androidx.annotation.Nullable;

/**
 * @author paddington
 * 2019/6/19
 */
public class EmptyUtils {
    public static boolean assertEmpty(@Nullable Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean assertEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }

    public static boolean assertNotEmpty(@Nullable Collection collection) {
        return !assertEmpty(collection);
    }

    public static boolean assertNotEmpty(@Nullable String string) {
        return !assertEmpty(string);
    }
}
