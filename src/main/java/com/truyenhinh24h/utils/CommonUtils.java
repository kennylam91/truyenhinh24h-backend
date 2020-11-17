package com.truyenhinh24h.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CommonUtils {

	public static Long getEndOfDay(Date date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
		cal.set(date.getYear() + 1900, date.getMonth(), date.getDate(), 24, 0, 0);
		return cal.getTimeInMillis();
	}

	public static Long getStartOfDay(Date date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
		cal.set(date.getYear() + 1900, date.getMonth(), date.getDate(), 0, 0, 0);
		return cal.getTimeInMillis();
	}

	public static long getHourInGMT7(Date date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

}
