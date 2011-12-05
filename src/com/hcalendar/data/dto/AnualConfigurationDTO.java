package com.hcalendar.data.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * This bean represents a Anual configuration.
 */
@Deprecated
public class AnualConfigurationDTO {

	private String username;
	private Integer year;
	private Float calendarHours;
	private List<FreeDay> calendarFreeDays = new ArrayList<AnualConfigurationDTO.FreeDay>();
	private List<WorkingDay> workingDays = new ArrayList<AnualConfigurationDTO.WorkingDay>();

	/**
	 * Default no-parameter constructor.
	 */
	public AnualConfigurationDTO() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Float getCalendarHours() {
		return calendarHours;
	}

	public void setCalendarHours(Float calendarHours) {
		this.calendarHours = calendarHours;
	}

	public List<FreeDay> getCalendarFreeDays() {
		return calendarFreeDays;
	}

	public void setCalendarFreeDays(List<FreeDay> calendarFreeDays) {
		this.calendarFreeDays = calendarFreeDays;
	}

	public void addCalendarFreeDays(FreeDay calendarFreeDays) {
		this.calendarFreeDays.add(calendarFreeDays);
	}

	public List<WorkingDay> getWorkingDays() {
		return workingDays;
	}

	public void setWorkingDays(List<WorkingDay> workingDays) {
		this.workingDays = workingDays;
	}

	public void addWorkingDays(WorkingDay workingDay) {
		this.workingDays.add(workingDay);
	}

	private static class FreeDay {

		protected XMLGregorianCalendar day;
		protected String comment;

		/**
		 * Gets the value of the day property.
		 * 
		 * @return possible object is {@link XMLGregorianCalendar }
		 * 
		 */
		public XMLGregorianCalendar getDay() {
			return day;
		}

		/**
		 * Sets the value of the day property.
		 * 
		 * @param value
		 *            allowed object is {@link XMLGregorianCalendar }
		 * 
		 */
		public void setDay(XMLGregorianCalendar value) {
			this.day = value;
		}

		/**
		 * Gets the value of the comment property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getComment() {
			return comment;
		}

		/**
		 * Sets the value of the comment property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setComment(String value) {
			this.comment = value;
		}

	}

	public static class WorkingDay {

		protected String workingDay;
		protected float hours;

		/**
		 * Gets the value of the workingDay property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getWorkingDay() {
			return workingDay;
		}

		/**
		 * Sets the value of the workingDay property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setWorkingDay(String value) {
			this.workingDay = value;
		}

		/**
		 * Gets the value of the hours property.
		 * 
		 */
		public float getHours() {
			return hours;
		}

		/**
		 * Sets the value of the hours property.
		 * 
		 */
		public void setHours(float value) {
			this.hours = value;
		}

	}
}
