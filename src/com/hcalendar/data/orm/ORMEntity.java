package com.hcalendar.data.orm;

import java.util.Date;

import com.hcalendar.data.IDateEntity;

/**
 * Java Bean to track the changes maded by the user
 * */
public class ORMEntity implements IDateEntity {

	private Date date;
	private DateType dateType;
	private Float hours;
	private String description;

	private ORMEntity() {
		super();
	}

	public ORMEntity(Date date, DateType dateType, Float hours,
			String description) {
		this.date = date;
		this.dateType = dateType;
		this.hours = hours;
		this.description = description;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public DateType getDateType() {
		return dateType;
	}

	@Override
	public void setDateType(DateType dateType) {
		this.dateType = dateType;
	}

	@Override
	public Float getHours() {
		return hours;
	}

	@Override
	public void setHours(Float hours) {
		this.hours = hours;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public IDateEntity createEnt() {
		return new ORMEntity();
	}
}
