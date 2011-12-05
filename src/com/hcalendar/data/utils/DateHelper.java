package com.hcalendar.data.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.hcalendar.data.utils.exception.DateException;

@SuppressWarnings("deprecation")
public class DateHelper {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");

	public final static int daysOnMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31,
			30, 31, 30, 31 };

	public static final String[] months = { "Enero", "Febrero", "Marzo",
			"Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
			"Octubre", "Noviembre", "Diciembre" };

	public static final String[] daysOfWeek = { "Lunes", "Martes", "Miércoles",
			"Jueves", "Viernes", "Sábado", "Domingo" };

	public static Date xmlGregorianCalendar2Date(
			XMLGregorianCalendar xmlGregorianCalendar) {
		return new Date(xmlGregorianCalendar.getYear() - 1900,
				xmlGregorianCalendar.getMonth() - 1,
				xmlGregorianCalendar.getDay());
	}

	public static XMLGregorianCalendar date2XMLGregorianCalendar(Date date)
			throws DateException {
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(
					new GregorianCalendar(date.getYear() + 1900, date
							.getMonth(), date.getDate()));
		} catch (DatatypeConfigurationException e) {
			throw new DateException(e);
		}
	}

	public static Date parse2Date(String strDate) throws DateException {
		try {
			return DATE_FORMAT.parse(strDate);
		} catch (ParseException e) {
			throw new DateException(e);
		}
	}

	public static List<Date> parse2Date(List<String> dLibresList)
			throws DateException {
		List<Date> result = new ArrayList<Date>();
		for (String dateStr : dLibresList) {
			result.add(parse2Date(dateStr));
		}
		return result;
	}

	public static XMLGregorianCalendar parse2XMLGregorianCalendar(String strDate)
			throws DateException {
		try {
			Date date = parse2Date(strDate);
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(
					new GregorianCalendar(date.getYear() + 1900, date
							.getMonth(), date.getDate()));
		} catch (DatatypeConfigurationException e) {
			throw new DateException(e);
		}
	}

	public static boolean containsDate(List<Date> dLibresDateList, Date today) {
		for (Date date : dLibresDateList) {
			if (!(date.getYear() == today.getYear()))
				continue;
			if (!(date.getMonth() == today.getMonth()))
				continue;
			if (date.getDate() == today.getDate())
				return true;
		}
		return false;
	}

	/**
	 * Compare dates
	 * 
	 * @param date
	 *            date of type Date
	 * @param date2
	 *            date of type xmlgregorian calendar
	 * @return 1 - date>date2 0 - date=date2 -1 - date<date2
	 * 
	 * */
	public static int compareDates(Date date, XMLGregorianCalendar date2) {
		int year1 = date.getYear() + 1900;
		int month1 = date.getMonth() + 1;
		int day1 = date.getDate();
		int year2 = date2.getYear();
		int month2 = date2.getMonth();
		int day2 = date2.getDay();

		if (year1 > year2)
			return 1;
		else if (year1 < year2)
			return -1;
		else if (month1 > month2)
			return 1;
		else if (month1 < month2)
			return -1;
		else if (day1 > day2)
			return 1;
		else if (day1 < day2)
			return -1;
		else
			return 0;
	}

	/**
	 * isLeap() returns true if the given year is a Leap Year.
	 * 
	 * "a year is a leap year if it is divisible by 4 but not by 100, except
	 * that years divisible by 400 *are* leap years." -- Kernighan & Ritchie,
	 * _The C Programming Language_, p 37.
	 */
	public static boolean isLeap(int year) {
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
			return true;
		return false;
	}

	/**
	 * Translate days of the week to spanish
	 * 
	 * @param day
	 *            of the week
	 * @return translated day of the week
	 * */
	public static String translateDayOfWeek(int day) {
		day = day + 1;
		if (day == Calendar.MONDAY)
			return daysOfWeek[0];
		if (day == Calendar.TUESDAY)
			return daysOfWeek[1];
		if (day == Calendar.WEDNESDAY)
			return daysOfWeek[2];
		if (day == Calendar.THURSDAY)
			return daysOfWeek[3];
		if (day == Calendar.FRIDAY)
			return daysOfWeek[4];
		if (day == Calendar.SATURDAY)
			return daysOfWeek[5];
		if (day == Calendar.SUNDAY)
			return daysOfWeek[6];
		return null;
	}

	public static boolean isBetween(Date date, Date fromDate, Date toDate)
			throws DateException {
		if (date.compareTo(fromDate) < 0)
			return false;
		if (date.compareTo(toDate) > 0)
			return false;
		return true;
	}
}
