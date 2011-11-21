package com.hcalendar.data.orm.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hcalendar.data.exception.BusinessException;
import com.hcalendar.data.orm.exception.ORMException;
import com.hcalendar.data.utils.DateHelper;
import com.hcalendar.data.utils.DateIterator;
import com.hcalendar.data.utils.exception.DateException;
import com.hcalendar.data.xml.userconfiguration.ObjectFactory;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User.YearConf;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User.YearConf.FreeDays;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User.YearConf.FreeDays.FreeDay;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User.YearConf.WorkingDays;
import com.hcalendar.data.xml.workedhours.AnualHours;
import com.hcalendar.data.xml.workedhours.AnualHours.UserInput;
import com.hcalendar.data.xml.workedhours.AnualHours.UserInput.Holidays;
import com.hcalendar.data.xml.workedhours.AnualHours.UserInput.WorkedHours;

public class ORMHelper {

	private static List<Date> freeDay2DateList(List<FreeDay> freedays) {
		List<Date> result = new ArrayList<Date>();
		for (FreeDay day : freedays) {
			result.add(DateHelper.xmlGregorianCalendar2Date(day.getDay()));
		}
		return result;
	}

	private static FreeDays getCalendarFreeDays(UserConfiguration userConfig, String profileName, int year) {
		UserConfiguration xmlObject = userConfig;
		for (User user : xmlObject.getUser()) {
			if (!user.getName().equals(profileName))
				continue;
			for (YearConf yearconf : user.getYearConf()) {
				if (year == yearconf.getYear())
					return yearconf.getFreeDays();
			}
		}
		return null;
	}

	private static void removeFromWorkedDays(AnualHours anualHours, Date date, String profileName) {
		List<WorkedHours> workedDays = getUsersWorkedHourList(anualHours, profileName);
		List<WorkedHours> copy = new ArrayList<WorkedHours>(workedDays);
		for (WorkedHours day : copy) {
			if (DateHelper.compareDates(date, day.getDate()) == 0)
				workedDays.remove(day);
		}

	}

	private static void removeFromHolidays(AnualHours anualHours, Date date, String profileName) {
		List<Holidays> holidays = getUserHolidaysList(anualHours, profileName);
		List<Holidays> copy = new ArrayList<Holidays>(holidays);
		for (Holidays day : copy) {
			if (DateHelper.compareDates(date, day.getDate()) == 0)
				holidays.remove(day);
		}
	}

	@SuppressWarnings("deprecation")
	private static void removeFromFreeDays(UserConfiguration userConfig, Date date, String profileName) {
		FreeDays freeDays = getCalendarFreeDays(userConfig, profileName, date.getYear() + 1900);
		List<FreeDay> copy = new ArrayList<FreeDay>(freeDays.getFreeDay());
		for (FreeDay day : copy) {
			if (DateHelper.compareDates(date, day.getDay()) == 0)
				freeDays.getFreeDay().remove(day);
		}
	}

	private static FreeDay createFreeDay() {
		ObjectFactory fac = new ObjectFactory();
		return fac.createUserConfigurationUserYearConfFreeDaysFreeDay();
	}

	private static Holidays createHoliday() {
		com.hcalendar.data.xml.workedhours.ObjectFactory fac = new com.hcalendar.data.xml.workedhours.ObjectFactory();
		return fac.createAnualHoursUserInputHolidays();
	}

	private static WorkedHours createWorkedHours() {
		com.hcalendar.data.xml.workedhours.ObjectFactory fac = new com.hcalendar.data.xml.workedhours.ObjectFactory();
		return fac.createAnualHoursUserInputWorkedHours();
	}

	public static List<WorkedHours> getUsersWorkedHourList(AnualHours hours, String profileName) {
		for (UserInput user : hours.getUserInput()) {
			if (user.getUserName().equals(profileName))
				return user.getWorkedHours();
		}
		return null;
	}

	public static List<Date> getUserHolidays(AnualHours anualHours, String profileName) {
		List<Date> result = new ArrayList<Date>();
		List<Holidays> holidays = new ArrayList<Holidays>();
		for (UserInput user : anualHours.getUserInput()) {
			if (user.getUserName().equals(profileName))
				holidays = user.getHolidays();
		}
		for (Holidays ho : holidays) {
			result.add(DateHelper.xmlGregorianCalendar2Date(ho.getDate()));
		}
		return result;
	}

	public static List<Holidays> getUserHolidaysList(AnualHours anualHours, String profileName) {
		List<Holidays> holidays = new ArrayList<Holidays>();
		for (UserInput user : anualHours.getUserInput()) {
			if (user.getUserName().equals(profileName))
				return user.getHolidays();
		}
		return holidays;
	}

	public static List<String> getCurrentProfiles(UserConfiguration userConfig) {
		UserConfiguration user = userConfig;
		List<String> result = new ArrayList<String>();
		for (User us : user.getUser()) {
			result.add(us.getName());
		}
		return result;
	}

	public static Float getAnualHours(UserConfiguration userConfig, String username, int year) {
		UserConfiguration userConf = userConfig;
		for (com.hcalendar.data.xml.userconfiguration.UserConfiguration.User user : userConf.getUser()) {
			if (user.getName().equals(username)) {
				for (YearConf conf : user.getYearConf()) {
					if (conf.getYear() == year)
						return conf.getCalendarHours();
				}
			}
		}
		return null;
	}

	public static float getPlannedHours(AnualHours anualHours, int year, String profileName) {
		float sumOfHours = 0;
		AnualHours hours = anualHours;
		for (WorkedHours wHours : getUsersWorkedHourList(hours, profileName)) {
			if (wHours.getDate().getYear() == year)
				sumOfHours = sumOfHours + wHours.getHours();
		}
		return sumOfHours;
	}

