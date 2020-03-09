package net.fluance.commons.lang;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {


	/**
	 * Method to convert a given date to a string.
	 * 
	 * @param currentDate
	 *            : the date you want to change to a string.
	 * @param format
	 *            : the format of the date.
	 * @return the date in string with the given format.
	 */
	public static String toStringDate(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
}