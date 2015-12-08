
package com.lxn.utilone.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * @Description:日期处理的工具类
 * @time:To014年9月19日 上午9:13:16
 */
public class DateUtil {

	private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    
	/**
	 * @Description:String转换成Date 用默认的格式 yyyy-MM-dd HH:mm:ss
	 * @param str
	 * @return Date类型时间
	 * @exception:
	 * @time:To014年9月19日 上午9:16:16
	 */
	public static Date strToDate(String str) {
		return strToDate(str, null);
	}
    /**
     * @Description:String转Date时间  自己定义格式
     * @param str
     * @param format 格式
     * @return
     * Date
     * @exception:
     * @time:9月19日 上午9:18
     */
	public static Date strToDate(String str, String format) {
		if (str == null || str.length() == 0) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Calendar strToCalendar(String str) {
		return strToCalendar(str, null);
	}

	public static Calendar strToCalendar(String str, String format) {
		Date date = strToDate(str, format);
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}
	 /**
     * @Description:Calendar时间 转字符串 使用默认格式yyyy-MM-dd HH:mm:ss
     * @param calendar
     * @param format
     */
	public static String calendarToStr(Calendar calendar) {
		return calendarToStr(calendar, null);
	}
    /**
     * @Description:Calendar时间 转字符串
     * @param calendar
     * @param format
     */
	public static String calendarToStr(Calendar calendar, String format) {
		if (calendar == null) {
			return null;
		}
		return dateToStr(calendar.getTime(), format);
	}
    /**
     * @Description:Date类型的时间转 String 使用默认格式yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
	public static String dateToStr(Date date) {
		return dateToStr(date, null);
	}
	/**
	 * @Description:Date类型的时间转 String
	 * @param date 时间
	 * @param format 格式
	 */
	public static String dateToStr(Date date, String format) {
		if (date == null) {
			return null;
		}
		if (format == null || format.length() == 0) {
			format = FORMAT;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String s = sdf.format(date);
		return s;
	}

	public static String getCurDateStr() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
				+ c.get(Calendar.DAY_OF_MONTH) + "-"
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
				+ ":" + c.get(Calendar.SECOND);
	}

	/**
	 * 获得当前日期的字符串格式
	 * @param format
	 * @return
	 */
	public static String getCurDateStr(String format) {
		Calendar calendar = Calendar.getInstance();
		return calendarToStr(calendar, format);
	}

	/**
	 * @Description:把long的时间 转成字符串格式 yyyy-MM-dd HH:mm:ss
	 * @param time
	 */
	public static String getDateTime(long time) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
	}
	/**
	 * @Description:把long的时间 转成字符串格式 yyyy-MM-dd
	 * @param time
	 */
	public static String getDate(long time) {
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	/**
	 * @Description:把Timestamp的时间 转成字符串格式 yyyy-MM-dd
	 * @param time
	 */
	public static String getDate(Timestamp time) {
		return new SimpleDateFormat("yyyy-MM-dd").format(time);
	}
	public static String getDateTime(Timestamp time) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
	}
	
	public static String getNow() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(System.currentTimeMillis());
	}

	public static String getNowDateDetail() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(System.currentTimeMillis());
	}
}
