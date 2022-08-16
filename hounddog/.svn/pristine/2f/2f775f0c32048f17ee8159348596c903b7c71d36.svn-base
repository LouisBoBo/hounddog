package com.slxk.hounddog.mvp.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间转换类
 */
public class DateUtils {

	/**
	 * 其他语言显示时间转阿拉伯数字显示
	 * 阿拉伯数字时间用于传给服务器
	 *
	 * @param gmtStr
	 * @param formatPatternStr
	 * @return
	 */
	public static String GetOthersStrFromENGLISHStr(String gmtStr, String formatPatternStr, Locale locale) {
		if (TextUtils.isEmpty(formatPatternStr)) {
			formatPatternStr = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat format = new SimpleDateFormat(formatPatternStr, locale);
		Date dateTo;
		try {
			dateTo = format.parse(gmtStr);
		} catch (ParseException e) {
			e.printStackTrace();
			dateTo = new Date();
		}
		SimpleDateFormat formatTo = new SimpleDateFormat(formatPatternStr, Locale.ENGLISH);
		return formatTo.format(dateTo);
	}

	/**
	 * 一分钟的秒数
	 */
	private final static int MAX_SECOND = 60;

	/**
	 * 获取当前时间
	 *
	 * @return
	 */
	public static String getTodayDateTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.ENGLISH);
		return format.format(new Date());
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getTodayDateTime_2() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",
				Locale.ENGLISH);
		return format.format(new Date());
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getTodayDateTime_3() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
				Locale.ENGLISH);
		return format.format(new Date());
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getTodayDateTime_4() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日",
				Locale.ENGLISH);
		return format.format(new Date());
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getTodayDateTime_5() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm",
				Locale.ENGLISH);
		return format.format(new Date());
	}

	/**
	 * 时间格式 yyyy-MM-dd 转换成 yyyy年MM月dd日 格式
	 * @param time
	 * @return
	 */
	public static String onTimeFormatConversion(String time){
		if (TextUtils.isEmpty(time)){
			return "";
		}
		String oldTime = timeFormatConversion(time);
		return timeFormatConversion_2(oldTime);
	}

	/**
	 * 掉此方法输入所要转换的时间输入例如（"2014-06-14"）返回时间戳
	 *
	 * @param time
	 * @return
	 */
	public static String timeFormatConversion(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd",
				Locale.ENGLISH);
		Date date;
		String times = null;
		try {
			date = sdr.parse(time);
			long l = date.getTime();
			String stf = String.valueOf(l);
			times = stf.substring(0, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}

	/**
	 * 输入时间戳，转成 yyyy年MM月dd日 格式
	 * @param time
	 * @return
	 */
	public static String timeFormatConversion_2(String time) {
		String times = "";
		if (!TextUtils.isEmpty(time)){
			SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
			@SuppressWarnings("unused")
			long lcc = Long.parseLong(time);
			int i = Integer.parseInt(time);
			times = sdr.format(new Date(i * 1000L));
		}
		return times;
	}

	/**
	 * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14 16:09"）
	 *
	 * @param time
	 * @return
	 */
	public static String timeConversionDate(String time) {
		String times = "";
		if (!TextUtils.isEmpty(time)){
			SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
			@SuppressWarnings("unused")
			long lcc = Long.parseLong(time);
			int i = Integer.parseInt(time);
			times = sdr.format(new Date(i * 1000L));
		}
		return times;
	}

	/**
	 * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14 16:09:25"）
	 *
	 * @param time
	 * @return
	 */
	public static String timeConversionDate_two(String time) {
		String times = "";
		if (!TextUtils.isEmpty(time) && !time.equals("0")){
			SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			@SuppressWarnings("unused")
			long lcc = Long.parseLong(time);
			int i = Integer.parseInt(time);
			times = sdr.format(new Date(i * 1000L));
		}
		return times;
	}

	/**
	 * 调用此方法输入所要转换的时间戳输入例如（1402733340000）输出（"2014-06-14 16:09:25"）  毫秒级
	 *
	 * @param time
	 * @return
	 */
	public static String timeConversionDate_three(String time) {
		String times = "";
		if (!TextUtils.isEmpty(time)){
			SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			@SuppressWarnings("unused")
			long lcc = Long.parseLong(time);
			long i = Long.parseLong(time);
			times = sdr.format(new Date(i));
		}
		return times;
	}

	/**
	 * 掉此方法输入所要转换的时间输入例如（"2014年06月14日16时09分00秒"）返回时间戳
	 * 
	 * @param time
	 * @return
	 */
	public static String data(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
				Locale.CHINA);
		Date date;
		String times = null;
		try {
			date = sdr.parse(time);
			long l = date.getTime();
			String stf = String.valueOf(l);
			times = stf.substring(0, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}

	/**
	 * 掉此方法输入所要转换的时间输入例如（"2014-06-14 16:09:00"）返回时间戳
	 *
	 * @param time
	 * @return
	 */
	public static String data_2(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.ENGLISH);
		Date date;
		String times = null;
		try {
			date = sdr.parse(time);
			long l = date.getTime();
			String stf = String.valueOf(l);
			times = stf.substring(0, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}

	/**
	 * 掉此方法输入所要转换的时间输入例如（"2014-06-14 16:09:00"）返回时间戳，毫秒级
	 *
	 * @param time
	 * @return
	 */
	public static long data_5(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.ENGLISH);
		Date date;
		long times = 0;
		try {
			date = sdr.parse(time);
			times = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}

	/**
	 * 掉此方法输入所要转换的时间输入例如（"2014-06-14 16:09:00"）返回时间戳，秒级
	 *
	 * @param time
	 * @return
	 */
	public static long data_6(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.ENGLISH);
		Date date;
		long times = 0;
		try {
			date = sdr.parse(time);
			times = date.getTime() / 1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}

	/**
	 * 掉此方法输入所要转换的时间输入例如（"2014-06-14 16:09"）返回时间戳
	 *
	 * @param time
	 * @return
	 */
	public static String data_3(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm",
				Locale.ENGLISH);
		Date date;
		String times = null;
		try {
			date = sdr.parse(time);
			long l = date.getTime();
			String stf = String.valueOf(l);
			times = stf.substring(0, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}

	/**
	 * 掉此方法输入所要转换的时间输入例如（"2014-06-14"）返回时间戳
	 *
	 * @param time
	 * @return
	 */
	public static String data_4(String time) {
		String times = "";
		if (!TextUtils.isEmpty(time)){
			SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd",
					Locale.ENGLISH);
			Date date;
			try {
				date = sdr.parse(time);
				long l = date.getTime();
				String stf = String.valueOf(l);
				times = stf.substring(0, 10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return times;
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getTodayDateTimes() {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日",
				Locale.CHINA);
		return format.format(new Date());
	}
	
	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getCurrentTime_Today() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.ENGLISH);
		return sdf.format(new Date());
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getCurrentTime_Today_2() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
		return sdf.format(new Date());
	}

	/**
	 * 获取当前时间
	 *
	 * @return
	 */
	public static String getCurrentTime_Today_3() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		return sdf.format(new Date());
	}

	/**
	 * 获取当前时间
	 *
	 * @return
	 */
	public static String getCurrentTime_Today_4() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
		return sdf.format(new Date());
	}

	/**
	 * 调此方法输入所要转换的时间输入例如（"2014-06-14-16-09-00"）返回时间戳
	 * 
	 * @param time
	 * @return
	 */
	public static String dataOne(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
				Locale.CHINA);
		Date date;
		String times = null;
		try {
			date = sdr.parse(time);
			long l = date.getTime();
			String stf = String.valueOf(l);
			times = stf.substring(0, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}

	/**
	 * 调此方法输入所要转换的时间输入例如（"2014-06-14 16:09:00"）返回时间戳
	 *
	 * @param time
	 * @return
	 */
	public static String dataOne2(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		Date date;
		String times = null;
		try {
			date = sdr.parse(time);
			long l = date.getTime();
			String stf = String.valueOf(l);
			times = stf.substring(0, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}

	public static String getTimestamp(String time, String type) {
		SimpleDateFormat sdr = new SimpleDateFormat(type, Locale.CHINA);
		Date date;
		String times = null;
		try {
			date = sdr.parse(time);
			long l = date.getTime();
			String stf = String.valueOf(l);
			times = stf.substring(0, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}

	/**
	 * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
	 * 
	 * @param time
	 * @return
	 */
	public static String times(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒", Locale.CHINA);
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(i * 1000L));
		return times;
	}

	/**
	 * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分"）
	 *
	 * @param time
	 * @return
	 */
	public static String times_2(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分", Locale.CHINA);
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(i * 1000L));
		return times;
	}
	
	/**
	 * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14 16:09:00"）
	 * 
	 * @param time
	 * @return
	 */
	public static String timedate(String time) {
		String times = "";
		if (!TextUtils.isEmpty(time)) {
			SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			@SuppressWarnings("unused")
			long lcc = Long.valueOf(time);
			int i = Integer.parseInt(time);
			times = sdr.format(new Date(i * 1000L));
		}
		return times;

	}

	/**
	 * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14"）
	 *
	 * @param time
	 * @return
	 */
	public static String timedate_2(String time) {
		String times = "";
		if (!TextUtils.isEmpty(time) && !time.equals("0")) {
			SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			@SuppressWarnings("unused")
			long lcc = Long.valueOf(time);
			int i = Integer.parseInt(time);
			times = sdr.format(new Date(i * 1000L));
		}
		return times;

	}

	/**
	 * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"06-14"）
	 *
	 * @param time
	 * @return
	 */
	public static String timedate_3(String time) {
		String times = "";
		if (!TextUtils.isEmpty(time)){
			SimpleDateFormat sdr = new SimpleDateFormat("MM-dd", Locale.ENGLISH);
			@SuppressWarnings("unused")
			long lcc = Long.valueOf(time);
			int i = Integer.parseInt(time);
			times = sdr.format(new Date(i * 1000L));
		}
		return times;
	}

	/**
	 * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16:09"）
	 * 
	 * @param time
	 * @return
	 */
	public static String timet(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(i * 1000L));
		return times;

	}

	/**
	 * @param time 斜杠分开
	 * @return
	 */
	public static String timeslash(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd,HH:mm", Locale.ENGLISH);
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(i * 1000L));
		return times;

	}

	/**
	 * @param time 斜杠分开
	 * @return
	 */
	public static String timeslashData(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
//		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(lcc * 1000L));
		return times;

	}
	
	/**
	 * @param time 斜杠分开
	 * @return
	 */
	public static String timeMinute(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("HH:mm");
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(i * 1000L));
		return times;

	}

	public static String tim(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyyMMdd HH:mm");
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(i * 1000L));
		return times;
	}

	public static String time(String time) {
		String times = "";
		if (!TextUtils.isEmpty(time)){
			SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			@SuppressWarnings("unused")
			long lcc = Long.valueOf(time);
			int i = Integer.parseInt(time);
			times = sdr.format(new Date(i * 1000L));
		}
		return times;
	}

	// 调用此方法输入所要转换的时间戳例如（1402733340）输出（"2014年06月14日16时09分00秒"）
	public static String times(long timeStamp) {
		SimpleDateFormat sdr = new SimpleDateFormat("MM月dd日  #  HH:mm");
		return sdr.format(new Date(timeStamp)).replaceAll("#",
				getWeek(timeStamp));

	}

	private static String getWeek(long timeStamp) {
		int mydate = 0;
		String week = null;
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date(timeStamp));
		mydate = cd.get(Calendar.DAY_OF_WEEK);
		// 获取指定日期转换成星期几
		if (mydate == 1) {
			week = "周日";
		} else if (mydate == 2) {
			week = "周一";
		} else if (mydate == 3) {
			week = "周二";
		} else if (mydate == 4) {
			week = "周三";
		} else if (mydate == 5) {
			week = "周四";
		} else if (mydate == 6) {
			week = "周五";
		} else if (mydate == 7) {
			week = "周六";
		}
		return week;
	}

	// 并用分割符把时间分成时间数组
	/**
	 * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14-16-09-00"）
	 * 
	 * @param time
	 * @return
	 */
	public String timesOne(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(i * 1000L));
		return times;

	}

	public static String timesTwo(String time) {
		String times = "";
		if (!TextUtils.isEmpty(time)&&!"0000-00-00".equals(time)){
			SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
			@SuppressWarnings("unused")
			long lcc = Long.valueOf(time);
			int i = Integer.parseInt(time);
			times = sdr.format(new Date(i * 1000L));
		}
		return times;

	}
	
	public static String timesThree(String time) {
		String times = "";
		if (!TextUtils.isEmpty(time)){
			SimpleDateFormat sdr = new SimpleDateFormat("yyyy.MM.dd");
			@SuppressWarnings("unused")
			long lcc = Long.valueOf(time);
			int i = Integer.parseInt(time);
			times = sdr.format(new Date(i * 1000L));
		}
		return times;

	}

	/**
	 * 并用分割符把时间分成时间数组
	 * 
	 * @param time
	 * @return
	 */
	public static String[] timestamp(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		@SuppressWarnings("unused")
		long lcc = Long.valueOf(time);
		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(i * 1000L));
		String[] fenge = times.split("[年月日时分秒]");
		return fenge;
	}

	/**
	 * 根据传递的类型格式化时间
	 * 
	 * @param str
	 * @param type
	 *            例如：yy-MM-dd
	 * @return
	 */
	public static String getDateTimeByMillisecond(String str, String type) {

		Date date = new Date(Long.valueOf(str));

		SimpleDateFormat format = new SimpleDateFormat(type);

		String time = format.format(date);

		return time;
	}

	/**
	 * 分割符把时间分成时间数组
	 * 
	 * @param time
	 * @return
	 */
	public String[] division(String time) {

		String[] fenge = time.split("[年月日时分秒]");

		return fenge;

	}

	/**
	 * 输入时间戳变星期
	 * 
	 * @param time
	 * @return
	 */
	public static String changeweek(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		long lcc = Long.valueOf(time);
		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(i * 1000L));
		Date date = null;
		int mydate = 0;
		String week = null;
		try {
			date = sdr.parse(times);
			Calendar cd = Calendar.getInstance();
			cd.setTime(date);
			mydate = cd.get(Calendar.DAY_OF_WEEK);
			// 获取指定日期转换成星期几
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (mydate == 1) {
			week = "星期日";
		} else if (mydate == 2) {
			week = "星期一";
		} else if (mydate == 3) {
			week = "星期二";
		} else if (mydate == 4) {
			week = "星期三";
		} else if (mydate == 5) {
			week = "星期四";
		} else if (mydate == 6) {
			week = "星期五";
		} else if (mydate == 7) {
			week = "星期六";
		}
		return week;

	}

	/**
	 * 获取日期和星期　例如：２０１４－１１－１３　１１:００　星期一
	 * 
	 * @param time
	 * @param type
	 * @return
	 */
	public static String getDateAndWeek(String time, String type) {
		return getDateTimeByMillisecond(time + "000", type) + "  "
				+ changeweekOne(time);
	}

	/**
	 * 输入时间戳变星期
	 * 
	 * @param time
	 * @return
	 */
	public static String changeweekOne(String time) {
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
		long lcc = Long.valueOf(time);
		int i = Integer.parseInt(time);
		String times = sdr.format(new Date(i * 1000L));
		Date date = null;
		int mydate = 0;
		String week = null;
		try {
			date = sdr.parse(times);
			Calendar cd = Calendar.getInstance();
			cd.setTime(date);
			mydate = cd.get(Calendar.DAY_OF_WEEK);
			// 获取指定日期转换成星期几
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (mydate == 1) {
			week = "星期日";
		} else if (mydate == 2) {
			week = "星期一";
		} else if (mydate == 3) {
			week = "星期二";
		} else if (mydate == 4) {
			week = "星期三";
		} else if (mydate == 5) {
			week = "星期四";
		} else if (mydate == 6) {
			week = "星期五";
		} else if (mydate == 7) {
			week = "星期六";
		}
		return week;

	}

	/**
	 * 输入日期如（2014年06月14日16时09分00秒）返回（星期数）
	 * 
	 * @param time
	 * @return
	 */
	public String week(String time) {
		Date date = null;
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒", Locale.CHINA);
		int mydate = 0;
		String week = null;
		try {
			date = sdr.parse(time);
			Calendar cd = Calendar.getInstance();
			cd.setTime(date);
			mydate = cd.get(Calendar.DAY_OF_WEEK);
			// 获取指定日期转换成星期几
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (mydate == 1) {
			week = "星期日";
		} else if (mydate == 2) {
			week = "星期一";
		} else if (mydate == 3) {
			week = "星期二";
		} else if (mydate == 4) {
			week = "星期三";
		} else if (mydate == 5) {
			week = "星期四";
		} else if (mydate == 6) {
			week = "星期五";
		} else if (mydate == 7) {
			week = "星期六";
		}
		return week;
	}

	/**
	 * 输入日期如（2014-06-14-16-09-00）返回（星期数）
	 * 
	 * @param time
	 * @return
	 */
	public String weekOne(String time) {
		Date date = null;
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
		int mydate = 0;
		String week = null;
		try {
			date = sdr.parse(time);
			Calendar cd = Calendar.getInstance();
			cd.setTime(date);
			mydate = cd.get(Calendar.DAY_OF_WEEK);
			// 获取指定日期转换成星期几
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (mydate == 1) {
			week = "星期日";
		} else if (mydate == 2) {
			week = "星期一";
		} else if (mydate == 3) {
			week = "星期二";
		} else if (mydate == 4) {
			week = "星期三";
		} else if (mydate == 5) {
			week = "星期四";
		} else if (mydate == 6) {
			week = "星期五";
		} else if (mydate == 7) {
			week = "星期六";
		}
		return week;
	}

}
