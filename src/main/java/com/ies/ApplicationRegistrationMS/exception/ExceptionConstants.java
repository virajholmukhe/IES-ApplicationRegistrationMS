package com.ies.ApplicationRegistrationMS.exception;

import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:messages.properties")
public enum ExceptionConstants {

	EMAIL_ALREADY_USED("email.already.used"),
	EMAIL_NOT_EXIST("email.not.exist"),
	INVALID_AADHAR_NUMBER("invalid.aadhar.number"),
	USER_ALREADY_CREATED_APPLICATION("user.already.created.application"),
	INVALID_TOKEN("invalid.token"),
	CASE_NUMBER_NOT_FOUND("case.number.not.found");

	private final String type;
	
	private ExceptionConstants(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return this.type;
	}
}
