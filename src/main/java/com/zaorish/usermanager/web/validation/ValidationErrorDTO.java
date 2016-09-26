package com.zaorish.usermanager.web.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDTO {

	private List<FieldErrorDTO> errors = new ArrayList<>();

	public void addFieldError(final String path, final String message) {
		FieldErrorDTO error = new FieldErrorDTO(path, message);
		errors.add(error);
	}

	public List<FieldErrorDTO> getErrors() {
		return errors;
	}

}
