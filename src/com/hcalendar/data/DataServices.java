package com.hcalendar.data;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hcalendar.ConfigurationNotInitedException;
import com.hcalendar.config.ConfigurationUtils;
import com.hcalendar.data.calculator.Calculator;
import com.hcalendar.data.calculator.exception.CalculatorException;
import com.hcalendar.data.exception.BusinessException;
import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.data.orm.exception.ORMException;
import com.hcalendar.data.orm.impl.ORMHelper;
import com.hcalendar.data.utils.DateHelper;
import com.hcalendar.data.utils.exception.DateException;
import com.hcalendar.data.xml.userconfiguration.ObjectFactory;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User.YearConf;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User.YearConf.FreeDays;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User.YearConf.FreeDays.FreeDay;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User.YearConf.WorkingDays;
import com.hcalendar.data.xml.workedhours.AnualHours;
import com.hcalendar.data.xml.workedhours.AnualHours.UserInput.WorkedHours;
import com.hcalendar.ui.widgets.impl.JWindowUtils;

public class DataServices {

	@SuppressWarnings("deprecation")
	private static void monthResumeCSV(Writer writer, int year, IORMClient orm,
			String profileName) throws BusinessException {
		try {
			writer.append("Mes");
			writer.append(',');
			writer.append("Horas");
			writer.append('\n');

			// Write columns
			int daysOnMonth;
			float lastCalculatedHours = 0;
			for (int i = 0; i < DateHelper.months.length; i++) {
				if (DateHelper.isLeap(year) && i == Calendar.FEBRUARY)
					daysOnMonth = DateHelper.daysOnMonth[i] + 1;
				else
					daysOnMonth = DateHelper.daysOnMonth[i];
				float hours = Calculator.calculateHoursUntilDate(orm
						.getAnualHours(),
						new Date(year - 1900, i, daysOnMonth), profileName);
				writer.append(DateHelper.months[i]);
				writer.append(',');
				writer.append(String.valueOf(hours - lastCalculatedHours));
				writer.append('\n');
				lastCalculatedHours = hours;
			}
		} catch (IOException e) {
			throw new BusinessException(e);
		} catch (ORMException e) {
			throw new BusinessException(e);
		}
	}

