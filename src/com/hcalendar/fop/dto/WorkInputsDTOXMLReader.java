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
 * XMLReader implementation for the WorkInputsDTO class. This class is used to
 * generate SAX events from the WorkInputsDTO class.
 */
public class WorkInputsDTOXMLReader extends AbstractObjectReader {

	private static final String XSL_TEMPLATE_MAIN_TEMPLATE = "WorkInputsDTO";
	private static final String XSL_TEMPLATE_WORKING_DAYS = "workInput";
	private static final String XSL_TEMPLATE_HOLIDAY = "holiday";
	private static final String XSL_TEMPLATE_FREEDAY = "freeday";
	private static final String XSL_TEMPLATE_MONTHRESUME = "monthresume";

	// Main template elements
	private static final String XSL_ELEMENT_MAIN_TEMPLATE_PROFILENAME = "profileName";
	private static final String XSL_ELEMENT_MAIN_TEMPLATE_YEAR = "year";
	private static final String XSL_ELEMENT_MAIN_TEMPLATE_FROMFILTER = "fromFilter";
	private static final String XSL_ELEMENT_MAIN_TEMPLATE_TOFILTER = "toFilter";
	private static final String XSL_ELEMENT_MAIN_TEMPLATE_TOTALHOURS = "totalHours";

	// Work inputs template elements
	private static final String XSL_ELEMENT_WORKINPUT_TEMPLATE_DATE = "date";
	private static final String XSL_ELEMENT_WORKINPUT_TEMPLATE_HOURS = "hours";
	private static final String XSL_ELEMENT_WORKINPUT_TEMPLATE_DESCRIPTION = "description";

	// Holidays template elements
	private static final String XSL_ELEMENT_HOLIDAYS_TEMPLATE_DATE = "date";
	private static final String XSL_ELEMENT_HOLIDAYS_TEMPLATE_COMMENT = "comment";

	// Free days template elements
	private static final String XSL_ELEMENT_FREEDAYS_TEMPLATE_DATE = "date";
	private static final String XSL_ELEMENT_FREEDAYS_TEMPLATE_COMMENT = "comment";

	// Free days template elements
	private static final String XSL_ELEMENT_MONTHRESUME_TEMPLATE_MONTHNAME = "monthName";
	private static final String XSL_ELEMENT_MONTHRESUME_TEMPLATE_MONTHHOURS = "monthHours";

	/**
	 * @see org.xml.sax.XMLReader#parse(InputSource)
	 */
	@Override
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
		generateMainSAXEvents(workInputs);

