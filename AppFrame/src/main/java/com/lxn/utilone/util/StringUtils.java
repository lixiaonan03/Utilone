package com.lxn.utilone.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	private static Pattern numericPattern = Pattern.compile("^[0-9\\-]+$");
	private static Pattern abcPattern = Pattern.compile("^[a-z|A-Z]+$");
	
	/**
	 * 判断是否数字表示
	 * @param src 源字符串
	 * @return 是否数字的标志
	 */
	public static boolean isNumeric(String src) {
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = numericPattern.matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}
	
	/**
	 * 判断是否纯字母组合
	 * @param src 源字符串
	 * @return 是否纯字母组合的标志
	 */
	public static boolean isABC(String src) {
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = abcPattern.matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}
	
	/**
	 * 截取字符串　超出的字符用symbol代替 　　
	 * @param len 字符串长度　长度计量单位为一个GBK汉字　　两个英文字母计算为一个单位长度
	 * @param str
	 * @param symbol
	 * @return
	 */
	public static String getLimitLengthString(String str, int len, String symbol) {
		int iLen = len * 2;
		int counterOfDoubleByte = 0;
		String strRet = "";
		try {
			if (str != null) {
				byte[] b = str.getBytes("GBK");
				if (b.length <= iLen) {
					return str;
				}
				for (int i = 0; i < iLen; i++) {
					if (b[i] < 0) {
						counterOfDoubleByte++;
					}
				}
				if (counterOfDoubleByte % 2 == 0) {
					strRet = new String(b, 0, iLen, "GBK") + symbol;
					return strRet;
				} else {
					strRet = new String(b, 0, iLen - 1, "GBK") + symbol;
					return strRet;
				}
			} else {
				return "";
			}
		} catch (Exception ex) {
			return str.substring(0, len);
		} finally {
			strRet = null;
		}
	}

	/**
	 * 截取字符串　超出的字符用symbol代替 　　
	 * @param len 字符串长度　长度计量单位为一个GBK汉字　　两个英文字母计算为一个单位长度
	 * @param str
	 * @param symbol
	 * @return12
	 */
	public static String getLimitLengthString(String str, int len) {
		return getLimitLengthString(str, len, "...");
	}
	
	/**
	 * 取得字符串的实际长度（考虑了汉字的情况）
	 * @param SrcStr 源字符串
	 * @return 字符串的实际长度
	 */
	public static int getStringLen(String SrcStr) {
		int return_value = 0;
		if (SrcStr != null) {
			char[] theChars = SrcStr.toCharArray();
			for (int i = 0; i < theChars.length; i++) {
				return_value += (theChars[i] <= 255) ? 1 : 2;
			}
		}
		return return_value;
	}
	
	public static boolean isNotBlank(String s){
		if(s!=null && !s.trim().equals("")){
			return true;
		}
		return false;
	}
	
	public static boolean isBlank(String s){
		if(s==null || s.trim().equals("")){
			return true;
		}
		return false;
	}
}
