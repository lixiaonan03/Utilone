package com.lxn.utilone.util.toolutil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
  *  @author lixiaonan
  *  功能描述: String处理的工具类的
  *  时 间： 2019-11-02 17:07
  */
public class StringUtil {
    /**
     * 判断一个字符串是否为空
     * @param s  入参
     * @return   为null 或 '' 为true
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为 null 或全为空白字符
     * @param s The string.
     * @return {@code true}: yes<br> {@code false}: no
     */
    public static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }



    /**
     * 判断一个String字符串 trim之后 是否为空的
     * @param str
     * @return
     */
    public static boolean isNotEmptyString(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        str = str.trim();
        return str.length() > 0;
    }
    /**
     * 判断一个字符串不为空
     * @param s
     * @return 不为空为true
     */
    public static boolean isNotEmpty(final CharSequence s) {
        return !(isEmpty(s));
    }

    /**
     * 比较2个字符串是否相等的
     * @param s1  入参1
     * @param s2  入参2
     * @return   相等返回true
     */
    public static boolean equals(final CharSequence s1, final CharSequence s2) {
        if (s1 == s2){
            return true;
        }
        int length;
        if (s1 != null && s2 != null && (length = s1.length()) == s2.length()) {
            if (s1 instanceof String && s2 instanceof String) {
                return s1.equals(s2);
            } else {
                for (int i = 0; i < length; i++) {
                    if (s1.charAt(i) != s2.charAt(i)){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * String中的字母变小写的
     * @param string 入参字母
     * @return  变换完成的
     */
    public static String toLowerString(String string){
        if(isEmpty(string)){
            return "";
        }
        return string.toLowerCase();
    }


    /**
     * 将字符串中的连续的多个换行缩减成一个换行
     * @param str  要处理的内容
     * @return	返回的结果
     */
    public static String replaceLineBlanks(String str) {
        String result = "";
        if (str != null) {
            Pattern p = compile("(\r?\n(\\s*\r?\n)+)");
            Matcher m = p.matcher(str);
            result = m.replaceAll("\r\n");
        }
        return result;
    }

    /**
     * 将字符串中的换行改成空格
     *
     * @param str 要处理的内容
     * @return 返回的结果
     */
    public static String replaceLineToTab(String str) {
        String result = "";
        if (str != null) {
            result = str.replace("\n", "\t");
        }
        return result;
    }


    /**
     * 把一段字符串中的除过数字和小数点的其他都去掉
     *
     * @param str
     * @return
     */
    public static String stringFilterForMoney(String str) {
        try {
            // 清除掉所有特殊字符
            str = str.replaceAll("[^\\d.]+", "");
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将数字以千为单位，用“，”分割。
     *
     * @param str1
     *            eg:123456.76
     * @return String str eg: 123,456.76  异常返回空字符串
     */
    public static String getMoneyString(String str1) {
        try {
            if(isEmpty(str1)){
                return "";
            }
            char firstWord = str1.charAt(0);
            if ("-".equals(firstWord + "")) {
                str1 = str1.replace("-", "");
            }
            String[] str = str1.split("\\.");
            str1 = str[0];
            // 先将字符串颠倒顺序
            str1 = new StringBuilder(str1).reverse().toString();
            String str2 = "";
            for (int i = 0; i < str1.length(); i++) {
                if (i * 3 + 3 > str1.length()) {
                    str2 += str1.substring(i * 3, str1.length());
                    break;
                }
                str2 += str1.substring(i * 3, i * 3 + 3) + ",";
            }
            if (str2.endsWith(",")) {
                str2 = str2.substring(0, str2.length() - 1);
            }
            // 最后再将顺序反转过来
            if (str.length > 1) {
                str1 = new StringBuilder(str2).reverse().toString() + "." + str[1];
            } else {
                str1 = new StringBuilder(str2).reverse().toString();
            }
            if ("-".equals(firstWord + "")) {
                str1 = "-" + str1;

            }
            return str1;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