	@SuppressWarnings("deprecation")
	private static void dayliResumeCSV(Writer writer, int year, IORMClient orm,
			String profileName) throws BusinessException {
		try {
			writer.append("Dia");
			writer.append(',');
			writer.append("Tipo");

			writer.append(',');

			writer.append("Horas");
			writer.append(',');
			writer.append("Comentarios");
			writer.append('\n');

			// Write columns
			int daysOnMonth;
			float lastCalculatedHours = 0;
			float hoursOfMonth = 0;

			AnualHours anualHours = orm.getAnualHours();
			List<WorkedHours> workedHours = ORMHelper.getUsersWorkedHourList(
					anualHours, profileName);
			for (WorkedHours days : workedHours) {
				Date date = DateHelper
						.xmlGregorianCalendar2Date(days.getDate());
				writer.append(DateHelper.DATE_FORMAT.format(date));
				writer.append(',');
				writer.append(DateHelper.translateDayOfWeek(date.getDay()));
				writer.append(',');
				writer.append(days.getHours() > 0.0 ? String.valueOf(days
						.getHours()) : "");
				writer.append(',');
				writer.append(days.getDescription());
				writer.append('\n');
			}
			// Month resume
			for (int i = 0; i < DateHelper.months.length; i++) {
				if (DateHelper.isLeap(year) && i == Calendar.FEBRUARY)
					daysOnMonth = DateHelper.daysOnMonth[i] + 1;
				else
					daysOnMonth = DateHelper.daysOnMonth[i];
				hoursOfMonth = Calculator.calculateHoursUntilDate(orm
						.getAnualHours(),
						new Date(year - 1900, i, daysOnMonth), profileName);
				writer.append("Resumen del mes");
				writer.append(',');
				writer.append(DateHelper.months[i]);
				writer.append(',');
				writer.append(String
						.valueOf(hoursOfMonth - lastCalculatedHours) + " horas");
				writer.append(',');
				writer.append("");
				writer.append('\n');
				lastCalculatedHours = hoursOfMonth;
			}
			// Year resume
			writer.append("Resumen del año");
			writer.append(',');
			writer.append(String.valueOf(year));
			writer.append(',');
			writer.append(hoursOfMonth + " horas");
			writer.append('\n');

			// for (int i = 0; i < DateHelper.months.length; i++) {
			// if (DateHelper.isLeap(year) && i == Calendar.FEBRUARY)
			// daysOnMonth = DateHelper.daysOnMonth[i] + 1;
			// else
			// daysOnMonth = DateHelper.daysOnMonth[i];
			// float hoursOfMonth =
			// Calculator.calculateHoursUntilDate(orm.getAnualHours(), new Date(
			// year - 1900, i, daysOnMonth));
			// // Calculate each day hours
			// for (int k = 0; k < daysOnMonth; k++) {
			// Date date = new Date(year - 1900, i, k + 1);
			// float hoursOfDay =
			// Calculator.calculateHoursOfDate(orm.getAnualHours(), date);
			// writer.append(DateHelper.DATE_FORMAT.format(date));
			// writer.append(',');
			// writer.append(DateHelper.translateDayOfWeek(date.getDay()));
			// writer.append(',');
			// writer.append(hoursOfDay > 0.0 ? String.valueOf(hoursOfDay) :
			// "");
			// writer.append(',');
			// writer.append("desc");
			// writer.append('\n');
			// }
			// // Month resume
			// writer.append("Resumen del mes");
			// writer.append(',');
			// writer.append(DateHelper.months[i]);
			// writer.append(',');
			// writer.append(String.valueOf(hoursOfMonth - lastCalculatedHours)
			// + " horas");
			// writer.append(',');
			// writer.append("");
			// writer.append('\n');
			// lastCalculatedHours = hoursOfMonth;
			// }
		} catch (IOException e) {
			throw new BusinessException(e);
		} catch (ORMException e) {
			throw new BusinessException(e);
		}
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
	 * @param calendarHours
	 *            hours of the (spanish:convenio)
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
	public static UserConfiguration createAnualConfigurationProfile(
			IORMClient orm, String name, int year, String calendarHours,
			Map<Integer, String> listaDiasLaborales, List<String> dLibres,
			boolean ovewriteProfile) throws BusinessException {
		try {
			ObjectFactory of = new ObjectFactory();
			final UserConfiguration anualConfig = orm.getAnualConfiguration();
			if (ovewriteProfile) {
				for (User userTemp : anualConfig.getUser())
					if (userTemp.getName().equals(name)) {
						anualConfig.getUser().remove(userTemp);
						break;
					}
			}
			final User user = of.createUserConfigurationUser();
			user.setName(name);
			List<YearConf> yearList = user.getYearConf();
			YearConf yearConf = of.createUserConfigurationUserYearConf();
			yearConf.setCalendarHours(Float.valueOf(calendarHours));
			List<WorkingDays> calWorkinDays = yearConf.getWorkingDays();
			WorkingDays wd;
			for (Integer day : listaDiasLaborales.keySet()) {
				wd = of.createUserConfigurationUserYearConfWorkingDays();
				wd.setWorkingDay(String.valueOf(day));
				wd.setHours(Float.valueOf(listaDiasLaborales.get(day)));
				calWorkinDays.add(wd);
			}

			FreeDays fDays = of.createUserConfigurationUserYearConfFreeDays();
			List<FreeDay> freeDays = fDays.getFreeDay();
			for (String day : dLibres) {
				FreeDay freeDay = of
						.createUserConfigurationUserYearConfFreeDaysFreeDay();
				freeDay.setDay(DateHelper.parse2XMLGregorianCalendar(day));
				freeDay.setComment("Dias libres de configuracion de usuario");
				freeDays.add(freeDay);
			}
			yearConf.setFreeDays(fDays);
			yearConf.setYear(year);
			yearList.add(yearConf);

			anualConfig.getUser().add(user);
			return anualConfig;
		} catch (DateException e) {
			throw new BusinessException(e);
		} catch (ORMException e) {
			throw new BusinessException(e);
		}
	}

	/**
	 * Export data to CSV with two option: - By day: resume by day - By month:
	 * resume by month
	 * 
	 * @param orm
	 *            orm instance
	 * @param year
	 *            year
	 * @param profileName
	 *            Username which get the hour input
	 * @param component
	 *            canvas to show option panel on it
	 * 
	 * */
	public static int exportToCSV(IORMClient orm, int year, String profileName,
			final Component component) throws BusinessException {
		try {
			int selectedOption = JWindowUtils.showOptionPanel(component,
					"Seleccione el tipo de informe que desea visualizar",
					new Object[] { "Desglosado por meses",
							"Desglosado por días" });
			FileWriter writer = new FileWriter(
					ConfigurationUtils.getCSVTempFile());
			if (selectedOption == 0)
				monthResumeCSV(writer, year, orm, profileName);
			else if (selectedOption == 1)
				dayliResumeCSV(writer, year, orm, profileName);
			else
				return -1;
			writer.flush();
			writer.close();

			// Open with excel
			if (!Desktop.isDesktopSupported()) {
				Process p = Runtime.getRuntime().exec(
						"rundll32 url.dll,FileProtocolHandler "
								+ ConfigurationUtils.getCSVTempFile());
				// use alternative (Runtime.exec)
				return -1;
			}

			Desktop desktop = Desktop.getDesktop();
			if (!desktop.isSupported(Desktop.Action.OPEN)) {
				System.err.println("OPEN not supported");
				// use alternative (Runtime.exec)
				return -1;
			}

			desktop.open(new File(ConfigurationUtils.getCSVTempFile()));

		} catch (IOException e) {
			throw new BusinessException(e);
		} catch (ConfigurationNotInitedException e) {
			throw new BusinessException(e);
		}
		return 0;
	}
}
