package com.hcalendar.data.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import com.hcalendar.data.xml.workedhours.AnualHours.UserInput.Holidays;
import com.hcalendar.data.xml.workedhours.AnualHours.UserInput.WorkedHours;
import com.hcalendar.fop.dto.WorkInputsDTOXMLReader;
import com.hcalendar.fop.dto.WorkInputsInputSource;

/**
 * This bean represents a hour inputs for a user.
 */
public class WorkInputsDTO {

	private String profileName;
	private Integer year;
	private List<WorkInput> workInput = new ArrayList<WorkInput>();
	private Map<String,Float> monthHoursResume = new LinkedHashMap<String, Float>();
	private List<Holiday> holidays = new ArrayList<Holiday>();
	private List<FreeDay> freeDays = new ArrayList<FreeDay>();
	
	//	Adittional data
	private Date fromFilter;
	private Date toFilter;

	/**
	 * Default no-parameter constructor.
	 */
	public WorkInputsDTO() {
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public List<WorkInput> getWorkInput() {
		return workInput;
	}

	public void setWorkInput(List<WorkInput> workInput) {
		this.workInput = workInput;
	}

	public void addWorkInput(WorkInput workInput) {
		this.workInput.add(workInput);
	}

	public List<Holiday> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<Holiday> holidays) {
		this.holidays = holidays;
	}

	public void addHoliday(Holiday holiday) {
		this.holidays.add(holiday);
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Date getFromFilter() {
		return fromFilter;
	}

	public void setFromFilter(Date fromFilter) {
		this.fromFilter = fromFilter;
	}

	public Date getToFilter() {
		return toFilter;
	}

	public void setToFilter(Date toFilter) {
		this.toFilter = toFilter;
	}
	

	public List<FreeDay> getFreeDays() {
		return freeDays;
	}

	public void setFreeDays(List<FreeDay> freeDays) {
		this.freeDays = freeDays;
	}

	public void addFreeDay(FreeDay freeDay) {
		this.freeDays.add(freeDay);
	}

	public Map<String,Float> getMonthHoursResume() {
		return monthHoursResume;
	}

	public void setMonthHoursResume(Map<String,Float> monthHoursResume) {
		this.monthHoursResume = monthHoursResume;
	}

	
	/**
	 * Resturns a Source object for this object so it can be used as input for a
	 * JAXP transformation.
	 * 
	 * @return Source The Source object
	 */
	public Source getSource() {
		return new SAXSource(new WorkInputsDTOXMLReader(),
				new WorkInputsInputSource(this));
	}

	public static class Holiday {

		protected XMLGregorianCalendar date;
		protected String comment;

		public Holiday(Holidays hol) {
			this.date = hol.getDate();
			this.comment = hol.getComment();
		}

		/**
		 * Gets the value of the date property.
		 * 
		 * @return possible object is {@link XMLGregorianCalendar }
		 * 
		 */
		public XMLGregorianCalendar getDate() {
			return date;
		}

		/**
		 * Sets the value of the date property.
		 * 
		 * @param value
		 *            allowed object is {@link XMLGregorianCalendar }
		 * 
		 */
		public void setDate(XMLGregorianCalendar value) {
			this.date = value;
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

	public static class WorkInput {

		protected XMLGregorianCalendar date;
		protected float hours;
		protected String description;

		public WorkInput(WorkedHours workedHours) {
			this.date = workedHours.getDate();
			this.hours = workedHours.getHours();
			this.description = workedHours.getDescription();
		}

		public WorkInput() {
		}

		/**
		 * Gets the value of the date property.
		 * 
		 * @return possible object is {@link XMLGregorianCalendar }
		 * 
		 */
		public XMLGregorianCalendar getDate() {
			return date;
		}

		/**
		 * Sets the value of the date property.
		 * 
		 * @param value
		 *            allowed object is {@link XMLGregorianCalendar }
		 * 
		 */
		public void setDate(XMLGregorianCalendar value) {
			this.date = value;
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

		/**
		 * Gets the value of the description property.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * Sets the value of the description property.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setDescription(String value) {
			this.description = value;
		}
	}
	
	public static class FreeDay {

		protected XMLGregorianCalendar day;
		protected String comment;

		public FreeDay(com.hcalendar.data.xml.userconfiguration.UserConfiguration.User.YearConf.FreeDays.FreeDay day2) {
			this.setDay(day2.getDay());
			this.setComment(day2.getComment());
		}

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
}
