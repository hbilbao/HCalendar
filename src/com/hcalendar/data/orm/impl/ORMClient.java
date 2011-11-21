package com.hcalendar.data.orm.impl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.hcalendar.data.IDateEntity;
import com.hcalendar.data.crud.CRUDManager;
import com.hcalendar.data.crud.exception.CRUDException;
import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.data.orm.exception.ORMException;
import com.hcalendar.data.xml.userconfiguration.ObjectFactory;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration;
import com.hcalendar.data.xml.workedhours.AnualHours;

class ORMClient implements IORMClient {

	private static List<IDateEntity> changesList = new ArrayList<IDateEntity>();

	private UserConfiguration user;
	private AnualHours anualHours;

	public ORMClient() throws ORMException {
		// Load form XML. If are empty, create a default one.
		try {
			try {
				setUserConfiguration(CRUDManager.loadUserConfigurationFromXML());
			} catch (CRUDException e) {
				if (e.getCause() instanceof FileNotFoundException) {
					ObjectFactory of = ObjectFactory.class.newInstance();
					setUserConfiguration(of.createUserConfiguration());
				}
			}
			try {
				setAnualHours(CRUDManager.loadAnualHoursFromXML());
			} catch (CRUDException e) {
				if (e.getCause() instanceof FileNotFoundException) {
					com.hcalendar.data.xml.workedhours.ObjectFactory of = com.hcalendar.data.xml.workedhours.ObjectFactory.class
							.newInstance();
					setAnualHours(of.createAnualHours());
				}
			}
		} catch (Exception e) {
			throw new ORMException(e);
		}
	}

	private void applyChange(IDateEntity changedDate, String profileName) throws ORMException {
		switch (changedDate.getDateType()) {
		case FREE_DAY:
			ORMHelper.addFreeDays(getUserConfiguration(), getAnualHours(), changedDate.getDate(),
					changedDate.getDescription(), profileName);
			break;
		case HOLIDAYS:
			ORMHelper.addHolidays(getAnualHours(), getUserConfiguration(), changedDate.getDate(),
					changedDate.getDescription(), profileName);
			break;
		case WORK_DAY:
			ORMHelper.addWorkDays(getAnualHours(), getUserConfiguration(), changedDate.getDate(),
					changedDate.getHours(), changedDate.getDescription(), profileName);
			break;
		}
	}

	// getter and setters
	private void setUserConfiguration(UserConfiguration userRead) {
		user = userRead;
	}

	private void setAnualHours(AnualHours hours) {
		anualHours = hours;
	}

	@Override
	public void addChange(String profileName, IDateEntity entity) throws ORMException {
		changesList.add(entity);
		if (profileName != null)
			applyChange(entity, profileName);
	}

	@Override
	public void persist(ENTITY_TYPE entityType) throws ORMException {
		try {
			if (ENTITY_TYPE.ANUALHOURS.equals(entityType)) {
				CRUDManager.saveAnualHours(getAnualHours());
				// Cargar en memoria el fichero
				setAnualHours(CRUDManager.loadAnualHoursFromXML());
			} else if (ENTITY_TYPE.USERCONFIGURATION.equals(entityType)) {
				CRUDManager.saveUserConfiguration(getUserConfiguration());
				// Cargar en memoria el fichero
				setUserConfiguration(CRUDManager.loadUserConfigurationFromXML());
			}
		} catch (CRUDException e) {
			throw new ORMException(e);
		} finally {
			reloadEntitys();
		}
	}

	@Override
	public UserConfiguration getUserConfiguration() throws ORMException {
		if (user == null)
			try {
				setUserConfiguration(CRUDManager.loadUserConfigurationFromXML());
			} catch (Exception e) {
				throw new ORMException(e);
			}
		return user;
	}

	@Override
	public AnualHours getAnualHours() throws ORMException {
		if (anualHours == null)
			try {
				setAnualHours(CRUDManager.loadAnualHoursFromXML());
			} catch (Exception e) {
				throw new ORMException(e);
			}
		return anualHours;
	}

	@Override
	public void reloadEntitys() throws ORMException {
		try {
			try {
				setUserConfiguration(CRUDManager.loadUserConfigurationFromXML());
			} catch (CRUDException e) {
				if (e.getCause() instanceof FileNotFoundException) {
					System.out
							.println("WARNING: Imposible cargar el fichero de configuracion de usuario, no existe");
				}
			}
			try {
				setAnualHours(CRUDManager.loadAnualHoursFromXML());
			} catch (CRUDException e) {
				if (e.getCause() instanceof FileNotFoundException) {
					System.out
							.println("WARNING: Imposible cargar el fichero de imputación de horas, no existe");
				}
			}
		} catch (Exception e) {
			throw new ORMException(e);
		}
	}
}
