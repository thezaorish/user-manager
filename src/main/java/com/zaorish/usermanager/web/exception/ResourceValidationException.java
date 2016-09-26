package com.zaorish.usermanager.web.exception;

import org.springframework.validation.Errors;

public class ResourceValidationException extends IllegalArgumentException {

	private final Object object;
	private final Errors errors;

	public ResourceValidationException(String message, Object object, Errors errors) {
		super(message);
		this.object = object;
		this.errors = errors;
	}

	public Object getObject() {
		return object;
	}

	public Errors getErrors() {
		return errors;
	}

}
