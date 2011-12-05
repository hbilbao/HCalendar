package com.hcalendar.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.hcalendar.HCalendarConstants;

/**
 * Utils for the configuration files used
 * */
public class ConfigurationUtils {

	// File getters
	/**
	 * Get the path to the anual configuration file
	 * 
	 * @return full real path to the anual configuration file
	 * @throws ConfigurationNotInitedException
	 * @throws IOException
	 * */
	public static File getAnualConfigurationFile()
			throws ConfigurationNotInitedException, IOException {
		File baseDir = new File(".");
		File file = new File(baseDir,
				HCalendarConstants.ANUALCONFIGURATION_FILE);
		return file.getCanonicalFile();
	}

	/**
	 * Get the path to the input hours file
	 * 
	 * @return full real path to the input hours file
	 * @throws ConfigurationNotInitedException
	 * @throws IOException
	 * */
	public static File getInputHoursFile()
			throws ConfigurationNotInitedException, IOException {
		File baseDir = new File(".");
		File file = new File(baseDir, HCalendarConstants.HOUR_INPUT_FILE);
		return file.getCanonicalFile();
	}

	/**
	 * Get the path to the csv temp file
	 * 
	 * @return full real path to the csv temp file
	 * @throws ConfigurationNotInitedException
	 * @throws IOException
	 * */
	public static File getCSVTempFile() throws ConfigurationNotInitedException,
			IOException {
		File baseDir = new File(".");
		File file = new File(baseDir, HCalendarConstants.EXPORT_CSV_TEMP_FILE);
		return file.getCanonicalFile();
	}

	/**
	 * Get the path to the pdf temp file
	 * 
	 * @return full real path to the csv temp file
	 * @throws ConfigurationNotInitedException
	 * @throws IOException
	 * */
	public static File getPDFTempFile() throws ConfigurationNotInitedException,
			IOException {
		File baseDir = new File(".");
		File file = new File(baseDir, HCalendarConstants.EXPORT_PDF_TEMP_FILE);
		return file.getCanonicalFile();
	}

	/**
	 * Get the input stream to the pdf xsl stylesheet file. Is inside the jar,
	 * so we can't get the actual file with the path.
	 * 
	 * @return Input stream to the file
	 * */
	public static InputStream getPDFStyleSheet() {
		return Thread
				.currentThread()
				.getContextClassLoader()
				.getResourceAsStream(
						HCalendarConstants.EXPORT_PDF_STYLESHEET_FILE);
	}

	// File exist check

	/**
	 * Check if anual configuration file exists
	 * 
	 * @return existenz of the file
	 * @throws ConfigurationNotInitedException
	 * @throws IOException
	 * */
	public static boolean existAnualConfigurationFile()
			throws ConfigurationNotInitedException, IOException {
		File file = getAnualConfigurationFile();
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
		File file = getInputHoursFile();
		return file.exists();
	}
}
