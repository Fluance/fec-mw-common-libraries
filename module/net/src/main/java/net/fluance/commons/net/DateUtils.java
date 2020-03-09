package net.fluance.commons.net;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	private static final int NEXT_DAY = 1;
	private DateUtils() {}
	
	public static boolean isToday (Date date) {
		Date today = new Date();
		date = setTimeToMidnight(date);
		today = setTimeToMidnight(today);
		return today.getTime() == date.getTime();
	}
	
	public static boolean isBeforeToday (Date date) {
		Date today = new Date();
		date = setTimeToMidnight(date);
		today = setTimeToMidnight(today);
		return today.getTime() > date.getTime();
	}
	
	public static boolean isAfterToday (Date date) {
		Date today = new Date();
		date = setTimeToMidnight(date);
		today = setTimeToMidnight(today);
		return today.getTime() < date.getTime();
	}
	
	public static boolean isTomorrow (Date date) {
		Date tomorrow = getNextDay(new Date());
		return date.getTime() == tomorrow.getTime();
	}
	
	/**
	 * Add or subtract days on the given date.
	 * @param date
	 * @param daysToAdd
	 * @return new calculated date
	 */
	public static Date changeDateByDays(Date date, int daysToAdd) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); 
		calendar.add(Calendar.DATE, daysToAdd);
		return calendar.getTime();
	}
	
	/**
	 * Get next day from date
	 * @param date
	 * @return next day date
	 */
	public static Date getNextDay(Date date) {
		return changeDateByDays(date, NEXT_DAY);
	}
	
	/**
	 * Convert the time to the midnight of the currently set date.
	 * @param date
	 * @return date with midnight time
	 */
	public static Date setTimeToMidnight(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); 
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
}
