package com.zaorish.usermanager.service.event;

import com.zaorish.usermanager.model.User;
import org.springframework.context.ApplicationEvent;

public class UserUpdateNicknameEvent extends ApplicationEvent {

	private User user;

	public UserUpdateNicknameEvent(Object source, User user) {
		super(source);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
