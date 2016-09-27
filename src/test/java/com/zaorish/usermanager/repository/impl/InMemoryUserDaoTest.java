package com.zaorish.usermanager.repository.impl;

import com.zaorish.usermanager.model.FilterCriteria;
import com.zaorish.usermanager.model.User;
import com.zaorish.usermanager.repository.UserDao;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class InMemoryUserDaoTest {

	private UserDao userDao;

	@Before
	public void setUp() {
		userDao = new InMemoryUserDao();
	}

	@Test
	public void shouldGetNicknameCaseInsensitive() {
		// given
		userDao.create(new User("user", "password", "email@email.com"));

		// when
		User user = userDao.get("USER").get();

		// then
		assertThat(user.getNickname(), is("user"));
	}

	@Test
	public void shouldGetPaginated() {
		// given
		for (int i = 1; i <= 10; i++) {
			userDao.create(new User("user_" + i, "password", "email_" + i + "@email.com"));
		}

		// when
		List<User> users = userDao.get(new FilterCriteria(), 4, 6);

		// then
		assertThat(users.size(), is(2));
		assertThat(users.get(0).getNickname(), is("user_5"));
		assertThat(users.get(1).getNickname(), is("user_6"));
	}

	@Test
	public void shouldGetPaginatedWhenNoUsersInDatabase() {
		// given

		// when
		List<User> users = userDao.get(new FilterCriteria(), 4, 6);

		// then
		assertThat(users.size(), is(0));
	}

	@Test
	public void shouldGetPaginatedWhenNotEnoughUsersInDatabase() {
		// given
		for (int i = 1; i <= 5; i++) {
			userDao.create(new User("user_" + i, "password", "email_" + i + "@email.com"));
		}

		// when
		List<User> users = userDao.get(new FilterCriteria(), 4, 6);

		// then
		assertThat(users.size(), is(1));
		assertThat(users.get(0).getNickname(), is("user_5"));
	}

}