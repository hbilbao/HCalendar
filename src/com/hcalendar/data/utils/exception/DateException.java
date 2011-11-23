package com.hcalendar.data.utils.exception;

/**
 * Exception thrown by the date operations
 * */
public class DateException extends Exception {

	private static final long serialVersionUID = 2080737904025246864L;

	public DateException() {
		super();
	}

	public DateException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DateException(String arg0) {
		super(arg0);
	}

	public DateException(Throwable arg0) {
		super(arg0);
	}

}
