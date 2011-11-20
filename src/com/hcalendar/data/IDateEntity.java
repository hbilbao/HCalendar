package com.hcalendar.data;

import java.util.Date;

public interface IDateEntity {

	public enum DateType {
		FREE_DAY, WORK_DAY, HOLIDAYS
	};

	public Date getDate();

	public void setDate(Date date);

	public DateType getDateType();

	public void setDateType(DateType dateType);

	public Float getHours();

	public void setHours(Float hours);

	public String getDescription();

	public void setDescription(String description);
}
