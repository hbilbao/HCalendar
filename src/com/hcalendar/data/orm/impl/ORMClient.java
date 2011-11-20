package com.hcalendar.data.orm.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.hcalendar.ConfigurationConstants;
import com.hcalendar.ConfigurationNotInitedException;
import com.hcalendar.data.IDateEntity;
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
				loadUserConfigurationFromXML();
			} catch (FileNotFoundException e) {
				ObjectFactory of = ObjectFactory.class.newInstance();
				this.user = of.createUserConfiguration();
			}
			try {
				loadAnualHoursFromXML();
			} catch (FileNotFoundException e) {
				com.hcalendar.data.xml.workedhours.ObjectFactory of = com.hcalendar.data.xml.workedhours.ObjectFactory.class
						.newInstance();
				this.anualHours = of.createAnualHours();
			}
		} catch (IOException e) {
			throw new ORMException(e);
		} catch (ConfigurationNotInitedException e) {
			throw new ORMException(e);
		} catch (JAXBException e) {
			throw new ORMException(e);
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

	private void saveUserConfiguration() throws JAXBException, IOException, ORMException,
			ConfigurationNotInitedException {
		final JAXBContext jaxbContext = JAXBContext.newInstance(UserConfiguration.class);
		StringWriter writer = new StringWriter();
		jaxbContext.createMarshaller().marshal(getUserConfiguration(), writer);

		// Write to a file
		FileWriter fichero = new FileWriter(ConfigurationConstants.getAnualConfigurationFile());
		PrintWriter pw = new PrintWriter(fichero);
		pw.println(writer);
		fichero.close();
	}

	private void saveAnualHours() throws JAXBException, IOException, ORMException,
			ConfigurationNotInitedException {
		// write it out as XML
		final JAXBContext jaxbContext = JAXBContext.newInstance(AnualHours.class);
		StringWriter writer = new StringWriter();
		jaxbContext.createMarshaller().marshal(getAnualHours(), writer);

		// Write to a file
		FileWriter fichero = new FileWriter(ConfigurationConstants.getInputHoursFile());
		PrintWriter pw = new PrintWriter(fichero);
		pw.println(writer);
		fichero.close();
	}

	// getter and setters
	private void setUserConfiguration(UserConfiguration userRead) {
		user = userRead;
	}

	private void setAnualHours(AnualHours hours) {
		anualHours = hours;
	}

	// Load from XML
	private void loadUserConfigurationFromXML() throws IOException, JAXBException,
			ConfigurationNotInitedException {
		StringBuffer strBuffer = new StringBuffer();
		// Read from file
		File archivo = new File(ConfigurationConstants.getAnualConfigurationFile());
		FileReader fr = new FileReader(archivo);
		BufferedReader br = new BufferedReader(fr);
		strBuffer.append(br.readLine());
		fr.close();

		// Parse the XML
		final JAXBContext jaxbContext = JAXBContext.newInstance(UserConfiguration.class);
		final UserConfiguration userRead = (UserConfiguration) jaxbContext.createUnmarshaller().unmarshal(
				new StringReader(strBuffer.toString()));
		setUserConfiguration(userRead);
	}

	private void loadAnualHoursFromXML() throws IOException, JAXBException, ConfigurationNotInitedException {
		StringBuffer strBuffer = new StringBuffer();
		// Read from file
		File archivo = new File(ConfigurationConstants.getInputHoursFile());
		FileReader fr = new FileReader(archivo);
		BufferedReader br = new BufferedReader(fr);
		strBuffer.append(br.readLine());
		fr.close();

		// Parse the XML
		final JAXBContext jaxbContext = JAXBContext.newInstance(AnualHours.class);
		final AnualHours aHours = (AnualHours) jaxbContext.createUnmarshaller().unmarshal(
				new StringReader(strBuffer.toString()));
		setAnualHours(aHours);
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
				saveAnualHours();
				// Cargar en memoria el fichero
				loadAnualHoursFromXML();
			} else if (ENTITY_TYPE.USERCONFIGURATION.equals(entityType)) {
				saveUserConfiguration();
				// Cargar en memoria el fichero
				loadUserConfigurationFromXML();
			}
		} catch (JAXBException e) {
			throw new ORMException(e);
		} catch (IOException e) {
			throw new ORMException(e);
		} catch (ConfigurationNotInitedException e) {
			throw new ORMException(e);
		} finally {
			reloadEntitys();
		}
	}

	@Override
	public UserConfiguration getUserConfiguration() throws ORMException {
		if (user == null)
			try {
				loadUserConfigurationFromXML();
			} catch (Exception e) {
				throw new ORMException(e);
			}
		return user;
	}

	@Override
	public AnualHours getAnualHours() throws ORMException {
		if (anualHours == null)
			try {
				loadAnualHoursFromXML();
			} catch (Exception e) {
				throw new ORMException(e);
			}
		return anualHours;
	}

	@Override
	public void reloadEntitys() throws ORMException {
		try {
			try {
				loadUserConfigurationFromXML();
			} catch (FileNotFoundException e) {
				System.out
						.println("WARNING: Imposible cargar el fichero de configuracion de usuario, no existe");
			}
			try {
				loadAnualHoursFromXML();
			} catch (FileNotFoundException e) {
				System.out.println("WARNING: Imposible cargar el fichero de imputación de horas, no existe");
			}
		} catch (Exception e) {
			throw new ORMException(e);
		}
	}
}
