package com.hcalendar.data.crud.exception;

public class CRUDException extends Exception {

	private static final long serialVersionUID = 8528932183357356621L;

	public CRUDException() {
		super();
	}

	public CRUDException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CRUDException(String arg0) {
		super(arg0);
	}

	public CRUDException(Throwable arg0) {
		super(arg0);
	}

}