		// End the document
		handler.endDocument();
	}

	/**
	 * Generates SAX events for a WorkInputsDTO object.
	 * 
	 * @param workInputs
	 *            WorkInputsDTO object to use
	 * @throws SAXException
	 *             In case of a problem during SAX event generation
	 */
	private void generateMainSAXEvents(WorkInputsDTO workInputs)
			throws SAXException {
		if (workInputs == null) {
			throw new NullPointerException(
					"Parameter workInputs must not be null");
		}
		if (handler == null) {
			throw new IllegalStateException("ContentHandler not set");
		}

		// general attributes
		handler.startElement(XSL_TEMPLATE_MAIN_TEMPLATE);
		handler.element(XSL_ELEMENT_MAIN_TEMPLATE_PROFILENAME,
				workInputs.getProfileName());
		handler.element(XSL_ELEMENT_MAIN_TEMPLATE_YEAR,
				String.valueOf(workInputs.getYear()));
		if (workInputs.getFromFilter() != null
				&& workInputs.getToFilter() != null) {
			handler.element(XSL_ELEMENT_MAIN_TEMPLATE_FROMFILTER,
					DateHelper.DATE_FORMAT.format(workInputs.getFromFilter()));
			handler.element(XSL_ELEMENT_MAIN_TEMPLATE_TOFILTER,
					DateHelper.DATE_FORMAT.format(workInputs.getToFilter()));
		}
		// worked days
		generateWorkingDays(workInputs.getWorkInput());
		// holidays
		generateHolidays(workInputs.getHolidays());
		// freedays
		generateFreeDays(workInputs.getFreeDays());
		// month resume
		float monthHoursResult = generateMonthHoursResume(workInputs
				.getMonthHoursResume());
		handler.element(XSL_ELEMENT_MAIN_TEMPLATE_TOTALHOURS,
				String.valueOf(monthHoursResult));
		handler.endElement(XSL_TEMPLATE_MAIN_TEMPLATE);
	}

	private void generateWorkingDays(List<WorkInput> workInputs)
			throws SAXException {
		Iterator<WorkInput> i = workInputs.iterator();
		// If not worked days, create default
		if (!i.hasNext()) {
			handler.startElement(XSL_TEMPLATE_WORKING_DAYS);
			handler.endElement(XSL_TEMPLATE_WORKING_DAYS);
		}
		while (i.hasNext()) {
			WorkInput workInput = i.next();
			if (workInput == null) {
				throw new NullPointerException(
						"Parameter workInputs must not be null");
			}
			if (handler == null) {
				throw new IllegalStateException("ContentHandler not set");
			}

			handler.startElement(XSL_TEMPLATE_WORKING_DAYS);
			String dateToPrint = DateHelper.DATE_FORMAT.format(DateHelper
					.xmlGregorianCalendar2Date(workInput.getDate()));
			handler.element(XSL_ELEMENT_WORKINPUT_TEMPLATE_DATE, dateToPrint);
			handler.element(XSL_ELEMENT_WORKINPUT_TEMPLATE_HOURS,
					String.valueOf(workInput.getHours()));
			handler.element(XSL_ELEMENT_WORKINPUT_TEMPLATE_DESCRIPTION,
					workInput.getDescription());
			handler.endElement(XSL_TEMPLATE_WORKING_DAYS);
		}
	}

	private void generateHolidays(List<Holiday> holidays) throws SAXException {
		Iterator<Holiday> i = holidays.iterator();
		// If not holidays, create default
		if (!i.hasNext()) {
			handler.startElement(XSL_TEMPLATE_HOLIDAY);
			handler.endElement(XSL_TEMPLATE_HOLIDAY);
		}
		while (i.hasNext()) {
			Holiday holiday = i.next();
			if (holiday == null) {
				throw new NullPointerException(
						"Parameter holidays must not be null");
			}
			if (handler == null) {
				throw new IllegalStateException("ContentHandler not set");
			}

			handler.startElement(XSL_TEMPLATE_HOLIDAY);
			String dateToPrint = DateHelper.DATE_FORMAT.format(DateHelper
					.xmlGregorianCalendar2Date(holiday.getDate()));
			handler.element(XSL_ELEMENT_HOLIDAYS_TEMPLATE_DATE, dateToPrint);
			handler.element(XSL_ELEMENT_HOLIDAYS_TEMPLATE_COMMENT,
					holiday.getComment());
			handler.endElement(XSL_TEMPLATE_HOLIDAY);
		}
	}

	private void generateFreeDays(List<FreeDay> freeDaysList)
			throws SAXException {
		Iterator<FreeDay> i = freeDaysList.iterator();
		// If not holidays, create default
		if (!i.hasNext()) {
			handler.startElement(XSL_TEMPLATE_FREEDAY);
			handler.endElement(XSL_TEMPLATE_FREEDAY);
		}
		while (i.hasNext()) {
			FreeDay day = i.next();
			if (day == null) {
				throw new NullPointerException(
						"Parameter freeDaysList must not be null");
			}
			if (handler == null) {
				throw new IllegalStateException("ContentHandler not set");
			}

			handler.startElement(XSL_TEMPLATE_FREEDAY);
			String dateToPrint = DateHelper.DATE_FORMAT.format(DateHelper
					.xmlGregorianCalendar2Date(day.getDay()));
			handler.element(XSL_ELEMENT_FREEDAYS_TEMPLATE_DATE, dateToPrint);
			handler.element(XSL_ELEMENT_FREEDAYS_TEMPLATE_COMMENT,
					day.getComment());
			handler.endElement(XSL_TEMPLATE_FREEDAY);
		}
	}

	private float generateMonthHoursResume(Map<String, Float> monthHoursResume)
			throws SAXException {
		Set<String> keys = monthHoursResume.keySet();
		// If not holidays, create default
		if (keys.size() == 0) {
			handler.startElement(XSL_TEMPLATE_MONTHRESUME);
			handler.endElement(XSL_TEMPLATE_MONTHRESUME);
		}
		float totHours = 0;
		for (String monthName : keys) {
			totHours = totHours + monthHoursResume.get(monthName);
			Float hours = monthHoursResume.get(monthName);
			handler.startElement(XSL_TEMPLATE_MONTHRESUME);
			handler.element(XSL_ELEMENT_MONTHRESUME_TEMPLATE_MONTHNAME,
					monthName);
			handler.element(XSL_ELEMENT_MONTHRESUME_TEMPLATE_MONTHHOURS,
					String.valueOf(hours));
			handler.endElement(XSL_TEMPLATE_MONTHRESUME);
		}
		return totHours;
	}
}
