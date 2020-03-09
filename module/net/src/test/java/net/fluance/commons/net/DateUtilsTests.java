package net.fluance.commons.net;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class DateUtilsTests {
	
	@Test
	public void getNextDay() throws ParseException {
		Date date = createDateByStringAndFormat("2019-03-21", "yyyy-MM-dd");
		Date expectedDate = createDateByStringAndFormat("2019-03-22", "yyyy-MM-dd");
		Date nextDayDate = DateUtils.getNextDay(date);
		Assert.assertTrue("Is one day added?", nextDayDate.compareTo(expectedDate) == 0);
	}
	
	@Test
	public void addDaysToDate_six_day() throws ParseException {
		Date date = createDateByStringAndFormat("2019-03-21", "yyyy-MM-dd");
		Date expectedDate = createDateByStringAndFormat("2019-03-27", "yyyy-MM-dd");
		Date newDate = DateUtils.changeDateByDays(date, 6);
		Assert.assertTrue("Are six days added?", newDate.compareTo(expectedDate) == 0);
	}
	
	@Test
	public void addDaysToDate_one_day_date_null() throws ParseException {
		Date date = null;
		Date newDate = DateUtils.changeDateByDays(date, 1);
		Assert.assertTrue("Is null?", newDate == null);
	}
	
	@Test
	public void addDaysToDate_minus_one_day() throws ParseException {
		Date date = createDateByStringAndFormat("2019-03-21", "yyyy-MM-dd");
		Date expectedDate = createDateByStringAndFormat("2019-03-20", "yyyy-MM-dd");
		Date newDate = DateUtils.changeDateByDays(date, -1);
		Assert.assertTrue("Is one day substract?", newDate.compareTo(expectedDate) == 0);
	}
	
	@Test
	public void setTimeToMidnight_minus_one_day() throws ParseException {
		Date date = createDateByStringAndFormat("2019-03-21 04:20:42", "yyyy-MM-dd HH:mm:ss");
		Date expectedDate = createDateByStringAndFormat("2019-03-21 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Date newDate = DateUtils.setTimeToMidnight(date);
		Assert.assertTrue("Is one day substract?", newDate.compareTo(expectedDate) == 0);
	}
	
	private Date createDateByStringAndFormat(String date, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(date);
	}
}
