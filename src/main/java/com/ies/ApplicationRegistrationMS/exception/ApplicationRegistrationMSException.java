package com.ies.ApplicationRegistrationMS.exception;

public class ApplicationRegistrationMSException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ApplicationRegistrationMSException(String errors) {
		super(errors);
	}
}
