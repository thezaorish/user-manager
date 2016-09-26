package com.zaorish.usermanager.web.validation;

import com.zaorish.usermanager.model.UserResource;
import com.zaorish.usermanager.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserResourceValidatorTest {

	private UserResourceValidator validator;

	@Mock
	private UserService userService;

	private UserResource userResource;
	private Errors errors;

	@Before
	public void setUp() {
		validator = new UserResourceValidator(userService);

		userResource = new UserResource();
		errors = new BeanPropertyBindingResult(userResource, "");
	}

	@Test
	public void shouldValidateResource() {
		// given a valid resource
		userResource.setNickname("nickname");
		userResource.setEmail("email@some.com");
		userResource.setPassword("password");

		// when
		validator.validate(userResource, errors);

		// then
		assertThat(errors.hasErrors(), is(false));
	}

	@Test
	public void shouldInvalidateResourceWithMissingMandatoryFields() {
		// given invalid resource

		// when
		validator.validateInternal(errors, userResource);

		// then
		assertThat(errors.getErrorCount(), is(3));
		assertThat(errors.getFieldError("nickname"), notNullValue());
		assertThat(errors.getFieldError("password"), notNullValue());
		assertThat(errors.getFieldError("email"), notNullValue());
	}

	@Test
	public void shouldInvalidateResourceWithExistingNickname() {
		// given resource where nickname already exists
		userResource.setNickname("existing");
		userResource.setEmail("email@some.com");
		userResource.setPassword("password");

		given(userService.exists(userResource.getNickname())).willReturn(true);

		// when
		validator.validateInternal(errors, userResource);

		// then
		assertThat(errors.getErrorCount(), is(1));
		assertThat(errors.getFieldError("nickname").getDefaultMessage(), is("Nickname already taken!"));
	}

}