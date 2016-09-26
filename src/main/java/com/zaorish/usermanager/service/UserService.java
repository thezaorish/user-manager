package com.zaorish.usermanager.service;

import com.zaorish.usermanager.model.FilterCriteria;
import com.zaorish.usermanager.model.PaginationInformation;
import com.zaorish.usermanager.model.User;
import com.zaorish.usermanager.web.validation.PagedResponse;

import java.util.Optional;

public interface UserService {

	void create(User user);

	Optional<User> get(String nickname);

	PagedResponse<User> getAll(FilterCriteria criteria, PaginationInformation paginationInformation);

	boolean exists(String nickname);

	int count();

	void update(String nickname, User user);

	void delete(User user);

}
