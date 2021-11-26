package com.lxn.utilone.util.toolutil;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * 作者：lxn on 2015/12/16 14:25
 * 描述：Utilone ————数字金额运算的工具类
 */
public class MathUtil {
    //默认获取的小数点的后2位
    private static final int FORMAT = 2;

    /**
     * BigDecimal类型的数字和int 类型相乘之后的截取小数点后2位的结果
     *
     * @param price BigDecimal类型 eg:商品单价
     * @param count int类型  eg:商品数量
     * @return
     */
    public static BigDecimal getMultiply(BigDecimal price, int count) {
        BigDecimal allprice = price.multiply(new BigDecimal(count));
        allprice = subDigit(allprice, FORMAT);
        return allprice;
    }

    /**
     * BigDecimal类型的数字和int 类型相乘之后以四舍五入的方式取整
     *
     * @param price BigDecimal类型 eg:商品单价
     * @param count int类型  eg:商品数量
     * @return 整数结果BigDecimal类型
     */
    public static BigDecimal getMultiplyToInt(BigDecimal price, int count) {
        BigDecimal allprice = price.multiply(new BigDecimal(count));
        allprice = subDigit(allprice, 0);
        return allprice;
    }

    /**
     * BigDecimal类型的数字和int 类型相乘之后
     *
     * @param price BigDecimal类型 eg:商品单价
     * @param count int类型  eg:商品数量
     * @param digit int类型  获取小数点后几位
     * @return
     */
    public static BigDecimal getMultiply(BigDecimal price, int count, int digit) {
        BigDecimal allprice = price.multiply(new BigDecimal(count));
        allprice = subDigit(allprice, digit);
        return allprice;
    }

    /**
     * 截取到小数点后几位 (直接截取)
     *
     * @param price BigDecimal类型的数字
     * @param digit 要获取的小数点后几位
     * @return
     */
    public static BigDecimal subDigit(BigDecimal price, int digit) {
        BigDecimal returnBigdecimal = price.setScale(digit, BigDecimal.ROUND_DOWN);
        return returnBigdecimal;
    }


    /**
     * 截取到小数点后几位 ()
     *
     * @param price BigDecimal类型的数字
     * @param digit 要获取的小数点后几位
     * @return
     */
    public static BigDecimal subDigitRu(BigDecimal price, int digit) {
        BigDecimal returnBigdecimal = price.setScale(digit, BigDecimal.ROUND_HALF_UP);
        return returnBigdecimal;
    }

    /**
     * 把一个BigDecimal的数字  转换成百分比显示的(只保留整数 后面的直接舍去)
     *
     * @param bigDecimal 数字
     * @return eg: 0.0123 返回1
     */
    public static String bigDecimalToPercentNoDecimal(BigDecimal bigDecimal) {
        bigDecimal = bigDecimal.multiply(new BigDecimal("100"));
        //保留2位小数后面的直接省去
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
        DecimalFormat df1 = new DecimalFormat("#0");
        return df1.format(bigDecimal);
    }

    /**
     * 把一个BigDecimal的数字  转换成百分比显示的(只保留一位小数 后面的直接舍去)
     *
     * @param bigDecimal 数字
     * @return eg 0.0123 返回1.2
     */
    public static String bigDecimalToPercentDecimalOne(BigDecimal bigDecimal) {
        bigDecimal = bigDecimal.multiply(new BigDecimal("100"));
        //保留3位小数后面的直接省去
        bigDecimal = bigDecimal.setScale(3, BigDecimal.ROUND_DOWN);
        DecimalFormat df1 = new DecimalFormat("#0.0");
        return df1.format(bigDecimal);
    }

    /**
     * 把一个BigDecimal的数字  转换成百分比显示的(只保留2位小数 后面的直接舍去)
     *
     * @param bigDecimal 数字
     * @return eg 0.0123 返回1.23
     */
    public static String bigDecimalToPercentDecimalTwo(BigDecimal bigDecimal) {
        bigDecimal = bigDecimal.multiply(new BigDecimal("100"));
        //保留4位小数后面的直接省去
        bigDecimal = bigDecimal.setScale(4, BigDecimal.ROUND_DOWN);
        DecimalFormat df1 = new DecimalFormat("#0.00");
        return df1.format(bigDecimal);
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        return v1.subtract(v2);
    }

    /**
     * 判断一个数是否大于0
     *
     * @param bigDecimal
     * @return
     */
    public static boolean isGreaterThan0(BigDecimal bigDecimal) {
        if (null == bigDecimal) {
            return false;
        }
        //结果是:-1 小于,0 等于,1 大于
        int compare = bigDecimal.compareTo(new BigDecimal("0"));
        if (0 < compare) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 把一个double 数字转换成 小数点后2位显示的
     *
     * @param math double 数字
     * @return String
     */
    public static String mathTodecimaltwo(double math) {
        BigDecimal b = new BigDecimal(String.valueOf(math));
        //小数位 直接舍去
        b = b.setScale(2, BigDecimal.ROUND_DOWN);
        return b.toString();
    }

    /**
     * 金额展示2位小数
     *
     * @param math 金额
     * @return String
     */
    public static String moneyForStringTwoDecimal(BigDecimal math) {
        if (null == math) {
            return "0.00";
        }
        //小数位 直接舍去
        math = math.setScale(2, BigDecimal.ROUND_DOWN);
        return math.toString();
    }

    /**
     * 金额展示2位小数
     *
     * @param math 金额
     * @return String
     */
    public static String moneyForStringTwoDecimalByHALFUP(BigDecimal math) {
        if (null == math) {
            return "0.00";
        }
        //小数位 直接舍去
        math = math.setScale(2, BigDecimal.ROUND_HALF_UP);
        return math.toString();
    }

    /**
     * 金额取整
     *
     * @param math 金额
     * @return String
     */
    public static String moneyForStringNoDecimal(BigDecimal math) {
        if (null == math) {
            return "0";
        }
        //小数位 直接舍去
        math = math.setScale(0, BigDecimal.ROUND_DOWN);
        return math.toString();
    }

    /**
     * 把一个String转换成 小数点后2位显示的
     *
     * @param math 数字
     * @return String
     */
    public static String mathTodecimaltwo(String math) {
        BigDecimal b = new BigDecimal(math.trim());
        //小数位 直接舍去
        b = b.setScale(2, BigDecimal.ROUND_DOWN);
        return b.toString();
    }
}