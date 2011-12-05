package com.hcalendar.data.calculator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.hcalendar.data.calculator.exception.CalculatorException;
import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.data.orm.exception.ORMException;
import com.hcalendar.data.orm.impl.ORMHelper;
import com.hcalendar.data.utils.DateHelper;
import com.hcalendar.data.utils.DateIterator;
import com.hcalendar.data.utils.exception.DateException;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User.YearConf;
import com.hcalendar.data.xml.workedhours.AnualHours;
import com.hcalendar.data.xml.workedhours.AnualHours.UserInput;
import com.hcalendar.data.xml.workedhours.AnualHours.UserInput.WorkedHours;

/**
 * Calculator methods
 * */
public class Calculator {

	/**
	 * Calculates year to show for a given profile
	 * 
	 * @param userConfiguration
	 *            anual configuration java bean
	 * @param username
	 *            Username which get the hour input
	 * @param year
	 *            year
	 * 
	 * @return calculated year
	 * */
	public static int calculateYearConfigForProfile(
			UserConfiguration userConfiguration, String username, Integer year) {
		List<Integer> years = new ArrayList<Integer>();
		List<User> usersList = userConfiguration.getUser();
		for (User user : usersList) {
			if (user.getName().equals(username)) {
				for (YearConf yearCgf : user.getYearConf())
					years.add(yearCgf.getYear());
			}
		}
		// Calculate optimal year
		if (years.contains(year))
			return year;
		for (int i = 1; i < 50; i++) {
			if (years.contains(year + i))
				return year + i;
		}
		for (int i = 1; i < 50; i++) {
			if (years.contains(year - i))
				return year - i;
		}
		return 0;
	}

	/**
	 * Calculates the planned hours of a given user and year
	 * 
	 * @param orm
	 *            orm instance
	 * @param name
	 *            Username which get the hour input
	 * @param year
	 *            year
	 * @param listaDiasLaborales
	 *            Labour days of week
	 * @param dLibresList
	 *            Calandar free days of a given year
	 * @param ovewriteProfile
	 *            ovewrite file?
	 * 
	 * @return AnualHours java bean
	 * @throws CalculatorException
	 * */
	@SuppressWarnings("deprecation")
	public static AnualHours calculatePlannedHoursOfYear(IORMClient orm,
			String name, Integer year, Map<Integer, String> listaDiasLaborales,
			List<String> dLibresList, boolean ovewriteProfile)
			throws CalculatorException {
		try {
			final AnualHours anualHours = orm.getAnualHours();
			com.hcalendar.data.xml.workedhours.ObjectFactory of = new com.hcalendar.data.xml.workedhours.ObjectFactory();

			if (ovewriteProfile) {
				for (UserInput userTemp : anualHours.getUserInput())
					if (userTemp.getUserName().equals(name)) {
						anualHours.getUserInput().remove(userTemp);
						break;
					}
			}

			UserInput userInput = of.createAnualHoursUserInput();
			userInput.setUserName(name);
			List<Date> dLibresDateList = DateHelper.parse2Date(dLibresList);

			// Obtener lista de dias festivos
			List<Integer> listaDiasFestivos = new ArrayList<Integer>();
			for (int i = 0; i < (Calendar.SATURDAY + 1); i++) {
				if (!listaDiasLaborales.containsKey(i))
					listaDiasFestivos.add(i);
			}

			// Insertar horas según calendario laboral
			DateIterator dateIt = new DateIterator(new Date(year - 1900, 0, 1),
					new Date(year - 1900, 11, 31));
			// Iterator<Date> it = dateIt.iterator();
			Date today;
			List<WorkedHours> woHours = userInput.getWorkedHours();
			while (dateIt.hasNext()) {
				today = dateIt.next();
				if (DateHelper.containsDate(dLibresDateList, today))
					continue;
				if (!listaDiasFestivos.contains(dateIt.getDayOfTheWeek())) {
					WorkedHours hours = of
							.createAnualHoursUserInputWorkedHours();
					XMLGregorianCalendar fec;
					fec = DatatypeFactory.newInstance()
							.newXMLGregorianCalendar(
									new GregorianCalendar(
											today.getYear() + 1900, today
													.getMonth(), today
													.getDate()));

					hours.setDate(fec);
					hours.setDescription("Horas introducidas según configuración del usuario");
					hours.setHours(Float.parseFloat(listaDiasLaborales
							.get(dateIt.getDayOfTheWeek())));
					System.out.println("Sumadas "
							+ Float.parseFloat(listaDiasLaborales.get(dateIt
									.getDayOfTheWeek())) + " del dia " + fec);
					woHours.add(hours);
				}
			}

			anualHours.getUserInput().add(userInput);
			return anualHours;
		} catch (DatatypeConfigurationException e) {
			throw new CalculatorException(e);
		} catch (ORMException e) {
			throw new CalculatorException(e);
		} catch (DateException e) {
			throw new CalculatorException(e);
		}
	}