	public static Map<Float, String> getInputHours(AnualHours anualHours, Date date, String profileName) {
		Map<Float, String> result = new HashMap<Float, String>();
		AnualHours aHours = anualHours;
		// Worked hours
		for (WorkedHours wHours : getUsersWorkedHourList(aHours, profileName)) {
			if (DateHelper.compareDates(date, wHours.getDate()) == 0)
				result.put(wHours.getHours(), wHours.getDescription());
		}
		// Holidays
		for (Holidays hol : getUserHolidaysList(anualHours, profileName)) {
			if (DateHelper.compareDates(date, hol.getDate()) == 0)
				result.put((float) 0, hol.getComment() == null ? "" : hol.getComment());
		}
		return result;
	}

	public static List<Date> getCalendarFreeDays(UserConfiguration userConfig, String username, Integer year)
			throws BusinessException {
		List<User> usersList = userConfig.getUser();
		for (User user : usersList) {
			if (user.getName().equals(username)) {
				for (YearConf yearCgf : user.getYearConf()) {
					if (yearCgf.getYear() == year.intValue()) {
						return freeDay2DateList(yearCgf.getFreeDays().getFreeDay());
					}
				}
			}
		}
		throw new BusinessException("El usuario no existe");
	}

	@SuppressWarnings("deprecation")
	public static List<Date> getCalendarNotWorkingDays(UserConfiguration userConfig, String username,
			int selectedYear) {
		UserConfiguration userconf = userConfig;
		List<Integer> dayList = new ArrayList<Integer>();
		List<Integer> notWorkingDays = new ArrayList<Integer>();
		List<Date> result = new ArrayList<Date>();

		for (User user : userconf.getUser()) {
			if (user.getName().equals(username)) {
				for (YearConf year : user.getYearConf()) {
					if (year.getYear() != selectedYear)
						return null;
					// Calcular los findes a partir de los laborales
					for (WorkingDays wd : year.getWorkingDays()) {
						dayList.add(Integer.valueOf(wd.getWorkingDay()));
					}
					for (int i = 1; i <= Calendar.SATURDAY; i++) {
						if (!dayList.contains(i))
							notWorkingDays.add(i);
					}
				}
			}
		}

		// Ya tenemos los dias que no son laborables a la semana. Ahora recorrer
		// el año y sacar las fechas.
		// Sacamos todas sin más calculo, puede pasar que se haya trabajado en
		// un dia que sea festivo. Al pintar lo haremos en el orden correcto y
		// punto
		DateIterator dateIt = new DateIterator(new Date(selectedYear - 1900, 0, 1), new Date(
				selectedYear - 1900, 11, 31));
		Date today;
		while (dateIt.hasNext()) {
			today = dateIt.next();
			if (notWorkingDays.contains(today.getDay() + 1))
				result.add(today);
		}
		return result;
	}

	// Métodos add y remove
	public static void addFreeDays(UserConfiguration userConfig, AnualHours anualHours, Date date,
			String comment, String profileName) throws ORMException {
		boolean repeated = false;
		try {
			@SuppressWarnings("deprecation")
			FreeDays freeDays = getCalendarFreeDays(userConfig, profileName, date.getYear() + 1900);
			List<FreeDay> freeDaysList = freeDays.getFreeDay();
			for (FreeDay day : freeDaysList) {
				if (DateHelper.compareDates(date, day.getDay()) == 0)
					repeated = true;
			}
			if (!repeated) {
				FreeDay freeDay = createFreeDay();
				freeDay.setDay(DateHelper.date2XMLGregorianCalendar(date));
				freeDay.setComment(comment);
				freeDaysList.add(freeDay);
			}
			removeFromHolidays(anualHours, date, profileName);
			removeFromWorkedDays(anualHours, date, profileName);
		} catch (DateException e) {
			throw new ORMException(e);
		}
	}

	public static void addHolidays(AnualHours anualHours, UserConfiguration userConfig, Date date,
			String comment, String profileName) throws ORMException {
		boolean repeated = false;
		try {
			AnualHours xmlObject = anualHours;
			List<Holidays> holidays = getUserHolidaysList(xmlObject, profileName);
			for (Holidays holiday : holidays) {
				if (DateHelper.compareDates(date, holiday.getDate()) == 0)
					repeated = true;
			}
			if (!repeated) {
				Holidays hol = createHoliday();
				hol.setDate(DateHelper.date2XMLGregorianCalendar(date));
				hol.setComment(comment);
				holidays.add(hol);
			}
			removeFromFreeDays(userConfig, date, profileName);
			removeFromWorkedDays(xmlObject, date, profileName);
		} catch (DateException e) {
			throw new ORMException(e);
		}

	}

	public static void addWorkDays(AnualHours anualHours, UserConfiguration userConfig, Date date,
			Float hours, String description, String profileName) throws ORMException {
		try {
			boolean repeated = false;
			AnualHours xmlObject = anualHours;
			List<WorkedHours> workedDays = getUsersWorkedHourList(xmlObject, profileName);
			for (WorkedHours day : workedDays) {
				if (DateHelper.compareDates(date, day.getDate()) == 0) {
					repeated = true;
					day.setDate(DateHelper.date2XMLGregorianCalendar(date));
					day.setDescription(description);
					day.setHours(hours);
				}
			}
			if (!repeated) {
				WorkedHours workedDay = createWorkedHours();
				workedDay.setDate(DateHelper.date2XMLGregorianCalendar(date));
				workedDay.setDescription(description);
				workedDay.setHours(hours);
				workedDays.add(workedDay);
			}
			removeFromFreeDays(userConfig, date, profileName);
			removeFromHolidays(anualHours, date, profileName);
		} catch (Exception e) {
			throw new ORMException(e);
		}
	}
}
