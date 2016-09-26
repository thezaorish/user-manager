package com.zaorish.usermanager.web.exception;

import com.zaorish.usermanager.web.validation.ApiErrorDTO;
import com.zaorish.usermanager.web.validation.ValidationErrorDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected final ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		final ApiErrorDTO apiErrorDTO = message(ex);
		return handleExceptionInternal(ex, apiErrorDTO, headers, BAD_REQUEST, request);
	}

	// 400
	@ExceptionHandler(value = {ResourceValidationException.class})
	@ResponseStatus(BAD_REQUEST)
	@ResponseBody
	public final ValidationErrorDTO handleValidation(final ResourceValidationException ex) {
		logger.error("handleValidation: Exception occurred " + ex.getMessage(), ex);

		Errors result = ex.getErrors();
		ValidationErrorDTO dto = processAllErrors(result);

		return dto;
	}

	// 404
	@ExceptionHandler(value = {ResourceNotFoundException.class})
	@ResponseStatus(NOT_FOUND)
	@ResponseBody
	public final ApiErrorDTO handleNotFound(final ResourceNotFoundException ex) {
		logger.error("handleNotFound: Exception occurred " + ex.getMessage(), ex);

		ApiErrorDTO apiErrorDTO = message(ex);
		return apiErrorDTO;
	}

	// 500
	@ExceptionHandler(value = {Exception.class})
	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ResponseBody
	public final ApiErrorDTO handleInternalServerError(final RuntimeException ex) {
		logger.error("handleBadRequest: Exception occurred " + ex.getMessage(), ex);

		ApiErrorDTO apiErrorDTO = message(ex);
		return apiErrorDTO;
	}

	private ValidationErrorDTO processAllErrors(final Errors errors) {
		ValidationErrorDTO error = new ValidationErrorDTO();

		for (FieldError fieldError : errors.getFieldErrors()) {
			String errorMessage = fieldError.getDefaultMessage();
			error.addFieldError(fieldError.getField(), errorMessage);
		}

		for (ObjectError objectError : errors.getGlobalErrors()) {
			String errorMessage = objectError.getDefaultMessage();
			error.addFieldError(errors.getObjectName(), errorMessage);
		}

		return error;
	}

	private ApiErrorDTO message(final Exception ex) {
		final String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
		final String detailedMessage = ex.getClass().getSimpleName();

		return new ApiErrorDTO(message, detailedMessage);
	}

}
