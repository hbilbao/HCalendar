package com.hcalendar;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class ConfigurationConstants {
	private static final String ANUALCONFIGURATION_FILE = "anualConfiguration.xml";
	private static final String HOUR_INPUT_FILE = "hours_input.xml";
	private static final String EXPORT_CSV_TEMP_FILE = "export_temp.csv";

	private static String getConfigFolderForEclipse(String path, String className) {
		return path.substring(0, path.length() - className.length());
	}

	private static String getConfigFolder() throws ConfigurationNotInitedException {
		// Eclipse mode
		Class c = ConfigurationConstants.class;
		String className = c.getSimpleName() + ".class";
		String path = null;
		path = c.getResource(className).getPath();
		int to = path.indexOf(".jar");
		if (to == -1)
			return getConfigFolderForEclipse(path, className);
		// jar mode
		try {
			File moduleFile = new File(ConfigurationConstants.class.getProtectionDomain().getCodeSource()
					.getLocation().toURI());
			path = moduleFile.getAbsolutePath();
			path = path.replace('/', File.separatorChar);
			int lastSeparator = path.lastIndexOf(File.separatorChar);
			System.out.println(path);
			return path.substring(0, lastSeparator);
		} catch (URISyntaxException e) {
			throw new ConfigurationNotInitedException(e);
		}
	}

	public static String getAnualConfigurationFile() throws IOException, ConfigurationNotInitedException {
		String folder = getConfigFolder();
		return folder + File.separatorChar + ANUALCONFIGURATION_FILE;
	}

	public static String getInputHoursFile() throws IOException, ConfigurationNotInitedException {
		String folder = getConfigFolder();
		return folder + File.separatorChar + HOUR_INPUT_FILE;
	}

	public static String getExcelTempFile() throws IOException, ConfigurationNotInitedException {
		String folder = getConfigFolder();
		return folder + File.separatorChar + EXPORT_CSV_TEMP_FILE;
	}

	public static boolean existAnualConfigurationFile() throws IOException, ConfigurationNotInitedException {
		File file = new File(getAnualConfigurationFile());
		return file.exists();
	}

	public static boolean existInputHoursFile() throws IOException, ConfigurationNotInitedException {
		File file = new File(getInputHoursFile());
		return file.exists();
	}

}
