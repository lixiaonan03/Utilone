package com.lxn.utilone.util.toolutil;

import java.math.BigDecimal;

/**
  *  @author lixiaonan
  *  功能描述: 文本转数字的工具类
  *  时 间： 2019-11-02 12:47
  */
public class StrToNumberUtil {
    /**
     * 字符串转int 如果字符串为空或装换异常 返回0
     * @param src
     * @return
     */
    public static int strToint(String src) {
        if (null == src || "".equals(src)) {
            return 0;
        }
        int number = 0;
        try {
            number = Integer.parseInt(src);
        } catch (Exception e) {
            return 0;
        }
        return number;
    }

    /**
     * 字符串转long 如果字符串为空或装换异常 返回0
     *
     * @param src
     * @return
     */
    public static long strTolong(String src) {
        if (null == src || "".equals(src)) {
            return 0;
        }
        long number = 0;
        try {
            number = Long.parseLong(src);
        } catch (Exception e) {
            return 0;
        }
        return number;
    }

    /**
     * 字符串转double 如果字符串为空或装换异常 返回0
     *
     * @param src
     * @return
     */
    public static double strTodouble(String src) {
        if (null == src || "".equals(src)) {
            return 0;
        }
        double number = 0.0;
        try {
            number = Double.parseDouble(src);
        } catch (Exception e) {
            return 0;
        }
        return number;
    }

    /**
     * 字符串转float 如果字符串为空或装换异常 返回0
     *
     * @param src
     * @return
     */
    public static float strTofloat(String src) {
        if (null == src || "".equals(src)) {
            return 0;
        }
        float number = 0;
        try {
            number = Float.parseFloat(src);
        } catch (Exception e) {
            return 0;
        }
        return number;
    }
    /**
     * 字符串转BigDecimal 如果字符串为空或装换异常 返回0
     *
     * @param src
     * @return
     */
    public static BigDecimal strToBigDecimal(String src) {
        BigDecimal  result;
        if (null == src || "".equals(src)) {
            return new BigDecimal("0");
        }
        try {
            result = new BigDecimal(src);
        } catch (Exception e) {
            return new BigDecimal("0");
        }
        return result;
    }
}
