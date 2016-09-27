package com.zaorish.usermanager.repository;

import com.zaorish.usermanager.model.FilterCriteria;
import com.zaorish.usermanager.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

	void create(User user);

	Optional<User> get(String nickname);

	List<User> get(FilterCriteria criteria, int fromIndex, int toIndex);

	int count(FilterCriteria filterCriteria);

	boolean exists(String nickname);

	void delete(User user);

	void deleteAll();

}
