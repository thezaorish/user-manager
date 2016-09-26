package com.zaorish.usermanager.web.validation;

public class ApiErrorDTO {

	private final String message;
	private final String developerMessage;

	public ApiErrorDTO(String message, String developerMessage) {
		this.message = message;
		this.developerMessage = developerMessage;
	}

	public String getMessage() {
		return message;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

}
