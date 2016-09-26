package com.zaorish.usermanager.web.custom;

import com.zaorish.usermanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSizeEndpoint implements Endpoint<Integer> {

	private UserService userService;

	@Autowired
	public DatabaseSizeEndpoint(UserService userService) {
		this.userService = userService;
	}

	@Override
	public String getId() {
		return "databaseSize";
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isSensitive() {
		return false;
	}

	@Override
	public Integer invoke() {
		return userService.count();
	}

}
