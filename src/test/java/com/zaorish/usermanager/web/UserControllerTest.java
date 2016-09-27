package com.zaorish.usermanager.web;

import com.zaorish.usermanager.model.FilterCriteria;
import com.zaorish.usermanager.model.PaginationInformation;
import com.zaorish.usermanager.model.User;
import com.zaorish.usermanager.model.UserResource;
import com.zaorish.usermanager.service.UserService;
import com.zaorish.usermanager.web.validation.PagedResponse;
import com.zaorish.usermanager.web.validation.UserResourceValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	private UserController userController;

	@Mock
	private UserService userService;
	@Mock
	private UserDataTransformer userDataTransformer;
	@Mock
	private UserResourceValidator validator;
	@Mock
	private LinkHeaderDecorator linkHeaderDecorator;

	@Before
	public void setUp() {
		userController = new UserController(userService, userDataTransformer, validator, linkHeaderDecorator);
	}

	@Test
	public void shouldGetPaginated() {
		// given
		FilterCriteria criteria = new FilterCriteria();
		PaginationInformation pagination = new PaginationInformation(1, 2);
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance();

		List<User> users = new ArrayList<>();
		given(userService.getAll(criteria, pagination)).willReturn(new PagedResponse<>(5, users, 1, 2));

		List<UserResource> resources = new ArrayList<>();
		given(userDataTransformer.transformIntoUserResource(users)).willReturn(resources);

		given(linkHeaderDecorator.decorateLinkHeader(uriBuilder, pagination, "users")).willReturn("value");

		// when
		ResponseEntity<PagedResponse<UserResource>> result = userController.findPaginated(criteria, pagination, uriBuilder);

		// then
		assertThat(result.getBody().getResults(), is(resources));
		assertThat(result.getStatusCode(), is(HttpStatus.OK));
		assertThat(result.getHeaders().get("Link"), is(asList("value")));
	}

	@Test
	public void shouldUpdateUser() {
		// given
		UserResource resource = new UserResource();

		String existingNickname = "existing";
		User user = new User("new_nick", "1234", "email@email.com");

		given(userService.get(existingNickname)).willReturn(Optional.of(user));
		given(userDataTransformer.transformIntoUser(resource)).willReturn(user);

		// when
		ResponseEntity<UserResource> result = userController.update(existingNickname, resource);

		// then
		verify(userService).update(existingNickname, user);
		//
		assertThat(result.getBody(), is(resource));
		assertThat(result.getStatusCode(), is(HttpStatus.OK));
	}

}