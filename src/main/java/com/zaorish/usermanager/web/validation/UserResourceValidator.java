package com.zaorish.usermanager.web.validation;

import com.zaorish.usermanager.model.UserResource;
import com.zaorish.usermanager.service.UserService;
import com.zaorish.usermanager.web.exception.ResourceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class UserResourceValidator implements Validator {

	private UserService userService;

	@Autowired
	public UserResourceValidator(UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UserResource.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserResource userResource = (UserResource) target;
		validateInternal(errors, userResource);
		checkForErrors(userResource, errors);
	}

	void validateInternal(Errors errors, UserResource userResource) {
		String nickname = userResource.getNickname();
		if (isEmpty(nickname)) {
			errors.rejectValue("nickname", null, "Nickname is mandatory!");
		} else if (userService.exists(nickname)) {
			errors.rejectValue("nickname", null, "Nickname already taken!");
		}

		if (isEmpty(userResource.getPassword())) {
			errors.rejectValue("password", null, "Password is mandatory!");
		}

		if (isEmpty(userResource.getEmail())) {
			errors.rejectValue("email", null, "Email is mandatory!");
		}
	}

	private void checkForErrors(Object target, Errors errors) {
		if (errors.hasErrors()) {
			throw new ResourceValidationException("Found " + errors + " while validating " + target, target, errors);
		}
	}

}
