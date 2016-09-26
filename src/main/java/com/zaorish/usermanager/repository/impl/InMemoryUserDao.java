package com.zaorish.usermanager.repository.impl;

import com.zaorish.usermanager.model.FilterCriteria;
import com.zaorish.usermanager.model.User;
import com.zaorish.usermanager.repository.UserDao;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Component
public class InMemoryUserDao implements UserDao {

	private List<User> users = new ArrayList<>();

	@Override
	public void create(User user) {
		users.add(user);
	}

	@Override
	public Optional<User> get(String nickname) {
		List<User> collect = users.stream().filter(it -> it.getNickname().equalsIgnoreCase(nickname)).collect(toList());
		return collect.isEmpty() ? Optional.empty() : Optional.of(collect.get(0));
	}

	@Override
	public int count(FilterCriteria criteria) {
		return filtered(criteria).size();
	}

	@Override
	public List<User> get(FilterCriteria criteria, int fromIndex, int toIndex) {
		return filtered(criteria).subList(fromIndex, toIndex);
	}

	private List<User> filtered(FilterCriteria criteria) {
		return users.stream().filter(it -> {
			return null == criteria || StringUtils.isEmpty(criteria.getCountry()) || criteria.getCountry().equals(it.getCountry());
		}).collect(toList());
	}

	@Override
	public boolean exists(String nickname) {
		return !get(nickname).equals(Optional.empty());
	}

	@Override
	public void delete(User user) {
		users.remove(user);
	}

	@Override
	public void deleteAll() {
		users.clear();
	}

}
