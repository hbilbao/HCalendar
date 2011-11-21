package com.hcalendar.data.crud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;

import com.hcalendar.ConfigurationConstants;
import com.hcalendar.data.crud.exception.CRUDException;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration.User;
import com.hcalendar.data.xml.workedhours.AnualHours;
import com.hcalendar.data.xml.workedhours.AnualHours.UserInput;

public class CRUDManager {

	// Create, Update
	public static void saveUserConfiguration(UserConfiguration userConfig) throws CRUDException {
		JAXBContext jaxbContext;
		FileWriter fichero = null;
		try {
			jaxbContext = JAXBContext.newInstance(UserConfiguration.class);
			StringWriter writer = new StringWriter();
			jaxbContext.createMarshaller().marshal(userConfig, writer);

			// Write to a file
			fichero = new FileWriter(ConfigurationConstants.getAnualConfigurationFile());
			PrintWriter pw = new PrintWriter(fichero);
			pw.println(writer);
		} catch (Exception e) {
			throw new CRUDException(e);
		} finally {
			if (fichero != null)
				try {
					fichero.close();
				} catch (IOException e) {
					throw new CRUDException(e);
				}
		}
	}

	public static void saveAnualHours(AnualHours aHours) throws CRUDException {
		FileWriter fichero = null;
		try {
			// write it out as XML
			final JAXBContext jaxbContext = JAXBContext.newInstance(AnualHours.class);
			StringWriter writer = new StringWriter();
			jaxbContext.createMarshaller().marshal(aHours, writer);

			// Write to a file
			fichero = new FileWriter(ConfigurationConstants.getInputHoursFile());
			PrintWriter pw = new PrintWriter(fichero);
			pw.println(writer);
		} catch (Exception e) {
			throw new CRUDException(e);
		} finally {
			if (fichero != null)
				try {
					fichero.close();
				} catch (IOException e) {
					throw new CRUDException(e);
				}
		}
	}

	// Delete
	public static void deleteProfile(UserConfiguration userConfiguration, AnualHours anualHours,
			String profileName) throws CRUDException {
		List<User> users = userConfiguration.getUser();
		for (User us : users) {
			if (us.getName().equals(profileName)) {
				users.remove(us);
				saveUserConfiguration(userConfiguration);
				break;
			}
		}
		// Borramos sus imputaciones de horas por si acaso
		for (UserInput user : anualHours.getUserInput()) {
			if (user.getUserName().equals(profileName)) {
				anualHours.getUserInput().remove(user);
				break;
			}
		}
		saveAnualHours(anualHours);
	}

	// Read /Retrieve
	public static UserConfiguration loadUserConfigurationFromXML() throws CRUDException {
		FileReader fr = null;
		StringBuffer strBuffer = new StringBuffer();
		try {
			// Read from file
			File archivo = new File(ConfigurationConstants.getAnualConfigurationFile());
			fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);
			strBuffer.append(br.readLine());

			// Parse the XML
			final JAXBContext jaxbContext = JAXBContext.newInstance(UserConfiguration.class);
			final UserConfiguration userRead = (UserConfiguration) jaxbContext.createUnmarshaller()
					.unmarshal(new StringReader(strBuffer.toString()));
			return userRead;
		} catch (Exception e) {
			throw new CRUDException(e);
		} finally {
			if (fr != null)
				try {
					fr.close();
				} catch (IOException e) {
					throw new CRUDException(e);
				}
		}
	}

	public static AnualHours loadAnualHoursFromXML() throws CRUDException {
		StringBuffer strBuffer = new StringBuffer();
		FileReader fr = null;
		try {
			// Read from file
			File archivo = new File(ConfigurationConstants.getInputHoursFile());
			fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);
			strBuffer.append(br.readLine());

			// Parse the XML
			final JAXBContext jaxbContext = JAXBContext.newInstance(AnualHours.class);
			final AnualHours aHours = (AnualHours) jaxbContext.createUnmarshaller().unmarshal(
					new StringReader(strBuffer.toString()));
			return aHours;
		} catch (Exception e) {
			throw new CRUDException(e);
		} finally {
			if (fr != null)
				try {
					fr.close();
				} catch (IOException e) {
					throw new CRUDException(e);
				}
		}
	}
}
