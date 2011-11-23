package com.hcalendar.data.exception;

/**
 * BusinessException thrown by the business operations
 * */
public class BusinessException extends Exception {

	private static final long serialVersionUID = 5462229351020234861L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

}
