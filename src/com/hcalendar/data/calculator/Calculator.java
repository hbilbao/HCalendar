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
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User;
import com.hcalendar.data.xml.workedhours.AnualHours;
import com.hcalendar.data.xml.workedhours.AnualHours.UserInput;
import com.hcalendar.data.xml.workedhours.AnualHours.UserInput.WorkedHours;

public class Calculator {

	@SuppressWarnings("deprecation")
	public static AnualHours calculatePlannedHoursOfYear(IORMClient orm, String name, Integer year,
			Map<Integer, String> listaDiasLaborales, List<String> dLibresList, boolean ovewriteProfile) throws CalculatorException {
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
			List<Date> dLibresDateList = DateHelper.convertToDate(dLibresList);

			// Obtener lista de dias festivos
			List<Integer> listaDiasFestivos = new ArrayList<Integer>();
			for (int i = 0; i < (Calendar.SATURDAY + 1); i++) {
				if (!listaDiasLaborales.containsKey(i))
					listaDiasFestivos.add(i);
			}

			// Insertar horas según calendario laboral
			DateIterator dateIt = new DateIterator(new Date(year - 1900, 0, 1), new Date(year - 1900, 11, 31));
			// Iterator<Date> it = dateIt.iterator();
			Date today;
			List<WorkedHours> woHours = userInput.getWorkedHours();
			while (dateIt.hasNext()) {
				today = dateIt.next();
				if (DateHelper.containsDate(dLibresDateList, today))
					continue;
				if (!listaDiasFestivos.contains(dateIt.getDayOfTheWeek())) {
					WorkedHours hours = of.createAnualHoursUserInputWorkedHours();
					XMLGregorianCalendar fec;
					fec = DatatypeFactory.newInstance().newXMLGregorianCalendar(
							new GregorianCalendar(today.getYear() + 1900, today.getMonth(), today.getDate()));

					hours.setDate(fec);
					hours.setDescription("Horas introducidas según configuración del usuario");
					hours.setHours(Float.parseFloat(listaDiasLaborales.get(dateIt.getDayOfTheWeek())));
					System.out.println("Sumadas "
							+ Float.parseFloat(listaDiasLaborales.get(dateIt.getDayOfTheWeek()))
							+ " del dia " + fec);
					woHours.add(hours);
				}
			}

			anualHours.getUserInput().add(userInput);
			return anualHours;
		} catch (DatatypeConfigurationException e) {
			throw new CalculatorException(e);
		} catch (ORMException e) {
			throw new CalculatorException(e);
		}
	}

	@SuppressWarnings("deprecation")
	public static float calculateHoursUntilDate(AnualHours anualHours, Date date, String username) {
		float result = 0;
		AnualHours hours = anualHours;
		List<WorkedHours> hoursList = ORMHelper.getUsersWorkedHourList(hours, username);
		for (int i = 0; i < hoursList.size(); i++) {
			WorkedHours hDay = hoursList.get(i);
			// for (WorkedHours hDay : hoursList) {
			// System.out.println("Checking " + hDay.getDate());
			// Filtrar por año
			if (date.getYear() + 1900 != hDay.getDate().getYear())
				continue;
			if (DateHelper.compareDates(date, hDay.getDate()) != -1) {
				result += hDay.getHours();
				System.out.println("Sumando " + hDay.getHours() + " del dia " + hDay.getDate() + ", total = "
						+ result);
			}
		}
		System.out.println("Calculated hours=" + result);
		return result;
	}

	@SuppressWarnings("deprecation")
	public static float calculateHoursOfDate(AnualHours anualHours, Date date, String username) {
		float result = 0;
		AnualHours hours = anualHours;
		List<WorkedHours> hoursList = ORMHelper.getUsersWorkedHourList(hours, username);
		for (int i = 0; i < hoursList.size(); i++) {
			WorkedHours hDay = hoursList.get(i);
			// for (WorkedHours hDay : hoursList) {
			// System.out.println("Checking " + hDay.getDate());
			// Filtrar por año
			if (date.getYear() + 1900 != hDay.getDate().getYear())
				continue;
			if (DateHelper.compareDates(date, hDay.getDate()) == 0) {
				result += hDay.getHours();
				System.out.println("Sumando " + hDay.getHours() + " del dia " + hDay.getDate() + ", total = "
						+ result);
			}
		}
		System.out.println("Calculated hours=" + result);
		return result;
	}
}
