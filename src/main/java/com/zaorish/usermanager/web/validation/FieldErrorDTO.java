package com.zaorish.usermanager.web.validation;

public class FieldErrorDTO {

	private final String property;
	private final String message;

	public FieldErrorDTO(final String property, final String message) {
		this.property = property;
		this.message = message;
	}

	public String getProperty() {
		return property;
	}

	public String getMessage() {
		return message;
	}

}
