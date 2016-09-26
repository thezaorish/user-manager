package com.zaorish.usermanager.web;

import com.zaorish.usermanager.model.User;
import com.zaorish.usermanager.model.UserResource;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class UserDataTransformer {

	public User transformIntoUser(UserResource userResource) {
		User user = new User(userResource.getNickname(), userResource.getPassword(), userResource.getEmail());
		user.setFirstname(userResource.getFirstname());
		user.setLastname(userResource.getLastname());
		user.setCountry(userResource.getCountry());
		return user;
	}

	public List<UserResource> transformIntoUserResource(List<User> users) {
		return users.stream().map(this::transformIntoUserResource).collect(toList());
	}

	public UserResource transformIntoUserResource(User user) {
		UserResource userResource = new UserResource();
		userResource.setFirstname(user.getFirstname());
		userResource.setLastname(user.getLastname());
		userResource.setNickname(user.getNickname());
		userResource.setEmail(user.getEmail());
		userResource.setCountry(user.getCountry());
		return userResource;
	}

}
