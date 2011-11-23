package com.hcalendar.config;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import com.hcalendar.ConfigurationNotInitedException;
import com.hcalendar.HCalendarConstants;

/**
 * Utils for the configuration files used
 * */
public class ConfigurationUtils {

	private static final String JAR_FILE_EXTENSION = ".jar";
	private static final String CLASS_FILE_EXTENSION = ".class";

	private static String getConfigFolderForEclipse(String path,
			String className) {
		return path.substring(0, path.length() - className.length());
	}

	private static String getConfigFolder()
			throws ConfigurationNotInitedException {
		// Eclipse mode
		Class<ConfigurationUtils> c = ConfigurationUtils.class;
		String className = c.getSimpleName() + CLASS_FILE_EXTENSION;
		String path = null;
		path = c.getResource(className).getPath();
		int to = path.indexOf(JAR_FILE_EXTENSION);
		if (to == -1)
			return getConfigFolderForEclipse(path, className);
		// jar mode
		try {
			File moduleFile = new File(ConfigurationUtils.class
					.getProtectionDomain().getCodeSource().getLocation()
					.toURI());
			path = moduleFile.getAbsolutePath();
			path = path.replace('/', File.separatorChar);
			int lastSeparator = path.lastIndexOf(File.separatorChar);
			System.out.println(path);
			return path.substring(0, lastSeparator);
		} catch (URISyntaxException e) {
			throw new ConfigurationNotInitedException(e);
		}
	}

	// File getters
	/**
	 * Get the path to the anual configuration file
	 * 
	 * @return full real path to the anual configuration file
	 * @throws ConfigurationNotInitedException
	 * */
	public static String getAnualConfigurationFile()
			throws ConfigurationNotInitedException {
		String folder = getConfigFolder();
		return folder + File.separatorChar
				+ HCalendarConstants.ANUALCONFIGURATION_FILE;
	}

	/**
	 * Get the path to the input hours file
	 * 
	 * @return full real path to the input hours file
	 * @throws ConfigurationNotInitedException
	 * */
	public static String getInputHoursFile()
			throws ConfigurationNotInitedException {
		String folder = getConfigFolder();
		return folder + File.separatorChar + HCalendarConstants.HOUR_INPUT_FILE;
	}

	/**
	 * Get the path to the csv temp file
	 * 
	 * @return full real path to the csv temp file
	 * @throws ConfigurationNotInitedException
	 * */
	public static String getCSVTempFile()
			throws ConfigurationNotInitedException {
		String folder = getConfigFolder();
		return folder + File.separatorChar
				+ HCalendarConstants.EXPORT_CSV_TEMP_FILE;
	}

	// File exist check

	/**
	 * Check if anual configuration file exists
	 * 
	 * @return existenz of the file
	 * @throws ConfigurationNotInitedException
	 * */
	public static boolean existAnualConfigurationFile()
			throws ConfigurationNotInitedException {
		File file = new File(getAnualConfigurationFile());
		return file.exists();
	}

	/**
	 * Check if input hours file exists
	 * 
	 * @return existenz of the file
	 * @throws ConfigurationNotInitedException
	 * */
	public static boolean existInputHoursFile() throws IOException,
			ConfigurationNotInitedException {
		File file = new File(getInputHoursFile());
		return file.exists();
	}

}