	/**
	 * Calculates the anual hours until a given day
	 * 
	 * @param anualHours
	 *            AnualHours bean
	 * @param date
	 *            Date
	 * @param username
	 *            Username which get the hour input
	 * 
	 * @return hours of the year until the given date
	 * */
	@SuppressWarnings("deprecation")
	public static float calculateHoursUntilDate(AnualHours anualHours,
			Date date, String username) {
		float result = 0;
		AnualHours hours = anualHours;
		List<WorkedHours> hoursList = ORMHelper.getUsersWorkedHourList(hours,
				username);
		for (int i = 0; i < hoursList.size(); i++) {
			WorkedHours hDay = hoursList.get(i);
			// Filtrar por año
			if (date.getYear() + 1900 != hDay.getDate().getYear())
				continue;
			if (DateHelper.compareDates(date, hDay.getDate()) != -1) {
				result += hDay.getHours();
				System.out.println("Sumando " + hDay.getHours() + " del dia "
						+ hDay.getDate() + ", total = " + result);
			}
		}
		System.out.println("Calculated hours=" + result);
		return result;
	}

	/**
	 * Calculates the hours of a given day
	 * 
	 * @param anualHours
	 *            AnualHours bean
	 * @param date
	 *            Date
	 * @param username
	 *            Username which get the hour input
	 * 
	 * @return hours of day
	 * */
	@SuppressWarnings("deprecation")
	public static float calculateHoursOfDate(AnualHours anualHours, Date date,
			String username) {
		float result = 0;
		AnualHours hours = anualHours;
		List<WorkedHours> hoursList = ORMHelper.getUsersWorkedHourList(hours,
				username);
		for (int i = 0; i < hoursList.size(); i++) {
			WorkedHours hDay = hoursList.get(i);
			// Filtrar por año
			if (date.getYear() + 1900 != hDay.getDate().getYear())
				continue;
			if (DateHelper.compareDates(date, hDay.getDate()) == 0) {
				result += hDay.getHours();
				System.out.println("Sumando " + hDay.getHours() + " del dia "
						+ hDay.getDate() + ", total = " + result);
			}
		}
		System.out.println("Calculated hours=" + result);
		return result;
	}

	/**
	 * calculate planned hours for a given year and a profile
	 * 
	 * @param anualConfig
	 *            anual configuration java bean
	 * @param year
	 *            year
	 * @param profileName
	 *            profile name which get the hour input
	 * 
	 * @return Calendar hours from the data layer
	 * */
	public static float calculateAnualPlannedHours(AnualHours anualConfig,
			int year, String profileName) {
		float sumOfHours = 0;
		for (WorkedHours wHours : ORMHelper.getUsersWorkedHourList(anualConfig,
				profileName)) {
			if (wHours.getDate().getYear() == year)
				sumOfHours = sumOfHours + wHours.getHours();
		}
		return sumOfHours;
	}

}
