package com.truyenhinh24h.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CommonUtils {

	private static final String GMT_PLUS_7 = "GMT+7:00";
	private static final TimeZone TIME_ZONE_PLUS_7 = TimeZone.getTimeZone(GMT_PLUS_7);

	/**
	 * 
	 * @param localDate
	 * @return the end moment of day in millisecond
	 */
	public static Long getEndOfDay(LocalDate localDate) {
		LocalDateTime endOfDay = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(),
				localDate.getDayOfMonth(), 23, 59, 59);
		return endOfDay.toEpochSecond(ZoneOffset.of("+7")) * 1000;
	}

	public static Long getStartOfDay(LocalDate localDate) {
		LocalDateTime endOfDay = LocalDateTime.of(localDate.getYear(), localDate.getMonthValue(),
				localDate.getDayOfMonth(), 0, 0, 1);
		return endOfDay.toEpochSecond(ZoneOffset.of("+7")) * 1000;
	}

	public static long getHourInGMT7(Date date) {
		Calendar cal = Calendar.getInstance(TIME_ZONE_PLUS_7);
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

//	public static void main(String[] args) {
//		LocalDate localDate = Instant.ofEpochMilli(1605808800000l).atZone(ZoneId.of("GMT+07:00")).toLocalDate();
//		Long start = getStartOfDay(localDate);
//		Long end = getEndOfDay(localDate);
//		System.out.println(start);
//		System.out.println(end);
//
//	}

}
