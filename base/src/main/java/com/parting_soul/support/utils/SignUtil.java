package com.parting_soul.support.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @Description: 生成签名
 * @Author: xianggu
 * @CreateDate: 2019/1/2 8:49 PM
 */
public class SignUtil {


    static private String salt = "ed732d7bf0e0470fb729dcf199d6b919";

    public static boolean checkSign(Map<String, String> params, String sign) {
        String signText = sign(params);
        return signText.equals(sign);
    }

    public static String sign(Map<String, String> params) {
        params.put("salt", salt);
        String orderText = getOrderText(params);
        return MD5Util.md5(orderText);
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private static String getOrderText(Map<String, String> params) {

        List<Map.Entry<String, String>> infoIds = new ArrayList<>(params.entrySet());
        // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        StringBuilder sb = new StringBuilder();
        int a = 0;
        for (Map.Entry<String, String> item : infoIds) {
            String key = item.getKey();
            String val = item.getValue();
            if (!"".equals(val)) {
                if (a == 0) {
                    sb.append(key).append("=").append(val);
                } else {
                    sb.append("&").append(key).append("=").append(val);
                }
            }
            a++;
        }
        return sb.toString();
    }


    /**
     * 循环加盐值加密
     *
     * @param params
     * @return
     */
    public static String signLoop(Map<String, String> params) {
        params.put("salt", salt);
        String orderText = getOrderText(params);
        String sign = MD5Util.md5(orderText);

        for (int i = 0; i < 10; i++) {
            sign = MD5Util.md5(sign + salt);
        }

        return sign;
    }

}
