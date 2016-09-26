package com.zaorish.usermanager.web.exception;

public class ResourceNotFoundException extends IllegalArgumentException {

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
