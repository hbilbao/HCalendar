package com.hcalendar.data.orm.exception;

public class ORMException extends Exception {

	private static final long serialVersionUID = -4263926609773591304L;

	public ORMException() {
		super();
	}

	public ORMException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ORMException(String arg0) {
		super(arg0);
	}

	public ORMException(Throwable arg0) {
		super(arg0);
	}

}
