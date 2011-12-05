package com.hcalendar.fop.dto;

//Java
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.hcalendar.data.dto.WorkInputsDTO;
import com.hcalendar.data.dto.WorkInputsDTO.FreeDay;
import com.hcalendar.data.dto.WorkInputsDTO.Holiday;
import com.hcalendar.data.dto.WorkInputsDTO.WorkInput;
import com.hcalendar.data.utils.DateHelper;
import com.hcalendar.fop.tools.AbstractObjectReader;

/**
 * XMLReader implementation for the ProjectTeam class. This class is used to
 * generate SAX events from the ProjectTeam class.
 */
public class WorkInputsDTOXMLReader extends AbstractObjectReader {

	/**
	 * @see org.xml.sax.XMLReader#parse(InputSource)
	 */
	public void parse(InputSource input) throws IOException, SAXException {
		if (input instanceof WorkInputsInputSource) {
			parse(((WorkInputsInputSource) input).getWorkInputs());
		} else {
			throw new SAXException("Unsupported InputSource specified. "
					+ "Must be a ProjectTeamInputSource");
		}
	}

	/**
	 * Starts parsing the WorkInputsDTO object.
	 * 
	 * @param workInputs
	 *            The object to parse
	 * @throws SAXException
	 *             In case of a problem during SAX event generation
	 */
	public void parse(WorkInputsDTO workInputs) throws SAXException {
		if (workInputs == null) {
			throw new NullPointerException(
					"Parameter projectTeam must not be null");
		}
		if (handler == null) {
			throw new IllegalStateException("ContentHandler not set");
		}

		// Start the document
		handler.startDocument();

		// Generate SAX events for the workInputs
		generateFor(workInputs);

		// End the document
		handler.endDocument();
	}

	/**
	 * Generates SAX events for a ProjectTeam object.
	 * 
	 * @param workInputs
	 *            ProjectTeam object to use
	 * @throws SAXException
	 *             In case of a problem during SAX event generation
	 */
	protected void generateFor(WorkInputsDTO workInputs) throws SAXException {
		if (workInputs == null) {
			throw new NullPointerException(
					"Parameter projectTeam must not be null");
		}
		if (handler == null) {
			throw new IllegalStateException("ContentHandler not set");
		}

		//	general attributes
		handler.startElement("WorkInputsDTO");
		handler.element("profileName", workInputs.getProfileName());
		handler.element("year", String.valueOf(workInputs.getYear()));
		if (workInputs.getFromFilter() != null
				&& workInputs.getToFilter() != null) {
			handler.element("fromFilter",
					DateHelper.DATE_FORMAT.format(workInputs.getFromFilter()));
			handler.element("toFilter",
					DateHelper.DATE_FORMAT.format(workInputs.getToFilter()));
		}
		// worked days
		generateWorkingDays(workInputs.getWorkInput());
		// holidays
		generateHolidays(workInputs.getHolidays());
		// freedays
		generateFreeDays(workInputs.getFreeDays());
		//	month resume
		float monthHoursResult = generateMonthHoursResume(workInputs.getMonthHoursResume());
		handler.element("totalHours", String.valueOf(monthHoursResult));
		handler.endElement("WorkInputsDTO");
	}

	private void generateHolidays(List<Holiday> holidays) throws SAXException {
		Iterator<Holiday> i = holidays.iterator();
		// If not holidays, create default
		if (!i.hasNext()) {
			handler.startElement("holiday");
			handler.endElement("holiday");
		}
		while (i.hasNext()) {
			Holiday holiday = (Holiday) i.next();
			if (holiday == null) {
				throw new NullPointerException(
						"Parameter projectMember must not be null");
			}
			if (handler == null) {
				throw new IllegalStateException("ContentHandler not set");
			}

			handler.startElement("holiday");
			String dateToPrint = DateHelper.DATE_FORMAT.format(DateHelper
					.xmlGregorianCalendar2Date(holiday.getDate()));
			handler.element("date", dateToPrint);
			handler.element("comment", holiday.getComment());
			handler.endElement("holiday");
		}
	}

	private void generateWorkingDays(List<WorkInput> workInputs)
			throws SAXException {
		Iterator<WorkInput> i = workInputs.iterator();
		// If not worked days, create default
		if (!i.hasNext()) {
			handler.startElement("workInput");
			handler.endElement("workInput");
		}
		while (i.hasNext()) {
			WorkInput workInput = (WorkInput) i.next();
			if (workInput == null) {
				throw new NullPointerException(
						"Parameter projectMember must not be null");
			}
			if (handler == null) {
				throw new IllegalStateException("ContentHandler not set");
			}

			handler.startElement("workInput");
			String dateToPrint = DateHelper.DATE_FORMAT.format(DateHelper
					.xmlGregorianCalendar2Date(workInput.getDate()));
			handler.element("date", dateToPrint);
			handler.element("hours", String.valueOf(workInput.getHours()));
			handler.element("description", workInput.getDescription());
			handler.endElement("workInput");
		}
	}
	
	private void generateFreeDays(List<FreeDay> list) throws SAXException {
		Iterator<FreeDay> i = list.iterator();
		// If not holidays, create default
		if (!i.hasNext()) {
			handler.startElement("freeday");
			handler.endElement("freeday");
		}
		while (i.hasNext()) {
			FreeDay day = (FreeDay) i.next();
			if (day == null) {
				throw new NullPointerException(
						"Parameter projectMember must not be null");
			}
			if (handler == null) {
				throw new IllegalStateException("ContentHandler not set");
			}

			handler.startElement("freeday");
			String dateToPrint = DateHelper.DATE_FORMAT.format(DateHelper
					.xmlGregorianCalendar2Date(day.getDay()));
			handler.element("date", dateToPrint);
			handler.element("comment", day.getComment());
			handler.endElement("freeday");
		}
	}
	

	private float generateMonthHoursResume(Map<String, Float> monthHoursResume) throws SAXException {
		Set<String> keys = monthHoursResume.keySet();
		// If not holidays, create default
		if (keys.size()==0) {
			handler.startElement("monthresume");
			handler.endElement("monthresume");
		}
		float totHours = 0;
		for (String monthName: keys) {
			totHours = totHours + monthHoursResume.get(monthName);
			Float hours = monthHoursResume.get(monthName);
			handler.startElement("monthresume");
			handler.element("monthName", monthName);
			handler.element("monthHours", String.valueOf(hours));
			handler.endElement("monthresume");
		}
		return totHours;
	}
}
