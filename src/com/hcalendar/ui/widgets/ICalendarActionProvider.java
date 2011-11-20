package com.hcalendar.ui.widgets;

import java.util.Date;

public interface ICalendarActionProvider {

	public enum LIST_TYPE {
		CALENDAR_FREEDAY, USER_HOLIDAYS, USER_NOT_WORKINGDAY, USER_WORKINGDAY
	}

	void addDayToList(Date date, LIST_TYPE type);

	void removeDayFromList(Date date, LIST_TYPE type);
}
