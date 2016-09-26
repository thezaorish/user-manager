package com.zaorish.usermanager.service.impl;

import com.zaorish.usermanager.model.FilterCriteria;
import com.zaorish.usermanager.model.PaginationInformation;
import com.zaorish.usermanager.model.User;
import com.zaorish.usermanager.repository.UserDao;
import com.zaorish.usermanager.service.UserService;
import com.zaorish.usermanager.service.event.UserCreatedEvent;
import com.zaorish.usermanager.service.event.UserUpdateNicknameEvent;
import com.zaorish.usermanager.web.validation.PagedResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

	private UserService userService;

	@Mock
	private UserDao userDao;
	@Mock
	private ApplicationEventPublisher eventPublisher;

	@Before
	public void setUp() {
		userService = new UserServiceImpl(userDao, eventPublisher);
	}

	@Test
	public void shouldCreateUser() {
		// given
		User user = new User("", "", "");

		// when
		userService.create(user);

		// then
		verify(userDao).create(user);
		//
		ArgumentCaptor<UserCreatedEvent> emailCaptor = ArgumentCaptor.forClass(UserCreatedEvent.class);
		verify(eventPublisher).publishEvent(emailCaptor.capture());
		assertThat(emailCaptor.getValue().getUser(), is(user));
	}

	@Test
	public void shouldUpdateUser() {
		// given an existing user
		User user = new User("existing", "1234", "email@email.com");
		given(userDao.get("existing")).willReturn(Optional.of(user));

		User newUser = new User("existing", "1234", "email@email.com").withFirstname("Firstname").withLastname("Lastname").from("Country");

		// when
		userService.update("existing", newUser);

		// then
		verify(userDao).create(newUser);
		verify(eventPublisher, never()).publishEvent(any(UserUpdateNicknameEvent.class));
	}

	@Test
	public void shouldUpdateUserAndNickname() {
		// given an existing user
		String existingNickname = "existing";

		User user = new User(existingNickname, "1234", "email@email.com");
		given(userDao.get(existingNickname)).willReturn(Optional.of(user));

		// changing his nickname
		User newUser = new User("nickname", user.getPassword(), user.getEmail());

		// when
		userService.update(existingNickname, newUser);

		// then
		verify(userDao).create(newUser);
		//
		ArgumentCaptor<UserUpdateNicknameEvent> emailCaptor = ArgumentCaptor.forClass(UserUpdateNicknameEvent.class);
		verify(eventPublisher).publishEvent(emailCaptor.capture());
		assertThat(emailCaptor.getValue().getUser(), is(newUser));
	}

	@Test
	public void shouldGetPaginated() {
		// given
		FilterCriteria criteria = new FilterCriteria();
		given(userDao.count(criteria)).willReturn(8);
		ArrayList<User> users = new ArrayList<>();

		given(userDao.get(criteria, 3, 6)).willReturn(users);

		// when
		PagedResponse<User> response = userService.getAll(criteria, new PaginationInformation(2, 3));

		// then
		assertThat(response.getResults(), is(users));
		assertThat(response.getTotal(), is(8));
		assertThat(response.getTotalPages(), is(3));
	}

}