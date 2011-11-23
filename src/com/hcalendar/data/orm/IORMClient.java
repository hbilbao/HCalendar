package com.hcalendar.data.orm;

import com.hcalendar.data.IDateEntity;
import com.hcalendar.data.orm.exception.ORMException;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration;
import com.hcalendar.data.xml.workedhours.AnualHours;

public interface IORMClient {

	public enum ENTITY_TYPE {
		USERCONFIGURATION, ANUALHOURS
	};

	void addChange(String profileName, IDateEntity entity) throws ORMException;

	void persist(ENTITY_TYPE entityType) throws ORMException;

	UserConfiguration getAnualConfiguration() throws ORMException;

	AnualHours getAnualHours() throws ORMException;

	void reloadEntitys() throws ORMException;

}
