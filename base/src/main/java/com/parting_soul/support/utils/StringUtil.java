package com.parting_soul.support.utils;

import android.text.TextUtils;

import androidx.annotation.StringRes;

import com.parting_soul.base.BaseApplication;

import java.text.DecimalFormat;
import java.util.List;


/**
 * @author : ciba
 * @date : 2018/9/10
 * @description : replace your description
 */

public class StringUtil {
    /**
     * 将专长集合转化为以空格分割的文字（最多只展示三个专长）
     *
     * @param specialities
     * @return
     */
    public static String getSpecialities(List<String> specialities) {
        String speciality = "";
        if (specialities != null && !specialities.isEmpty()) {
            for (int i = 0; i < specialities.size(); i++) {
                if (i >= 3) {
                    break;
                }
                String sp = specialities.get(i);
                speciality = speciality + sp + " ";
            }
        }
        return speciality;
    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    public static String getTwoDecimalsValue(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
    }

    /**
     * 获取数量，超过1000 用k表示
     *
     * @param num
     * @return
     */
    public static String getNum(int num) {
        //取千位数
        int multi = num / 1000;
        if (multi == 0) {
            return String.valueOf(num);
        }

        //取百位数
        int secondMulti = num % 1000 / 100;
        if (secondMulti == 0) {
            return multi + "k";
        }
        return multi + "." + secondMulti + "k";
    }

    /**
     * 获取数量
     *
     * @param num
     * @return
     */
    public static int parseStringToInt(String num) {
        int integer = 0;
        try {
            integer = Integer.parseInt(num);
        } catch (Exception e) {
        }
        return integer;
    }

    /**
     * 获取数量
     *
     * @param num
     * @return
     */
    public static double parseStringToDouble(String num) {
        double integer = 0;
        try {
            integer = Double.parseDouble(num);
        } catch (Exception e) {
        }
        return integer;
    }


    /**
     * 超过999 返回999+
     *
     * @param num
     * @return
     */
    public static String getNumLessThan999(int num) {
        String result = String.valueOf(num);
        if (num > 999) {
            result = "999+";
        }
        return result;
    }

    public static boolean isPhoneNum(String phone) {
        return !TextUtils.isEmpty(phone) && phone.length() == 11;
    }

    /**
     * 以指定字符拼接id
     *
     * @return
     */
    public static String jointIdSplitWithChar(List<String> ids, char split) {
        if (ids == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String path : ids) {
            builder.append(path)
                    .append(split);
        }
        if (builder.length() > 0) {
            builder.replace(builder.length() - 1, builder.length(), "");
        }
        return builder.toString();
    }

    public static String format(@StringRes int resId, Object... args) {
        String format = BaseApplication.getAppContext().getString(resId);
        return String.format(format, args);
    }

    public static String getString(@StringRes int resId) {
        return BaseApplication.getAppContext().getString(resId);
    }


    /**
     * 加密电话
     *
     * @param phone
     * @return
     */
    public static String encryptPhone(String phone) {
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            return phone;
        }
        return phone.replaceAll("(?<=\\d{3})\\d(?=\\d{4})", "*");
    }

    public static String getNovelCount(int wordCount) {
        String wordCountStr = "";
        if (wordCount < 10000) {
            wordCountStr = String.valueOf(wordCount);
        } else {
            wordCountStr = (wordCount / 10000) + "万";
        }
        wordCountStr += "字";
        return wordCountStr;
    }
}
