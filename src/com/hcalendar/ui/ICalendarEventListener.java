package com.hcalendar.ui;

import java.util.Date;

import com.hcalendar.data.IDateEntity;
import com.hcalendar.ui.widgets.ICalendarActionProvider;

public interface ICalendarEventListener {

	void onDateChanged(Date date, Boolean selected,
			ICalendarActionProvider actionProvider);

	void onDataInput(IDateEntity entity);
}
