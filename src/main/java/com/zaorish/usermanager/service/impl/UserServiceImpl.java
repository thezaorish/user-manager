package com.zaorish.usermanager.service.impl;

import com.zaorish.usermanager.model.FilterCriteria;
import com.zaorish.usermanager.model.PaginationInformation;
import com.zaorish.usermanager.model.User;
import com.zaorish.usermanager.repository.UserDao;
import com.zaorish.usermanager.service.UserService;
import com.zaorish.usermanager.service.event.UserCreatedEvent;
import com.zaorish.usermanager.service.event.UserUpdateNicknameEvent;
import com.zaorish.usermanager.web.validation.PagedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private UserDao userDao;

	private ApplicationEventPublisher eventPublisher;

	@Autowired
	public UserServiceImpl(UserDao userDao, ApplicationEventPublisher eventPublisher) {
		this.userDao = userDao;
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void create(User user) {
		userDao.create(user);
		eventPublisher.publishEvent(new UserCreatedEvent(this, user));
		logger.info("Created user: {}", user);
	}

	@Override
	public Optional<User> get(String nickname) {
		return userDao.get(nickname);
	}

	@Override
	public PagedResponse<User> getAll(FilterCriteria filterCriteria, PaginationInformation pagination) {
		int totalCount = userDao.count(filterCriteria);
		logger.info("Retrieved {} users matching {}", totalCount, filterCriteria);
		pagination.setTotalCount(totalCount);

		List<User> users = userDao.get(filterCriteria, pagination.getStartIndex(), pagination.getEndIndex());
		return new PagedResponse<>(totalCount, users, pagination.getPage(), pagination.getSize());
	}

	@Override
	public boolean exists(String nickname) {
		return userDao.exists(nickname);
	}

	@Override
	public int count() {
		return userDao.count(null);
	}

	@Override
	public void update(String existingNickname, User updatedUser) {
		User existingUser = userDao.get(existingNickname).get();
		updateUser(existingUser, updatedUser);
		userDao.create(updatedUser);

		if (!existingNickname.equalsIgnoreCase(existingUser.getNickname())) {
			eventPublisher.publishEvent(new UserUpdateNicknameEvent(this, updatedUser));
		}
	}
	private void updateUser(User existingUser, User updatedUser) {
		existingUser.setFirstname(updatedUser.getFirstname());
		existingUser.setLastname(updatedUser.getLastname());
		existingUser.setNickname(updatedUser.getNickname());
		existingUser.setPassword(updatedUser.getPassword());
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setCountry(updatedUser.getCountry());
	}

	@Override
	public void delete(User user) {
		userDao.delete(user);
	}

}
