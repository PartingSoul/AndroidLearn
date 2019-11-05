package com.parting_soul.support.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author : ciba
 * @date : 2018/5/31
 * @description : replace your description
 */

public class MD5Util {
    public static String md5(String str) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            byte[] encodedValue = md5.digest();
            int j = encodedValue.length;
            char[] finalValue = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte encoded = encodedValue[i];
                finalValue[k++] = hexDigits[encoded >> 4 & 0xf];
                finalValue[k++] = hexDigits[encoded & 0xf];
            }
            return new String(finalValue);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str;
    }
}