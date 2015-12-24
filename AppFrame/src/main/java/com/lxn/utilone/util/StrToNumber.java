package com.lxn.utilone.util;

/**
 * String转数字的转换工具类
 */
public class StrToNumber {
	/**
	 * 字符串转int 如果字符串为空或装换异常 返回0
	 * @param src
	 * @return
	 */
	public static int strToint(String src) {
		if(null==src||src.equals("")){
			return 0;
		}
		int number=0;
		try {
			number=Integer.parseInt(src);
		} catch (Exception e) {
			return 0;
		}
		return number;
	}
	/**
	 * 字符串转long 如果字符串为空或装换异常 返回0
	 * @param src
	 * @return
	 */
	public static long strTolong(String src) {
		if(null==src||src.equals("")){
			return 0;
		}
		long number=0;
		try {
			number=Long.parseLong(src);
		} catch (Exception e) {
			return 0;
		}
		return number;
	}
	/**
	 * 字符串转double 如果字符串为空或装换异常 返回0
	 * @param src
	 * @return
	 */
	public static double strTodouble(String src) {
		if(null==src||src.equals("")){
			return 0;
		}
		double number=0.0;
		try {
			number=Double.parseDouble(src);
		} catch (Exception e) {
			return 0;
		}
		return number;
	}
}
