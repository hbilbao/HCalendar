package com.hcalendar;

/**
 * Exception thrown by configuration file readers
 * */
public class ConfigurationNotInitedException extends Exception {

	public ConfigurationNotInitedException() {
		super();
	}

	public ConfigurationNotInitedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ConfigurationNotInitedException(String arg0) {
		super(arg0);
	}

	public ConfigurationNotInitedException(Throwable arg0) {
		super(arg0);
	}

	private static final long serialVersionUID = 7964922658977691217L;

}
