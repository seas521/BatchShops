package com.if2c.harald.tools;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.if2c.harald.exception.DateFormatException;
import com.if2c.harald.exception.DateIsNullException;

public class DateUtils {
	static FastDateFormat sdf = DateFormatUtils.ISO_DATE_FORMAT;
	public static final String[] pattern = new String[] { "yyyyMMdd",
			"yyyy-MM-dd", "yyyy/MM/dd", "MM月dd日", "yyyy年", "yyyy年MM月",
			"yyyy年MM月dd日", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss",
			"yyyy/MM/dd HH:mm:ss", "公元前y年", "公元前yy年", "公元前yyy年", "公元y年",
			"公元yy年", "公元yyy年", "yyyy年MM月dd日 HH:mm", "yyyy年MM月dd日HH:mm",
			"yyyy-MM-dd HH:mm", "yyyy年MM月dd日 HH:mm:ss","yyyy-MM-dd HH:mm:ss.sss" };
	public static final String datePattern = "yyyy-MM-dd";
	public static final String datePatternWithHMS = "yyyy-MM-dd HH:mm:ss";

	public static String parseDate(java.sql.Date date) {
		return sdf.format(date);
	}

	public static Date strToDate(String src, String format) {
		java.util.Date temp = null;
		DateFormat dateFormat = new SimpleDateFormat(format);
		try {
			temp = dateFormat.parse(src);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public static Date strToDate(String src) {
		return strToDate(src, "yyyy-MM-dd");
	}

	public static Date strToDatehhmmss(String src) {
		return strToDate(src, "yyyy-MM-dd HH:mm:ss");
	}

	public static String changeyyyymmddToDate(String src) {
		String str1 = src.substring(0, 4);
		String str2 = src.substring(4, 6);
		String str3 = src.substring(6, 8);
		String str = str1 + "-" + str2 + "-" + str3;
		return str;
	}

	public static String dateToStr(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	public static String dateToStr(Date date) {
		if (date != null) {
			return dateToStr(date, "yyyy-MM-dd");
		} else {
			return "";
		}
	}

	public DateUtils() {
	}

	/**
	 * 取系统当前时间
	 * 
	 * @author:Jimmy
	 * @param:none
	 * @return:java.util.Date
	 */
	public static Date getCurrentSystemDateTime() {
		return strToDate(logFormatTransform(getCurrentTimeLogFormat()),
				"yyyy-MM-dd HH:mm:ss");
	}

	public static int getYear(String strDate) {
		return Integer.parseInt(strDate.substring(0, 4));
	}

	public static int getMonth(String sDate) {
		return Integer.parseInt(sDate.substring(5, sDate.indexOf("-", 5)));
	}

	public static int getDay(String sDate) {
		sDate = sDate.substring(sDate.lastIndexOf("-") + 1);
		if (sDate.indexOf(" ") != -1)
			sDate = sDate.substring(0, sDate.indexOf(" "));
		return Integer.parseInt(sDate);
	}

	public static String getDateString(int year, int month, int day) {
		return year + "-" + (month >= 10 ? month + "" : "0" + month) + "-"
				+ (day >= 10 ? day + "" : "0" + day);
	}

	public static void setBusinessDate(String business_date) {
		BUSINESS_DATE_FOR_TEST = formatDateTimeString(business_date, 1);
	}

	public static String getBusinessDate() {
		return BUSINESS_DATE_FOR_TEST;
	}

	public static String getToday() {
		return getDateTimeString(System.currentTimeMillis(), 1);
	}

	public static String getCurrentTimeStamp() {
		return getDateTimeString(System.currentTimeMillis(), 3);
	}

	public static String getCurrentDateTime() {
		return getDateTimeString(System.currentTimeMillis(), 2);
	}

	public static boolean isDateTimeString(String sDate) {
		String separator = "-";
		StringTokenizer token = new StringTokenizer(sDate, separator);
		String year = token.nextToken();
		String month = token.nextToken();
		String day = token.nextToken();
		if (day.indexOf(" ") != -1) {
			day = day.substring(0, day.indexOf(" "));
			boolean is_date = isDate(year, month, day);
			String time = day.substring(day.indexOf(" ") + 1);
			return is_date && isTime(time);
		} else {
			return isDate(year, month, day);
		}
	}

	private static boolean isDate(String year, String month, String day) {
		return isDate(Integer.parseInt(year), Integer.parseInt(month),
				Integer.parseInt(day));
	}

	private static boolean isDate(int year, int month, int day) {
		int day_of_months[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		try {
			boolean bLeapYear = false;
			if ((year + "").length() != 4)
				return false;
			if (isLeapYear(year))
				day_of_months[1] = 29;
			if (month < 1 || month > 12)
				return false;
			return day >= 1 && day <= day_of_months[month - 1];
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean isTime(String time) {
		if (time == null || time.equals(""))
			time = "00:00:00.000";
		String s[] = time.split(":");
		String s1[] = s[2].split("\\.");
		boolean ret = Integer.parseInt(s[0]) < 24
				&& Integer.parseInt(s[1]) < 60;
		if (s1.length == 2)
			ret = ret && Integer.parseInt(s1[0]) < 60
					&& Integer.parseInt(s1[1]) < 1000;
		else if (s1.length == 0)
			ret = ret && Integer.parseInt(s[2]) < 60;
		else
			ret = false;
		return ret;
	}

	private static boolean isLeapYear(int year) {
		if (year % 4 != 0)
			return false;
		return year % 100 != 0 || year % 400 == 0;
	}

	public static long getMillisecond(String date_time_str) {
		if (date_time_str.indexOf(" ") == -1) {
			return getMillisecond(date_time_str, null);
		} else {
			String s[] = date_time_str.split(" ");
			return getMillisecond(s[0], s[1]);
		}
	}

	private static long getMillisecond(String date, String time) {
		if (time == null || time.equals(""))
			time = "00:00:00.000";
		String s[] = date.split("-");
		String s2[] = time.split(":");
		String s3[] = s2[2].split("\\.");
		Calendar cl = Calendar.getInstance();
		cl.set(1, Integer.parseInt(s[0]));
		cl.set(2, Integer.parseInt(s[1]) - 1);
		cl.set(5, Integer.parseInt(s[2]));
		cl.set(11, Integer.parseInt(s2[0]));
		cl.set(12, Integer.parseInt(s2[1]));
		if (s3.length > 1) {
			cl.set(13, Integer.parseInt(s3[0]));
			cl.set(14, Integer.parseInt(s3[1]));
		} else {
			cl.set(13, Integer.parseInt(s2[2]));
			cl.set(14, 0);
		}
		return cl.getTimeInMillis();
	}

	public static String getDateString(long mill) {
		return getDateTimeString(mill, 1);
	}

	public static String getDateTimeString(long mill) {
		return getDateTimeString(mill, 2);
	}

	public static String getYYMMDD(String date_time_str) {
		return formatDateTimeString(date_time_str, 4);
	}

	public static String getYYYYMMDD(String date_time_str) {
		return formatDateTimeString(date_time_str, 5);
	}

	public static String formatDateTimeString(String date_time_str, int format) {
		return getDateTimeString(getMillisecond(date_time_str), format);
	}

	public static String getDateTimeString(long mill, int format) {
		if (mill < 0L)
			return "";
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(mill);
		int year = cl.get(1);
		int month = cl.get(2) + 1;
		int day = cl.get(5);
		int hour = cl.get(11);
		int mm = cl.get(12);
		int ss = cl.get(13);
		int ms = cl.get(14);
		String ret = "";
		switch (format) {
		default:
			break;

		case 0: // '\0'
			ret = year + "-" + (month >= 10 ? "" + month : "0" + month);
			break;

		case 1: // '\001'
			ret = year + "-" + (month >= 10 ? "" + month : "0" + month) + "-"
					+ (day >= 10 ? "" + day : "0" + day);
			break;

		case 2: // '\002'
			ret = year + "-" + (month >= 10 ? "" + month : "0" + month) + "-"
					+ (day >= 10 ? "" + day : "0" + day) + " "
					+ (hour >= 10 ? "" + hour : "0" + hour) + ":"
					+ (mm >= 10 ? "" + mm : "0" + mm) + ":"
					+ (ss >= 10 ? "" + ss : "0" + ss);
			break;

		case 3: // '\003'
			String sMs;
			if (ms < 10)
				sMs = "00" + ms;
			else if (ms < 100)
				sMs = "0" + ms;
			else
				sMs = "" + ms;
			sMs = "." + sMs;
			ret = year + "-" + (month >= 10 ? "" + month : "0" + month) + "-"
					+ (day >= 10 ? "" + day : "0" + day) + " "
					+ (hour >= 10 ? "" + hour : "0" + hour) + ":"
					+ (mm >= 10 ? "" + mm : "0" + mm) + ":"
					+ (ss >= 10 ? "" + ss : "0" + ss) + sMs;
			break;

		case 4: // '\004'
			ret = (year + "").substring(2)
					+ (month >= 10 ? "" + month : "0" + month)
					+ (day >= 10 ? "" + day : "0" + day);
			break;

		case 5: // '\005'
			ret = year + "" + (month >= 10 ? "" + month : "0" + month)
					+ (day >= 10 ? "" + day : "0" + day);
			break;

		case 6: // '\006'
			ret = year + "" + "\u5E74"
					+ (month >= 10 ? "" + month : "0" + month) + "\u6708"
					+ (day >= 10 ? "" + day : "0" + day) + "\u65E5";
			break;
		}
		return ret;
	}

	public static int substract(String biger_date_time, String smaller_date_time) {
		return (int) ((getMillisecond(formatDateTimeString(biger_date_time, 1)) - getMillisecond(formatDateTimeString(
				smaller_date_time, 1))) / 0x5265c00L);
	}

	public static int substract3(String biger_date_time,
			String smaller_date_time) {
		return (int) ((getMillisecond(formatDateTimeString(biger_date_time, 1)) - getMillisecond(formatDateTimeString(
				smaller_date_time, 1))) / 0x5265c00L) + 1;
	}

	public static int[] substract1(String biger_date_time,
			String smaller_date_time) {
		int year_biger = getYear(biger_date_time);
		int month_biger = getMonth(biger_date_time);
		int day_biger = getDay(biger_date_time);
		int year_smaller = getYear(smaller_date_time);
		int month_smaller = getMonth(smaller_date_time);
		int day_smaller = getDay(smaller_date_time);
		int retMonth = ((year_biger - year_smaller) * 12 + month_biger)
				- month_smaller;
		int retDay = 0;
		if (day_biger < day_smaller - 1) {
			retMonth--;
			retDay = substract(biger_date_time, year_biger + "-"
					+ (month_biger - 1) + "-" + day_smaller);
		} else {
			retDay = day_biger - day_smaller;
		}
		return (new int[] { retMonth, retDay });
	}

	public static BigDecimal substract2(String biger_date_time,
			String smaller_date_time) {
		int termArr[] = substract1(biger_date_time, smaller_date_time);
		BigDecimal mth = (new BigDecimal(termArr[0])).add((new BigDecimal(
				termArr[1])).divide(new BigDecimal(30D), 1, 4));
		return mth;
	}

	public static String add(String date_time_str, int days, int format) {
		if (format == 1)
			return add(date_time_str, days);
		else
			return getDateTimeString(getMillisecond(date_time_str)
					+ (long) (days * 0x5265c00), format);
	}

	public static String add(String strDate, int days) {
		DateFormat df = getDateFormat();
		Date d = getFormatedDate(df, strDate);
		Calendar c = new GregorianCalendar();
		c.setTime(d);
		c.set(5, c.get(5) + days);
		return df.format(c.getTime());
	}

	public static String add(String strDate, int days, String format) {
		DateFormat df = getDateFormat(format);
		Date d = getFormatedDate(df, strDate);
		Calendar c = new GregorianCalendar();
		c.setTime(d);
		c.set(5, c.get(5) + days);
		return df.format(c.getTime());
	}

	public static int subtract(String strEndDate, String strStartDate) {
		return subtract(strEndDate, strStartDate, "yyyy-MM-dd");
	}

	public static int subtract(String strEndDate, String strStartDate,
			String format) {
		DateFormat df = getDateFormat(format);
		Date endDate = getFormatedDate(df, strEndDate);
		Date startDate = getFormatedDate(df, strStartDate);
		long msEndDate = endDate.getTime();
		long msStartDate = startDate.getTime();
		return (int) ((msEndDate - msStartDate) / 0x5265c00L);
	}

	public static String addMonth(String strDate, int months) {
		DateFormat df = getDateFormat();
		Date d = getFormatedDate(df, strDate);
		Calendar c = new GregorianCalendar();
		c.setTime(d);
		c.set(2, c.get(2) + months);
		return df.format(c.getTime());
	}

	public static String addMonth(String strDate, int months, String format) {
		DateFormat df = getDateFormat(format);
		Date d = getFormatedDate(df, strDate);
		Calendar c = new GregorianCalendar();
		c.setTime(d);
		c.set(2, c.get(2) + months);
		return df.format(c.getTime());
	}

	public static boolean isFormatedDate(String strDate) {
		try {
			getFormatedDate(strDate);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	private static DateFormat getDateFormat() {
		return getDateFormat("yyyy-MM-dd");
	}

	private static DateFormat getDateFormat(String format) {
		return new SimpleDateFormat(format);
	}

	private static Date getFormatedDate(DateFormat df, String strDate) {
		try {
			return df.parse(strDate);
		} catch (Exception ex) {
			throw new RuntimeException(
					"\u65E5\u671F\u683C\u5F0F\u4E0D\u5BF9\uFF0C\u65E0\u6CD5\u89E3\u6790\u3002",
					ex);
		}
	}

	public static Date getFormatedDate(String strDate) {
		return getFormatedDate(getDateFormat(), strDate);
	}

	public static Date yearAfter(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, day);
		return calendar.getTime();
	}

	public static Date dayAfter(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, day);
		return calendar.getTime();
	}

	public static Date monthAfter(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}

	public static Date monthBefore(Date date, int month) {
		return monthAfter(date, 0 - month);
	}

	public static Date yearBefore(Date date, int month) {
		return yearAfter(date, 0 - month);
	}

	public static Date dayBefore(Date date, int day) {
		return dayAfter(date, 0 - day);
	}

	public static int getDayOfMonth(int year, int month) {
		int day_of_months[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		try {
			if (isLeapYear(year))
				day_of_months[1] = 29;
			return day_of_months[month - 1];
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean isInDatePeriod(String someDate, String begin_date,
			String end_date) {
		return substract(someDate, begin_date) >= 0
				&& substract(someDate, end_date) <= 0;
	}

	public static String addZero(String srcDate) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(df.parse(srcDate));
	}

	/*
	 * 取得两个时间相差多少分钟.
	 */
	public static long getSubstractMinutes(String strEndDate,
			String strStartDate) {
		long msEndDate = getMillisecond(strEndDate);
		long msStartDate = getMillisecond(strStartDate);
		long temp = msEndDate - msStartDate;
		long result = (long) temp / (60 * 1000);
		return result;
	}

	public static void main(String args[]) throws DateFormatException {
		System.out.println(DateUtils.strToDate("2013-09-26").after(
				DateUtils.strToDate(DateUtils.getCurrentDateTime(),
						"yyyy-MM-dd")));
		System.out.println(DateUtils.convertString2Date("2013-11-25 10:05:15.0"));
	}

	public static String getLastMonthDate() {
		String result = "";
		Calendar cl = Calendar.getInstance();
		int year = cl.get(1);
		int month = cl.get(2) + 1;
		if (--month == 0) {
			year--;
			month = 12;
		}
		int days = getDayOfMonth(year, month);
		if (month < 10)
			result = year + "-0" + month + "-" + days;
		else
			result = year + "-" + month + "-" + days;
		return result;
	}

	public static String logFormatTransform(String time) {
		String newStr = "";
		if (time != null && !time.trim().equals("")) {
			newStr = time.substring(0, 4) + "-" + time.substring(4, 6) + "-"
					+ time.substring(6, 8);
			if (time.length() > 8)
				newStr = newStr + " " + time.substring(8, 10) + ":"
						+ time.substring(10, 12) + ":" + time.substring(12, 14);
		}
		return newStr;
	}

	public static String getCurrentTimeLogFormat() {
		return getCurrentDateTime().replaceAll("-", "").replaceAll(":", "")
				.replaceAll(" ", "");
	}

	public static String[] getBalanceCycleMon(String bal_year_start,
			String bal_year_end) {
		int nyear = Integer.parseInt(bal_year_end)
				- Integer.parseInt(bal_year_start);
		String bal_cycle[] = new String[nyear * 12];
		int seq = 0;
		for (int m = 0; m < nyear; m++) {
			for (int n = 1; n <= 12; n++) {
				int next_year = Integer.parseInt(bal_year_start) + m;
				String next_mon = n <= 9 ? "0" + n : "" + n;
				bal_cycle[seq++] = next_year + next_mon + "01" + next_year
						+ next_mon + getDayOfMonth(next_year, n) + next_year
						+ "\u5E74" + n + "\u6708";
			}

		}

		return bal_cycle;
	}

	public static String[] getBalanceCycleQua(String bal_year_start,
			String bal_year_end) {
		int nyear = Integer.parseInt(bal_year_end)
				- Integer.parseInt(bal_year_start);
		String bal_cycle[] = new String[nyear * 4];
		int seq = 0;
		for (int m = 0; m < nyear; m++) {
			int n = 1;
			for (int j = 1; n <= 12; j++) {
				int next_year = Integer.parseInt(bal_year_start) + m;
				String next_mon = n <= 9 ? "0" + n : "" + n;
				String next_mon_2 = (n += 2) <= 9 ? "0" + n : "" + n;
				bal_cycle[seq++] = next_year + next_mon + "01" + next_year
						+ next_mon_2 + getDayOfMonth(next_year, n) + next_year
						+ "\u5E74" + j + "\u5B63\u5EA6";
				n++;
			}

		}

		return bal_cycle;
	}

	/**
	 * 将String按照通用的格式转成Date
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Date convertString2Date(String dateStr)
			throws DateFormatException {
		try {
			return org.apache.commons.lang3.time.DateUtils.parseDate(dateStr, pattern);
		} catch (ParseException e) {
			throw new DateFormatException("There's a exception when "+dateStr+" convert to Date");
		}
	}

	/**
	 * 不带时分秒
	 * 
	 * @param date
	 * @return
	 * @throws DateIsNullException
	 */
	public static String convertDate2String(Date date)
			throws DateIsNullException {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
			return dateFormat.format(date);
		} catch (Exception e) {
			throw new DateIsNullException("The date is null");
		}
	}

	/**
	 * 带时分秒
	 * 
	 * @param date
	 * @return
	 * @throws DateIsNullException
	 */
	public static String convertDate2StringWithHMS(Date date)
			throws DateIsNullException {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					datePatternWithHMS);
			return dateFormat.format(date);
		} catch (Exception e) {
			throw new DateIsNullException("The date is null");
		}
	}

	/**
	 * 
	 * @param dateSource
	 *            源时间
	 * @param dateTarget
	 *            目标时间
	 * @return -1(源时间<目标时间) 0(源时间=目标时间) 1(源时间>目标时间)
	 * @throws DateIsNullException
	 * @throws ParseException
	 */
	public static int compareDiffDateWithHMS(Date dateSource, Date dateTarget)
			throws ParseException, DateIsNullException {
		SimpleDateFormat sdf = new SimpleDateFormat(datePatternWithHMS);
		Date source = sdf.parse(convertDate2StringWithHMS(dateSource));
		Date target = sdf.parse(convertDate2StringWithHMS(dateTarget));
		return source.compareTo(target);
	}

	private static String BUSINESS_DATE_FOR_TEST = null;
	public static final int FORMAT_SHORTDATE = 0;
	public static final int FORMAT_DATE = 1;
	public static final int FORMAT_DATETIME = 2;
	public static final int FORMAT_DATETIMEMILLISECOND = 3;
	public static final int FORMAT_YYMMDD = 4;
	private static final int FORMAT_YYYYMMDD = 5;
	public static final int FORMAT_DATESTRING = 6;
	public static final int FORMAT_DATE_CBBS = 7;
	public static final int ONE_DAY_MILLISECOND = 0x5265c00;
	public static String GeneratorBeanId = "bizTime";
	public static String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	public static String FULL24_DATE_FORMAT = "yyyy-MM-dd hh24:mm:ss";

}
